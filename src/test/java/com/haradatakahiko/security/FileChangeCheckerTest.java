////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.ignore.AbstractIgnoreEntry;
import com.haradatakahiko.security.ignore.FileExtensionIgnoreEntry;
import com.haradatakahiko.security.ignore.IgnoreType;
import com.haradatakahiko.security.util.XmlDocument;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * FileChangeCheckerのテスト
 * @author はらだ　たかひこ
 */
public class FileChangeCheckerTest
{
    /** テスト用設定XMLファイルのパス */
    public static final String TEST_CONFIG = "example.config.xml";
    
    /** 無視する拡張子配列 */
    public static final AbstractIgnoreEntry[] EXPECTS;
    //----------------------------------------------------------------------------------------------
    /**
     * static initializer
     */
    static
    {
        FileExtensionIgnoreEntry log = new FileExtensionIgnoreEntry("log", IgnoreType.EXTENSION);
        FileExtensionIgnoreEntry cls = new FileExtensionIgnoreEntry("class", IgnoreType.EXTENSION);
        EXPECTS = new AbstractIgnoreEntry[]{log, cls};
    }
    //----------------------------------------------------------------------------------------------
    /**
     * getInstance()で唯一のインスタンスを取得できること
     */
    @Test
    public void getInstanceTest() throws Exception
    {
        final String MESSAGE = "getInstance()で唯一のインスタンスを取得できること";
        Object obj = FileChangeChecker.getInstance();
        Assert.assertTrue(MESSAGE, obj instanceof FileChangeChecker);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 設定ファイルをロードできること
     */
    @Test
    public void getConfigWithValidTest() throws Exception
    {
        final String MESSAGE = "設定ファイルをロードできること";
        FileChangeChecker checker = FileChangeChecker.getInstance();
        Object obj = checker.getConfig(TEST_CONFIG);
        Assert.assertTrue(obj instanceof XmlDocument);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 不正なパスの設定ファイルをロードしてIOExceptionをスローすること
     */
    @Test(expected = IOException.class)
    public void getConfigWithInvalidTest() throws Exception
    {
        final String MESSAGE = "不正なパスの設定ファイルをロードしてIOExceptionをスローすること";
        FileChangeChecker checker = FileChangeChecker.getInstance();
        Object obj = checker.getConfig("not-exist-file.dat");
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 設定ファイルから設定値を読み込めること
     */
    @Test
    public void getConfigValueWithNormalTest() throws Exception
    {
        final String MESSAGE = "設定ファイルから設定値を読み込めること";
        FileChangeChecker checker = FileChangeChecker.getInstance();
        checker.setConfigXml(checker.getConfig(TEST_CONFIG));
        final String EXPECT = "changecheck";
        String result = checker.getConfigValue("/config/targets/target");
        Assert.assertEquals(MESSAGE, EXPECT, result);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 設定読込前にgetConfigValue()を呼び出すとIllegalStateExceptionがスローされること
     */
    @Test(expected = IllegalStateException.class)
    public void getConfigValueWithoutLoadTest() throws Exception
    {
        final String MESSAGE = "設定読込前にgetConfigValue()を呼び出すと例外がスローされること";
        FileChangeChecker checker = FileChangeChecker.getInstance();
        checker.setConfigXml(null);
        checker.getConfigValue("/config/targets/target");
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 無視する拡張子のセットの大きさが2であること
     */
    @Test
    public void getIgnoreSetSizeTest() throws Exception
    {
        final String MESSAGE = "無視する拡張子のセットの大きさが正しいこと";
        FileChangeChecker checker = FileChangeChecker.getInstance();
        checker.setConfigXml(checker.getConfig(TEST_CONFIG));
        Set<AbstractIgnoreEntry> ignoreSet = checker.getIgnoreSet(checker.getConfigXml());
        Assert.assertEquals(MESSAGE, EXPECTS.length, ignoreSet.size());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 取得した無視する拡張子のセットの内容が正しいこと
     */
    @Test
    public void getIgnoreSetContentsTest() throws Exception
    {
        final String MESSAGE = "取得した無視する拡張子のセットの内容が正しいこと";
        FileChangeChecker checker = FileChangeChecker.getInstance();
        checker.setConfigXml(checker.getConfig(TEST_CONFIG));
        Set<AbstractIgnoreEntry> ignoreSet = checker.getIgnoreSet(checker.getConfigXml());
        for(AbstractIgnoreEntry expect : EXPECTS)
        {
            Assert.assertTrue(MESSAGE, ignoreSet.contains(expect));
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * checkHash(null)がnullを返すこと
     */
    @Test
    public void checkHashWithNullTest() throws Exception
    {
        final String MESSAGE = "checkHash(null)がnullを返すこと";
        FileChangeChecker checker = FileChangeChecker.getInstance();
        Object result = invokeConfigure(checker, TEST_CONFIG);
        String actual = checker.checkHash(null);
        Assert.assertEquals(MESSAGE, null, actual);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ハッシュ値(SHA-256)を正しく計算できること
     */
    @Test
    public void checkHashWithNormalTest() throws Exception
    {
        final String MESSAGE = "ハッシュ値(SHA-512)を正しく計算できること";
        final String EXPECT = "b93d21685ca6e79039febb1f8bb267c81ee2f3937360f7a1f9f3f39ea9eca11fd3c"
                            + "57ab446ec723345844a21e0f21418a5b5437ee163f6e5c017504900c3e177";
        FileChangeChecker checker = FileChangeChecker.getInstance();
        Object result = invokeConfigure(checker, TEST_CONFIG);
        String actual = checker.checkHash(Paths.get(TEST_CONFIG));
        Assert.assertEquals(MESSAGE, EXPECT, actual);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * configure()メソッドをreflection実行する
     * @param instance FileChangeCheckerのインスタンス
     * @param configPath configure()メソッドの引数
     */
    private Object invokeConfigure(FileChangeChecker instance, String configPath)
    {
        Object result = null;
        try
        {
            Class<? extends FileChangeChecker> cls = instance.getClass();
            Method method = cls.getDeclaredMethod("configure", String.class);
            if(method != null)
            {
                method.setAccessible(true);
                result = method.invoke(instance, configPath);
            }
            else
            {
                throw new ReflectiveOperationException("メソッドがみつかりません");
            }
        }
        catch(ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
        return result;
    }
}