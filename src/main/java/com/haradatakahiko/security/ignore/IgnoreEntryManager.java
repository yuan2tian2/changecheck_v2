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
public class IgnoreEntryManager
{
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(IgnoreEntryManager.class);
    
    /** このクラスの唯一のインスタンス */
    private static final IgnoreEntryManager instance = new IgnoreEntryManager();
    

    //----------------------------------------------------------------------------------------------
    /** 
     * コンストラクタ
     * <pre>シングルトンにするためprotected以下</pre>
     */
    protected IgnoreEntryManager()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * このクラスの唯一のインスタンスを返す
     * @return このクラスの唯一のインスタンス
     */
    public static IgnoreEntryManager getInstance()
    {
        return instance;
    }
}
