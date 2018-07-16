////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.ignore;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 抽象無視対象ディレクトリエントリクラス
 * @author はらだ　たかひこ
 */
public abstract class AbstractIgnoreEntry implements Serializable
{
    /** 文字列化フォーマット */
    protected static final String STRINGIFY_FORMAT = "IgnoreEntry [%s] %s";
    
    /** 対象のパス */
    protected String path;
    
    /** タイプ */
    protected IgnoreType type;
    //----------------------------------------------------------------------------------------------
    /**
     * 無視対象化どうかを判定する
     * @param arg チェック対象のパス
     * @return 判定結果(true:無視する, false:無視しない)
     */
    public abstract boolean shouldIgnore(final String arg);
    //----------------------------------------------------------------------------------------------
    /**
     * ディフォルトコンストラクタ
     */
    protected AbstractIgnoreEntry()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param path 無視対象のパス
     * @param type 無視タイプ
     */
    protected AbstractIgnoreEntry(String path, IgnoreType type)
    {
        setPath(path);
        setIgnoreType(type);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * このオブジェクトの文字列表現を返す
     * @return このオブジェクトの文字列表現
     */
    @Override
    public String toString()
    {
        return String.format(STRINGIFY_FORMAT, type, path);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * このクラスのオブジェクトを比較して同値かチェックする
     * @param anotherObj 比較対象オブジェクト
     * @return チェック結果（true:同値、false:非同値）
     */
    @Override
    public boolean equals(Object anotherObj)
    {
        if(!(anotherObj instanceof AbstractIgnoreEntry))
        {
            return false;
        }
        if(this == anotherObj)
        {
            return true;
        }
        AbstractIgnoreEntry another = (AbstractIgnoreEntry)anotherObj;
        return (path.equals(another.getPath()) && type == another.getIgnoreType());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ハッシュコードを返す
     * @return ハッシュコード
     */
    @Override
    public int hashCode()
    {
        return (path != null) ? path.hashCode() : 0;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 無視する対象のパスをセットする
     * @param arg 無視する対象のパス
     */
    public void setPath(final String arg)
    {
        path = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 無視する対象のパスを取得する
     * @return 無視する対象のパス
     */
    public String getPath()
    {
        return (path == null) ? "" : path;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 無視タイプをセットする
     * @param arg 無視タイプ
     */
    public void setIgnoreType(IgnoreType arg)
    {
        type = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 無視タイプを取得する
     * @return 無視タイプ
     */
    public IgnoreType getIgnoreType()
    {
        return type;
    }
}
