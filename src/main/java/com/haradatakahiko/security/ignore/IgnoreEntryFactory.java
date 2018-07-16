////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.ignore;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 無視するディレクトリエントリを管理するクラス
 * @author はらだ　たかひこ
 */
public class IgnoreEntryFactory
{
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(IgnoreEntryFactory.class);
    
    /** このクラスの唯一のインスタンス */
    private static final IgnoreEntryFactory instance = new IgnoreEntryFactory();
    //----------------------------------------------------------------------------------------------
    /** 
     * コンストラクタ
     * <pre>シングルトンにするためprotected以下</pre>
     */
    protected IgnoreEntryFactory()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * このクラスの唯一のインスタンスを返す
     * @return このクラスの唯一のインスタンス
     */
    public static IgnoreEntryFactory getInstance()
    {
        return instance;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * AbstractIgnoreEntry実装クラスのインスタンスを構築する
     * @param path パス
     * @param type 無視タイプ
     * @return AbstractIgnoreEntry実装クラスのインスタンス
     */
    public AbstractIgnoreEntry getIgnoreEntry(final String path, final String type)
    {
        if(path == null || type == null)
        {
            throw new IllegalArgumentException("パラメータがnullです");
        }
        IgnoreType ignoreType = IgnoreType.valueOf(type.toUpperCase());
        AbstractIgnoreEntry entity = null;
        switch(ignoreType)
        {
            case EXTENSION :
                entity = new FileExtensionIgnoreEntry(path, ignoreType);
                break;
            case DIRECTORY :
                entity = new DirectoryIgnoreEntry(path, ignoreType);
                break;
            case REGULAR_EXPRESSION :
                throw new IllegalArgumentException("未実装です");
                //break
        }
        return entity;
    }
}
