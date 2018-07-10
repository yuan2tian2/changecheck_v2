////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.ModifyType;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * テキストファイルにファイル変更レポートを出力する
 * @author はらだ　たかひこ
 */
public class TextReporter implements IResultReporter
{
    /** 出力に使用する文字セット */
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    
    /** 出力レコードのフォーマット */
    public static final String RECORD_FORMAT = "[%s]\t%s\r\n";
    
    /** エラーメッセージ */
    public static final String MSG_FILEPATH_NOT_SPECIFIED = "ファイルパスがセットされていません";
    
    /** エラーメッセージ */
    public static final String MSG_INVALID_ARGUMENT = "引数が不正です";
    
    /** 出力ファイルのパス */
    protected Path filePath = null;
    
    /** 出力に使用するPrintWriter */
    protected PrintWriter writer = null;
    //----------------------------------------------------------------------------------------------
    /**
     * ディフォルトコンストラクタ
     */
    public TextReporter()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * <pre>出力ストリームをオープンする</pre>
     * @param path 出力ファイルのパス
     */
    public TextReporter(Path path) throws IOException
    {
        this();
        setFilePath(path);
        open();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ファイル変更レポートを出力する
     * @param modifyMap ファイル変更の結果Map
     */
    @Override
    public void report(Map<String, ModifyType> modifyMap) throws IOException
    {
        if(modifyMap == null)
        {
            throw new IllegalArgumentException(MSG_INVALID_ARGUMENT);
        }
        if(writer == null)
        {
            throw new IllegalStateException(MSG_FILEPATH_NOT_SPECIFIED);
        }
        for(Map.Entry<String, ModifyType> entry : modifyMap.entrySet())
        {
            writer.printf(RECORD_FORMAT, entry.getValue(), entry.getKey());
        }
        writer.flush();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出力ストリームをクローズする
     * @throws IOException クローズ失敗
     */
    @Override
    public void close()
    {
        if(writer != null)
        {
            writer.close();
            writer = null;
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出力ストリームをオープンする
     * @throws IOException オープン失敗
     */
    @Override
    public void open() throws IOException
    {
        Path outPath = getFilePath();
        if(outPath == null)
        {
            throw new IllegalStateException(MSG_FILEPATH_NOT_SPECIFIED);
        }
        writer = new PrintWriter(Files.newBufferedWriter(outPath, CHARSET));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出力するファイルのパスをセットする
     * @param arg 出力するファイルのパス
     */
    public void setFilePath(final Path arg)
    {
        filePath = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出力するファイルのパスを取得する
     * @return 出力するファイルのパス
     */
    public Path getFilePath()
    {
        return filePath;
    }
}
