////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.util.IterableNodeList;
import com.haradatakahiko.security.util.XmlDocument;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Node;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ファイルの変更をチェックするメインプログラム
 * @author はらだ　たかひこ
 */
public final class FileChangeChecker
{
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileChangeChecker.class);
    
    /** ディフォルトの設定ファイル名 */
    private static final String DEFAULT_CONFIG = "config.xml";
    
    /** チェック結果テーブル */
    private ResultTable<String, String> result = new ResultTable<>();
    
    /** このクラスの唯一のインスタンス */
    private static FileChangeChecker instance = new FileChangeChecker();
    
    /** 設定ファイルXmlドキュメント */
    protected XmlDocument config = new XmlDocument();
    
    /** 変更内容のMap */
    protected Map<String, ModifyType> modifyMap = new HashMap<>();
    
    /** ハッシュ値計算エンジン */
    protected IHashMaker hashMaker = null;
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     */
    protected FileChangeChecker()
    {
        initializeCounter();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * カウンタを初期化する
     */
    protected void initializeCounter()
    {
        modifyMap.clear();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 処理実行メソッド
     * @param args コマンドライン引数
     */
    public synchronized Map<String, ModifyType> execute(final String configPath)
    {
        try
        {
            configure(configPath);
            String root = getConfigValue("/config/targets/target");
            LOGGER.info(root);
            deleteCheck();
            walk(Paths.get(root));
            result.exportResult();
            return modifyMap;
        }
        catch(IOException | NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * プログラムの設定を読み込む
     * @param args コマンドライン引数
     * @throws IOException 読込エラー
     * @throws NoSuchAlgorithmException 不正アルゴリズム
     */
    protected void configure(final String configPath) throws IOException, NoSuchAlgorithmException
    {
        getConfig(configPath);
        hashMaker = configureHashMaker();
        Set<String> ignoreSet = getIgnoreSet();
        result.setDataFile(getConfigValue("/config/data/file"));
        result.importResult();
        result.setIgnoreExtensionSet(ignoreSet);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 対象外ファイルの拡張子セットを取得する
     * @return 対象外ファイルの拡張子セット
     * @throws IOException 読込エラー
     */
    protected Set<String> getIgnoreSet() throws IOException
    {
        Set<String> ignoreExtSet = new HashSet<>();
        try
        {
            IterableNodeList nodeList = config.getNodeList("/config/ignores/extension");
            for(Node node : nodeList)
            {
                String extension = XmlDocument.getNodeText(node);
                if(!extension.startsWith("."))
                {
                    extension = "." + extension;
                }
                LOGGER.info(String.format("対象外拡張子：%s", extension));
                ignoreExtSet.add(extension);
            }
        }
        catch(XPathExpressionException e)
        {
            throw new IOException(e);
        }
        return ignoreExtSet;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 削除されたファイルを検索する
     */
    protected void deleteCheck()
    {
        Iterator<Map.Entry<String, String>> iterator = result.iterator();
        while(iterator.hasNext())
        {
            Map.Entry<String, String> entry = iterator.next();
            Path path = Paths.get(entry.getKey());
            if(!Files.exists(path))
            {
                modifyMap.put(entry.getKey(), ModifyType.DELETE);
                iterator.remove();
                LOGGER.warn(String.format("[%s] %s", ModifyType.DELETE, entry.getKey()));
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ディレクトリを再帰的にチェックする
     * @param path チェック対象
     * @throws IOException 読込エラー
     * @throws NoSuchAlgorithmException 不正なハッシュ関数アルゴリズムが指定された
     */
    private void walk(Path path) throws IOException, NoSuchAlgorithmException
    {
        if(path == null || !Files.exists(path))
        {
            throw new IOException(String.format("ディレクトリがみつかりません。%s", path));
        }
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(path))
        {
            for(Path entry : stream)
            {
                if(Files.isDirectory(entry))
                {
                    walk(entry);
                }
                else if(Files.isRegularFile(entry))
                {
                    checkHash(entry);
                }
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ファイルのハッシュ値をチェックする
     * @param path 対象ファイルのパス
     * @throws IOException 読込エラー
     * @throws NoSuchAlgorithmException 不正アルゴリズム
     */
    private void checkHash(Path path) throws IOException, NoSuchAlgorithmException
    {
        Path absolutePath = path.toAbsolutePath();
        byte[] hashValue = hashMaker.hash(absolutePath);
        String hashString = IHashMaker.toHexString(hashValue);
        ModifyType modifyType = result.detectModify(absolutePath.toString(), hashString);
        result.addItem(absolutePath.toString(), hashString);
        if(modifyType != ModifyType.SAME && modifyType != ModifyType.IGNORE)
        {
            modifyMap.put(absolutePath.toString(), modifyType);
        }
        LOGGER.debug(String.format("[%s] : %s", modifyType.toString(), absolutePath.toString()));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ハッシュ計算エンジンの設定を行う
     */
    protected IHashMaker configureHashMaker() throws IOException, NoSuchAlgorithmException
    {
        String algorithm = getConfigValue("/config/data/algorithm");
        LOGGER.info(String.format("ハッシュアルゴリズム：%s", algorithm));
        IHashMaker engine = new SimpleHashMaker(algorithm);
        return engine;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 設定ファイルから設定値を読み込む
     * @param xpath 設定ファイルのXPath
     * @return 設定値
     */
    public String getConfigValue(String xpath)
    {
        try
        {
            return config.getNodeText(xpath);
        }
        catch(XPathExpressionException e)
        {
            throw new RuntimeException(e);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 設定ファイルを読み込む
     * @param configPath 設定ファイルのパス
     * @throws IOException 読み込みエラー
     */
    protected void getConfig(final String configPath) throws IOException
    {
        LOGGER.info(String.format("設定ファイル：%s", configPath));
        try(InputStream stream =  new FileInputStream(configPath))
        {
            config.load(stream);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 設定XMLドキュメントオブジェクトを返す
     * @return 設定XMLドキュメントオブジェクト
     */
    public XmlDocument getConfigXml()
    {
        return config;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * このクラスの唯一のインスタンスを返す
     * @return このクラスの唯一のインスタンス
     */
    public static FileChangeChecker getInstance()
    {
        return instance;
    }
}
