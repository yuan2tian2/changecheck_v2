////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 認証実装クラス
 * @author はらだ　たかひこ
 */
public class AuthenticatorImpl extends Authenticator
{
    /** メールアドレス */
    protected String email;
    
    /** パスワード */
    protected String passwd;
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param email メールアドレス
     * @param passwd パスワード
     */
    public AuthenticatorImpl(String email, String passwd)
    {
        setEmail(email);
        setPassword(passwd);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * PasswordAuthenticationオブジェクトを取得する
     * @return PasswordAuthenticationオブジェクト
     */
    @Override
    protected PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(email, passwd);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 電子メールをセットする
     * @param arg 電子メールアドレス
     */
    public void setEmail(final String arg)
    {
        email = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * パスワードをセットする
     * @param arg パスワード
     */
    public void setPassword(final String arg)
    {
        passwd = arg;
    }
}
