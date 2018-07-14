////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * MailReporterFactoryのテスト
 * @author はらだ　たかひこ
 */
public class MailReporterFactoryTest
{
    /** 件名 */
    public static final String SUBJECT = "[subject]test";
    
    /** テスト用メールアドレス */
    public static final String AUTHORS_EMAIL = "harada.takahiko.factory@gmail.com";
    //----------------------------------------------------------------------------------------------
    /**
     * MailReporterのインスタンスを作成できること
     */
    @Test
    public void createReporterTest() throws Exception
    {
        final String MESSAGE = "MailReporterのインスタンスを作成できること";
        MailReporterFactory factory = new MailReporterFactory();
        IResultReporter reporter = factory.createReporter(createParameter());
        Assert.assertTrue(MESSAGE, reporter instanceof MailReporter);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用のパラメータMapを構築する
     * @return テスト用のパラメータMap
     */
    public static Map<String, String> createParameter()
    {
        Map<String, String> map = new HashMap<>();
        map.put("from", AUTHORS_EMAIL);
        map.put("subject", "[subject]test");
        map.put("to", "to@gmail.com");
        map.put("cc", "cc@gmail.com," + AUTHORS_EMAIL);
        map.put("password", "**********");
        map.put("port", "25");
        map.put("smtp", "smtp.google.com");
        map.put("timeout", "30000");
        map.put("smtpauth", "false");
        return map;
    }
}
