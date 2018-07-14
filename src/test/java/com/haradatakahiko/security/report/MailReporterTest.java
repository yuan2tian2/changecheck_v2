////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.ModifyType;

import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;

import org.junit.Assert;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * メールレポーターのテスト
 * @author はらだ　たかひこ
 */
public class MailReporterTest
{
    /**
     * report()メソッドで例外が発生しないこと
     */
    @Test
    public void reportTest() throws Exception
    {
        final String MESSAGE = "report()メソッドで例外が発生しないこと";
        Map<String, String> params = MailReporterFactoryTest.createParameter();
        IResultReporter reporter = createReporter(params);
        Map<String, ModifyType> map = TextReporterTest.createDummyMap();
        reporter.report(map);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * メールの送信者アドレスが設定されていること
     */
    @Test
    public void editMessageFromTest() throws Exception
    {
        final String MESSAGE = "メールの送信者アドレスが設定されていること";
        Map<String, String> params = MailReporterFactoryTest.createParameter();
        MailReporter reporter = createReporter(params);
        Map<String, ModifyType> map = TextReporterTest.createDummyMap();
        Properties props = createProperties(params);
        Session session = Session.getInstance(props);
        MimeMessage message = reporter.editMessage(session, map);
        final Address EXPECT = new InternetAddress(MailReporterFactoryTest.AUTHORS_EMAIL);
        Address[] actuals = message.getFrom();
        Assert.assertEquals(MESSAGE, 1, actuals.length);
        Assert.assertEquals(MESSAGE, EXPECT, actuals[0]);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * メールの宛先アドレスが設定されていること
     */
    @Test
    public void editMessageToTest() throws Exception
    {
        final String MESSAGE = "メールの宛先アドレスが設定されていること";
        Map<String, String> params = MailReporterFactoryTest.createParameter();
        MailReporter reporter = createReporter(params);
        Map<String, ModifyType> map = TextReporterTest.createDummyMap();
        Properties props = createProperties(params);
        Session session = Session.getInstance(props);
        MimeMessage message = reporter.editMessage(session, map);
        final Address EXPECT = new InternetAddress("to@gmail.com");
        Address[] actuals = message.getRecipients(Message.RecipientType.TO);
        Assert.assertEquals(MESSAGE, 1, actuals.length);
        Assert.assertEquals(MESSAGE, EXPECT, actuals[0]);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * メールの同報先アドレスが設定されていること
     */
    @Test
    public void editMessageCcTest() throws Exception
    {
        final String MESSAGE = "メールの同報先アドレスが設定されていること";
        Map<String, String> params = MailReporterFactoryTest.createParameter();
        MailReporter reporter = createReporter(params);
        Map<String, ModifyType> map = TextReporterTest.createDummyMap();
        Properties props = createProperties(params);
        Session session = Session.getInstance(props);
        MimeMessage message = reporter.editMessage(session, map);
        Address expect1 = new InternetAddress("cc@gmail.com");
        Address expect2 = new InternetAddress(MailReporterFactoryTest.AUTHORS_EMAIL);
        final Address[] EXPECTS = new Address[]{expect1, expect2};
        Address[] actuals = message.getRecipients(Message.RecipientType.CC);
        Assert.assertEquals(MESSAGE, EXPECTS.length, actuals.length);
        for(int i = 0; i < EXPECTS.length; i++)
        {
            Assert.assertEquals(MESSAGE, EXPECTS[i], actuals[i]);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 件名が設定されていること
     */
    @Test
    public void editMessageSubjectTest() throws Exception
    {
        final String MESSAGE = "件名が設定されていること";
        Map<String, String> params = MailReporterFactoryTest.createParameter();
        MailReporter reporter = createReporter(params);
        Map<String, ModifyType> map = TextReporterTest.createDummyMap();
        Properties props = createProperties(params);
        Session session = Session.getInstance(props);
        MimeMessage message = reporter.editMessage(session, map);
        Assert.assertEquals(MESSAGE, MailReporterFactoryTest.SUBJECT, message.getSubject());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用メールレポータを構築する
     * @param パラメータのMap
     * @return テスト用メールレポータ
     */
    private MailReporter createReporter(Map<String, String> params)
    {
        MailReporterSample reporter = new MailReporterSample();
        reporter.setFrom(params.get("from"));
        reporter.setSubject(params.get("subject"));
        reporter.setPassword(params.get("password"));
        String toString = params.get("to");
        reporter.setTo((toString != null) ? toString.split(",") : new String[0]);
        String ccString = params.get("cc");
        reporter.setCc((ccString != null) ? ccString.split(",") : new String[0]);
        reporter.setProperties(createProperties(params));
        return reporter;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * Javamailのプロパティを構築する
     * @param params パラメータMap
     * @return Javamailのプロパティ
     */
    private Properties createProperties(Map<String, String> params)
    {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", params.get("smtp"));
        props.setProperty("mail.smtp.port", params.get("port"));
        props.setProperty("mail.smtp.connectiontimeout", params.get("timeout"));
        props.setProperty("mail.smtp.timeout", params.get("timeout"));
        return props;
    }
}
