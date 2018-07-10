////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.IOException;
import java.util.Map;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * レポーターファクトリのインタフェース
 * @author はらだ　たかひこ
 */
public interface IReporterFactory
{
    /**
     * レポーターを構築する
     * @param params 設定XMLドキュメント
     * @throws IOException ファイルのオープンエラー
     */
    IResultReporter createReporter(Map<String, String> params) throws IOException;
    //----------------------------------------------------------------------------------------------
    /**
     * レポータのファクトリのインスタンスを返す
     * @param type レポート種別
     * @return 種別に応じたファクトリ
     */
    public static IReporterFactory getInstance(ReportType type)
    {
        if(type == null)
        {
            throw new IllegalArgumentException();
        }
        IReporterFactory factoryImpl = null;
        switch(type)
        {
            case TEXT :
                factoryImpl = new TextReporterFactory();
                break;
            case NULL :
                factoryImpl = new NullReporterFactory();
                break;
            case MAIL :
                factoryImpl = new MailReporterFactory();
                break;
            default :
                String message = String.format("%s is not implemented", type);
                throw new UnsupportedOperationException(message);
        }
        return factoryImpl;
    }
}
