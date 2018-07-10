////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.util.XmlDocument;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * テキストレポーターのファクトリ
 * @author はらだ　たかひこ
 */
public class TextReporterFactory implements IReporterFactory
{
    /**
     * テキストレポーターを構築する
     * @param params 設定XMLドキュメント
     * @throws IOException ファイルのオープンエラー
     */
    @Override
    public IResultReporter createReporter(Map<String, String> params) throws IOException
    {
        String pathString = params.get("file");
        return new TextReporter(Paths.get(pathString));
    }
}
