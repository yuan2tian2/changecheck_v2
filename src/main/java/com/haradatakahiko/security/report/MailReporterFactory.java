////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.util.XmlDocument;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import javax.xml.xpath.XPathExpressionException;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * メールレポーターのファクトリ
 * @author はらだ　たかひこ
 */
public class MailReporterFactory implements IReporterFactory
{
    /**
     * メールレポーターを構築する
     * @param params 設定XMLドキュメント
     * @throws IOException ファイルのオープンエラー
     */
    @Override
    public IResultReporter createReporter(Map<String, String> params) throws IOException
    {
        MailReporter reporter = new MailReporter();
        if(params != null)
        {
            reporter.setProperties(createProperties(params));
            reporter.setFrom(params.get("from"));
            reporter.setPassword(params.get("password"));
            reporter.setSubject(params.get("subject"));
            String toString = params.get("to");
            reporter.setTo((toString != null) ? toString.split(",") : new String[0]);
            String ccString = params.get("cc");
            reporter.setCc((ccString != null) ? ccString.split(",") : new String[0]);
        }
        return reporter;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * SMTPのプロパティを構築する
     * @param params パラメータのMap
     * @return SMTPのプロパティ
     */
    protected Properties createProperties(Map<String, String> params)
    {
        final String SSL_PORT = "465";
        if(params == null)
        {
            return null;
        }
        boolean isSsl = SSL_PORT.equals(params.get("port"));
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", params.get("smtp"));
        props.setProperty("mail.smtp.port", params.get("port"));
        props.setProperty("mail.smtp.connectiontimeout", params.get("timeout"));
        props.setProperty("mail.smtp.timeout", params.get("timeout"));
        props.setProperty("mail.smtp.auth", params.get("smtpauth"));
        if(isSsl)
        {
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.socketFactory.port", SSL_PORT);
        }
        return props;
    }
}
