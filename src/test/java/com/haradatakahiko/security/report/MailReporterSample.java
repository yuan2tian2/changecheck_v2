////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.ModifyType;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * メールレポーターのテスト用実装クラス
 * @author はらだ　たかひこ
 */
public class MailReporterSample extends MailReporter
{
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReporterSample.class);
    //----------------------------------------------------------------------------------------------
    /**
     * メッセージを送信する
     * @param message 送信するメッセージ
     * @throws MessagingException 送信失敗
     */
    @Override
    public void sendMessage(MimeMessage message) throws MessagingException
    {
        LOGGER.debug(message.toString());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * スーパークラスのeditMessage()を呼び出す
     * @param session SMTPセッション
     * @param modifyMap 変更チェック結果のMap
     * @return メッセージ
     * @throws MessagingException SMTPのエラー
     * @throws UnsupportedEncodingException 対応していないエンコード
     */
    public MimeMessage editMessage(Session session, Map<String, ModifyType> modifyMap)
                                throws MessagingException, UnsupportedEncodingException
    {
        return super.editMessage(session, modifyMap);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * メッセージに受信者をセットする
     * @param message メッセージ
     * @param type 受信者のタイプ(TO, CC, BCC)
     * @param addresses アドレス文字列の配列
     * @throws MessagingException SMTPのエラー
     */
    public void addRecipients(MimeMessage message, final Message.RecipientType type, 
                                               final  String[] addresses) throws MessagingException
    {
        super.addRecipients(message, type, addresses);
    }
}
