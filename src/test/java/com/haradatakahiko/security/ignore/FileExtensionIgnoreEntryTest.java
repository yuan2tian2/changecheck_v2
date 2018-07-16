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
 * FileExtensionIgnoreEntryのテスト
 * @author はらだ　たかひこ
 */
public class FileExtensionIgnoreEntryTest
{
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileExtensionIgnoreEntryTest.class);
    
    /** 無視対象の拡張子（テスト用） */
    private static final String TARGET_EXTENSION = "log";
    //----------------------------------------------------------------------------------------------
    /**
     * 無視対象の拡張子の場合shouldIgnore()がtrueを返すこと
     */
    @Test
    public void shoudIgnoreWithTargetTest() throws Exception
    {
        final String MESSAGE ="無視対象の拡張子の場合shouldIgnore()がtrueを返すこと";
        FileExtensionIgnoreEntry entry = new FileExtensionIgnoreEntry(TARGET_EXTENSION);
        LOGGER.debug(entry.toString());
        Assert.assertTrue(MESSAGE, entry.shouldIgnore("/var/test.log"));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 無視対象ではない拡張子の場合はshouldIgnore()がfalseを返すこと
     */
    @Test
    public void shoudIgnoreWithoutTargetTest() throws Exception
    {
        final String MESSAGE = "無視対象ではない拡張子の場合はshouldIgnore()がfalseを返すこと";
        FileExtensionIgnoreEntry entry = new FileExtensionIgnoreEntry(TARGET_EXTENSION);
        Assert.assertFalse(MESSAGE, entry.shouldIgnore("/var/test.txt"));
    }
}
