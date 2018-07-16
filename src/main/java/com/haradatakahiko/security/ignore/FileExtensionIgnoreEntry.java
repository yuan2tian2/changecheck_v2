////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.ignore;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ファイル拡張子で指定する無視するディレクトリエントリ
 * @author はらだ　たかひこ
 */
public class FileExtensionIgnoreEntry extends AbstractIgnoreEntry implements Serializable
{
    /** 拡張子のセパレータ */
    public static final String SEPARATOR = ".";
    //----------------------------------------------------------------------------------------------
    /**
     * ディフォルトコンストラクタ
     */
    public FileExtensionIgnoreEntry()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param path パス
     */
    public FileExtensionIgnoreEntry(String path)
    {
        this(path, IgnoreType.EXTENSION);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param path パス
     * @param type 無視するタイプ
     */
    public FileExtensionIgnoreEntry(String path, IgnoreType type)
    {
        super(path.startsWith(SEPARATOR) ? path : (SEPARATOR + path), type);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 無視対象化どうかを判定する
     * @param arg チェック対象のパス
     * @return 判定結果(true:無視する, false:無視しない)
     */
    @Override
    public boolean shouldIgnore(final String arg)
    {
        if(arg == null)
        {
            throw new IllegalArgumentException("引数がnullです");
        }
        return arg.endsWith(getPath());
    }
}
