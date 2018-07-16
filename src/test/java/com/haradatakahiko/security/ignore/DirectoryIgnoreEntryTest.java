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
 * DirectoryIgnoreEntryのテスト
 * @author はらだ　たかひこ
 */
public class DirectoryIgnoreEntryTest
{
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryIgnoreEntryTest.class);
    
    /** 無視対象のディレクトリ（テスト用） */
    private static final String TARGET_DIR = "target";
    
    //----------------------------------------------------------------------------------------------
    /**
     * 対象のディレクトリ名で始まるパスの場合shouldIgnore()がtrueとなること
     */
    @Test
    public void shoudIgnoreWithTargetAtStartTest() throws Exception
    {
        final String MESSAGE = "対象のディレクトリ名で始まるパスの場合shouldIgnore()がtrueとなること";
        DirectoryIgnoreEntry entry = new DirectoryIgnoreEntry(TARGET_DIR);
        Assert.assertTrue(MESSAGE, entry.shouldIgnore("target/log/xxx"));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 対象ディレクトリ名で終わるパスの場合shouldIgnore()がtrueとなること
     */
    @Test
    public void shoudIgnoreWithTargetAtEndTest() throws Exception
    {
        final String MESSAGE = "対象ディレクトリ名で終わるパスの場合shouldIgnore()がtrueとなること";
        DirectoryIgnoreEntry entry = new DirectoryIgnoreEntry(TARGET_DIR);
        Assert.assertTrue(MESSAGE, entry.shouldIgnore("/var/log/target"));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 対象ディレクトリ名がパスの途中にある場合shoudIgnore()がtrueを返すこと
     */
    @Test
    public void shoudIgnoreWithTargetInMiddstTest() throws Exception
    {
        final String MESSAGE = "対象ディレクトリ名がパス中にある場合shoudIgnore()がtrueを返すこと";
        DirectoryIgnoreEntry entry = new DirectoryIgnoreEntry(TARGET_DIR);
        Assert.assertTrue(MESSAGE, entry.shouldIgnore("/var/log/target/aaa/bbb"));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 対象ディレクトリ名がディレクトリ名の一部の場合shoudIgnore()がfalseを返すこと
     */
    @Test
    public void shoudIgnoreWithTargetPartialTest() throws Exception
    {
        final String MESSAGE = "対象ディレクトリ名がディレクトリ名の一部の場合、"
                                    + "shoudIgnore()がfalseを返すこと";
        DirectoryIgnoreEntry entry = new DirectoryIgnoreEntry(TARGET_DIR);
        Assert.assertFalse(MESSAGE, entry.shouldIgnore("/var/log/targetdirectory/aaa/bbb"));
    }
}
