////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.report.IResultReporter;
import com.haradatakahiko.security.report.ReportType;
import com.haradatakahiko.security.report.IReporterFactory;
import com.haradatakahiko.security.report.TextReporterFactory;
import com.haradatakahiko.security.util.IterableNodeList;
import com.haradatakahiko.security.util.XmlDocument;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ファイルの変更をチェックするメインプログラム
 * @author はらだ　たかひこ
 */
public final class App
{
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    
    /** ディフォルトの設定ファイル名 */
    private static final String DEFAULT_CONFIG = "config.xml";
    //----------------------------------------------------------------------------------------------
    /**
     * エントリーポイント
     * @param args コマンドライン引数（オプション）
     * <pre>0:設定ファイルのパス</pre>
     */
    public static void main(final String[] args)
    {
        App instance = new App();
        try
        {
            LOGGER.info("リソース変更チェック START");
            instance.execute(args);
            LOGGER.info("リソース変更チェック END");
        }
        catch(RuntimeException e)
        {
            LOGGER.error("予期せぬエラーが発生しました。", e);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 処理実行インスタンスメソッド
     * @param args コマンドラインパラメータ
     */
    private void execute(final String[] args)
    {
        try
        {
            FileChangeChecker app = FileChangeChecker.getInstance();
            String configPath = (args.length > 0) ? args[0] : DEFAULT_CONFIG;
            Map<String, ModifyType> result = app.execute(configPath);
            XmlDocument xml = app.getConfigXml();
            try(IResultReporter reporter = createReporter(xml))
            {
                reporter.report(result);
            }
            Map<ModifyType, Integer> summaryMap = summarizeResult(result);
            writeSummary(summaryMap);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 集計結果出力のインスタンスを作成する
     * @param xml 設定XMLドキュメント
     * @return 集計結果出力のインスタンス
     * @throws IOException 出力エラー
     */
    private IResultReporter createReporter(XmlDocument xml) throws IOException
    {
        IResultReporter reporter = null;
        try
        {
            Map<String, String> params = getReportParams(xml);
            ReportType type = ReportType.valueOf(params.get("type"));
            LOGGER.info(String.format("出力レポート形式：%s", type));
            IReporterFactory factory = IReporterFactory.getInstance(type);
            reporter = factory.createReporter(params);
        }
        catch(XPathExpressionException e)
        {
            throw new RuntimeException(e);
        }
        return reporter;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 変更チェック結果レポート出力用パラメータを取得する
     * @param xml 設定XMLドキュメント
     * @return 変更チェック結果レポート出力用パラメータのMap
     * @throws XPathExpressionException XPath式の構文エラー
     */
    private Map<String, String> getReportParams(XmlDocument xml) throws XPathExpressionException
    {
        Map<String, String> params = new HashMap<>();
        Node parentNode = xml.getSingleNode("/config/report");
        IterableNodeList children = new IterableNodeList(parentNode.getChildNodes());
        for(Node node : children)
        {
            if(node.getNodeType() == Node.ELEMENT_NODE)
            {
                params.put(node.getNodeName(), node.getTextContent());
            }
        }
        return params;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ファイル変更チェックの結果をレポートする
     * @param modifyMap ファイル変更チェック結果のMap
     * @return 集計結果Map
     */
    protected Map<ModifyType, Integer> summarizeResult(Map<String, ModifyType> modifyMap)
    {
        boolean isModified = false;
        Map<ModifyType, Integer> countMap = new HashMap<>();
        Iterator<Map.Entry<String, ModifyType>> iterator = modifyMap.entrySet().iterator();
        for(Map.Entry<String, ModifyType> entry : modifyMap.entrySet())
        {
            ModifyType modify = entry.getValue();
            if(modify == ModifyType.SAME || modify == ModifyType.IGNORE)
            {
                continue;
            }
            isModified = true;
            int count = countMap.getOrDefault(modify, 0);
            countMap.put(modify, ++count);
        }
        if(isModified)
        {
            LOGGER.warn("ファイルの変更が検知されました。");
        }
        return countMap;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 集計結果をログファイルに出力する
     * @param summaryMap 集計結果のMap
     */
    protected void writeSummary(Map<ModifyType, Integer> summaryMap)
    {
        for(Map.Entry<ModifyType, Integer> entry : summaryMap.entrySet())
        {
            LOGGER.info(String.format("[%s] : %d件", entry.getKey(), entry.getValue()));
        }
    }
}
