////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 変更チェック結果を格納するハッシュテーブルを管理する
 * @author はらだ　たかひこ
 */
@SuppressWarnings("serial")
public class ResultTable<K, V> implements Serializable
{
    /** パスとハッシュ値16進数文字列のマップ */
    protected Map<K, V> map = new HashMap<>();
    
    /** 無視する拡張子セット */
    protected Set<String> ignoreExtensionSet;
    
    /** データファイルのパス */
    protected String dataFile = "datafile.ser";
    //---------------------------------------------------------------------------------------------
    /**
     * データファイルのパスをセットする
     * @param arg データファイルのパス
     */
    public void setDataFile(final String arg)
    {
        dataFile = arg;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * データファイルのパスを取得する
     * @return データファイルのパス
     */
    public String getDataFile()
    {
        return dataFile;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * ファイル情報（パスとハッシュ値）をMapに追加する
     * @param path パス
     * @param hash ハッシュ値
     */
    public void addItem(final K path, final V hash)
    {
        map.put(path, hash);
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 変更状態を判定する
     * @param path 判定対象のパス
     * @param hash ハッシュ値
     * @return 変更状態enum
     */
    public ModifyType detectModify(final K path, final V hash)
    {
        if(shoudIgnore(path))
        {
            return ModifyType.IGNORE;
        }
        V oldValue = map.get(path);
        if(oldValue == null)
        {
            map.put(path, hash);
            return ModifyType.ADD;
        }
        map.remove(path);
        map.put(path, hash);
        return oldValue.equals(hash) ? ModifyType.SAME : ModifyType.MODIFY;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * マップをファイルに出力する
     * @throws IOException 出力失敗
     */
    public void exportResult() throws IOException
    {
        File file = new File(getDataFile());
        try(FileOutputStream fout = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fout))
        {
            oos.writeObject(map);
        }
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 前回実行時のマップをインポートする
     * @throws IOException 読み込みエラー
     */
    @SuppressWarnings("unchecked")
    public void importResult() throws IOException
    {
        File file = new File(getDataFile());
        if(file.exists())
        {
            try(FileInputStream fin = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fin))
            {
                Object obj = ois.readObject();
                if(obj instanceof Map)
                {
                    map = (Map<K, V>)obj;
                }
            }
            catch(ClassNotFoundException e)
            {
                throw new IOException(e);
            }
        }
    }
    //---------------------------------------------------------------------------------------------
    /**
     * データの件数を返す
     * @return データの件数
     */
    public int getCount()
    {
        return map.size();
    }
    //---------------------------------------------------------------------------------------------
    /**
     * データの反復子を返す
     * @return iterator
     */
    public Iterator<Map.Entry<K, V>> iterator()
    {
        return map.entrySet().iterator();
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 与えられたパスが無視する対象か判定する
     * @param path パス
     * @return 判定結果
     */
    public boolean shoudIgnore(K path)
    {
        if(ignoreExtensionSet == null || path == null)
        {
            return false;
        }
        String pathString = path.toString();
        for(String ext : ignoreExtensionSet)
        {
            if(pathString.endsWith(ext))
            {
                return true;
            }
        }
        return false;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 無視するファイルの拡張子セットをセットする
     * @param arg 拡張子セット
     */
    public void setIgnoreExtensionSet(final Set<String> arg)
    {
        ignoreExtensionSet = arg;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 無視するファイルの拡張子セットを取得する
     * @return 無視するファイルの拡張子セット
     */
    public Set<String> getIgnoreExtensionSet()
    {
        return ignoreExtensionSet;
    }
}
