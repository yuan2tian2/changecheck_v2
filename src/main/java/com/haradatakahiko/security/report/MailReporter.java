////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.ModifyType;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * メールレポーター
 * @author はらだ　たかひこ
 */
public class MailReporter implements IResultReporter
{
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReporter.class);
    
    /** 文字コード */
    public static final String ENCODING = "UTF-8";
    
    /** SMTPのプロパティ */
    protected Properties props = new Properties();
    
    /** 送信元メールアドレス */
    protected String from;
    
    /** 宛先 */
    protected String[] to = new String[0];
    
    /** 同報 */
    protected String[] cc = new String[0];
    
    /** パスワード */
    protected String password;
    
    /** 件名 */
    protected String subject;
    //----------------------------------------------------------------------------------------------
    /**
     * オープン
     * @throws IOException ありえない
     */
    @Override
    public void open() throws IOException
    {
        //no operation
    }
    //----------------------------------------------------------------------------------------------
    /**
     * レポートを出力する
     * @param modifyMap 変更チェック結果のMap
     * @throws IOException 送信エラー等
     */
    @Override
    public void report(Map<String, ModifyType> modifyMap) throws IOException
    {
        final String TRUE_STRING = "true";
        Session session = null;
        LOGGER.info(String.format("map件数：%d", modifyMap.size()));
        if(modifyMap.size() == 0)
        {
            return;
        }
        try
        {
            LOGGER.info(from);
            LOGGER.info(password);
            boolean smtpAuth = TRUE_STRING.equals(props.getProperty("mail.smtp.auth"));
            Authenticator auth = new AuthenticatorImpl(from, password);
            session = smtpAuth ? Session.getInstance(props, auth) : Session.getInstance(props);
            MimeMessage message = editMessage(session, modifyMap);
            sendMessage(message);
        }
        catch(AuthenticationFailedException e)
        {
            throw new RuntimeException("認証エラー", e);
        }
        catch(MessagingException e)
        {
            throw new IOException(e);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * メッセージをSMTPサーバへ送信する
     * @param message メールメッセージオブジェクト
     * @throws MessagingException 送信失敗
     */
    protected void sendMessage(MimeMessage message) throws MessagingException
    {
        Transport.send(message);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * メールのメッセージを編集する
     * @param session SMTPセッション
     * @param modifyMap 変更チェック結果のMap
     * @return メッセージ
     * @throws MessagingException SMTPのエラー
     * @throws UnsupportedEncodingException 対応していないエンコード
     */
    protected MimeMessage editMessage(Session session, Map<String, ModifyType> modifyMap) 
                                           throws MessagingException, UnsupportedEncodingException
    {
        final String RECORD_FORMAT = "[%s]\t%s\r\n";
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        addRecipients(message, Message.RecipientType.TO, to);
        addRecipients(message, Message.RecipientType.CC, cc);
        message.setSubject(subject);
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, ModifyType> entry : modifyMap.entrySet())
        {
            sb.append(String.format(RECORD_FORMAT, entry.getValue(), entry.getKey()));
        }
        message.setText(sb.toString(), ENCODING);
        return message;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * メッセージに受信者をセットする
     * @param message メッセージ
     * @param type 受信者のタイプ(TO, CC, BCC)
     * @param addresses アドレス文字列の配列
     * @throws MessagingException SMTPのエラー
     */
    protected void addRecipients(MimeMessage message, final Message.RecipientType type, 
                                               final  String[] addresses) throws MessagingException
    {
        for(String recipient : addresses)
        {
            if(recipient.equals(""))
            {
                continue;
            }
            LOGGER.info(String.format("[宛先]%s", recipient));
            message.addRecipient(type, new InternetAddress(recipient));
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * クローズする
     */
    @Override
    public void close()
    {
        //no operation
    }
    //---------------------------------------------------------------------------------------------
    /**
     * SMTPのプロパティを設定する
     * @param arg SMTPのプロパティ
     */
    public void setProperties(final Properties arg)
    {
        props = arg;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 送信者アドレスをセットする
     * @param arg 送信者アドレス
     */
    public void setFrom(final String arg)
    {
        from = arg;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * SMTP-AUTHのパスワードをセットする
     * @param arg パスワード
     */
    public void setPassword(final String arg)
    {
        password = arg;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 送信先のアドレス文字列の配列をセットする
     * @param arg アドレス文字列の配列
     */
    public void setTo(final String[] arg)
    {
        if(arg != null)
        {
            to = arg;
        }
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 同報先のアドレス文字列の配列をセットする
     * @param arg アドレス文字列の配列
     */
    public void setCc(final String[] arg)
    {
        if(arg != null)
        {
            cc = arg;
        }
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 件名をセットする
     * @param arg 件名
     */
    public void setSubject(final String arg)
    {
        subject = arg;
    }
}
