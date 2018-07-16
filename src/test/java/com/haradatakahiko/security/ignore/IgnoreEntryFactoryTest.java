////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.ignore;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.Assert;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * IgnoreEntryFactoryのテスト
 * @author はらだ　たかひこ
 */
public class IgnoreEntryFactoryTest
{
    /**
     * getInstance()で同一のインスタンスが取得できること
     */
    @Test
    public void getInstanceTest() throws Exception
    {
        final String MESSAGE = "getInstance()で同一のインスタンスが取得できること";
        IgnoreEntryFactory factory1 = IgnoreEntryFactory.getInstance();
        IgnoreEntryFactory factory2 = IgnoreEntryFactory.getInstance();
        Assert.assertTrue(MESSAGE, factory1 == factory2);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * getIgnoreEntry(null, null)で例外がスローされること
     */
    @Test(expected = IllegalArgumentException.class)
    public void getIgnoreEntryWithNullTest() throws Exception
    {
        final String MESSAGE = "getIgnoreEntry(null, null)で例外がスローされること";
        IgnoreEntryFactory factory = IgnoreEntryFactory.getInstance();
        factory.getIgnoreEntry(null, null);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ディレクトリを指定した場合にDirectoryIgnoreEntityのインスタンスが返されること
     */
    @Test
    public void getIgnoreEntryWithDirectoryTest() throws Exception
    {
        final String MESSAGE = "ディレクトリを指定した場合にDirectoryIgnoreEntityが返されること";
        IgnoreEntryFactory factory = IgnoreEntryFactory.getInstance();
        Object obj = factory.getIgnoreEntry("abc", "directory");
        Assert.assertTrue(MESSAGE, obj instanceof DirectoryIgnoreEntry);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 拡張子を指定した場合にFileExtensionIgnoreEntryのインスタンスが返されること
     */
    @Test
    public void getIgnoreEntryWithFileExtensionTest() throws Exception
    {
        final String MESSAGE = "拡張子を指定した場合にFileExtensionIgnoreEntryが返されること";
        IgnoreEntryFactory factory = IgnoreEntryFactory.getInstance();
        Object obj = factory.getIgnoreEntry("abc", "extension");
        Assert.assertTrue(MESSAGE, obj instanceof FileExtensionIgnoreEntry);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 正規表現を指定した場合に未実装例外がスローされること
     */
    @Test(expected = IllegalArgumentException.class)
    public void getIgnoreEntryWithRegularExpressionTest() throws Exception
    {
        final String MESSAGE = "正規表現を指定した場合に未実装例外がスローされること";
        IgnoreEntryFactory factory = IgnoreEntryFactory.getInstance();
        Object obj = factory.getIgnoreEntry("abc", "REGULAR_EXPRESSION");
    }
}
