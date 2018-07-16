////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.ignore;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.Assert;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * AbstractIgnoreEntryのテストコード
 * @author はらだ　たかひこ
 */
public class AbstractIgnoreEntryTest
{
    /**
     * 適切に文字列化できること
     */
    @Test
    public void toStringTest() throws Exception
    {
        final String MESSAGE = "適切に文字列化できること";
        AbstractIgnoreEntry entry = new DirectoryIgnoreEntry("abc");
        final String EXPECT = "IgnoreEntry [ディレクトリ] abc";
        Assert.assertEquals(MESSAGE, EXPECT, entry.toString());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * equals(null)がfalseを返すこと
     */
    @Test
    public void equalsWithNullTest() throws Exception
    {
        final String MESSAGE = "equals(null)がfalseを返すこと";
        AbstractIgnoreEntry entry = new DirectoryIgnoreEntry("abc");
        Assert.assertFalse(entry.equals(null));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * equals()に自身を渡すとtrueを返すこと
     */
    @Test
    public void equalsWithSelfTest() throws Exception
    {
        final String MESSAGE = "equals()に自身を渡すとtrueを返すこと";
        AbstractIgnoreEntry entry = new DirectoryIgnoreEntry("abc");
        Assert.assertTrue(entry.equals(entry));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * equals()に同じ値の異なるインスタンスを渡すとtrueを返すこと
     */
    @Test
    public void equalsWithEquavalentTest() throws Exception
    {
        final String MESSAGE = "equals()に同じ値の異なるインスタンスを渡すとtrueを返すこと";
        AbstractIgnoreEntry entry1 = new DirectoryIgnoreEntry("abc");
        AbstractIgnoreEntry entry2 = new DirectoryIgnoreEntry("abc");
        Assert.assertTrue(entry1.equals(entry2));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * equals()に異なる値の異なるインスタンスを渡すとfalseを返すこと
     */
    @Test
    public void equalsWithDifferenceTest() throws Exception
    {
        final String MESSAGE = "equals()に異なる値の異なるインスタンスを渡すとfalseを返すこと";
        AbstractIgnoreEntry entry1 = new DirectoryIgnoreEntry("abc");
        AbstractIgnoreEntry entry2 = new DirectoryIgnoreEntry("def");
        Assert.assertFalse(MESSAGE, entry1.equals(entry2));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * equals()に異なる値の異なる無視タイプのインスタンスを渡すとfalseを返すこと
     */
    @Test
    public void equalsWithDifferentTypeTest() throws Exception
    {
        final String MESSAGE = "equals()に異なる値の異なる無視タイプのインスタンスを"
                                                                    + "渡すとfalseを返すこと";
        AbstractIgnoreEntry entry1 = new DirectoryIgnoreEntry("abc");
        AbstractIgnoreEntry entry2 = new FileExtensionIgnoreEntry("abc");
        Assert.assertFalse(MESSAGE, entry1.equals(entry2));
    }
}
