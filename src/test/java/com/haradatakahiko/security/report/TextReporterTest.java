////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.ModifyType;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * TextReporterのテスト
 * @authro はらだ　たかひこ
 */
public class TextReporterTest
{
    /** テスト用術力ファイルのファイル名 */
    public static final String TEST_REPORT = "./work/test.log";
    //----------------------------------------------------------------------------------------------
    /**
     * 後始末
     */
    @After
    public void tearDown() throws Exception
    {
        Files.deleteIfExists(Paths.get(TEST_REPORT));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * report()にnullを渡すとIllegalArgumentExceptionがスローされること
     */
    @Test(expected = IllegalArgumentException.class)
    public void reportWithNullArgumentTest() throws Exception
    {
        final String MESSAGE = "report()にnullを渡すとIllegalArgumentExceptionがスローされること";
        TextReporter reporter = new TextReporter();
        reporter.report(null);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * openせずにreport()を呼び出した場合に例外がスローされること
     */
    @Test(expected = IllegalStateException.class)
    public void reportWithoutOpenTest() throws Exception
    {
        final String MESSAGE = "openせずにreport()を呼び出した場合に例外がスローされること";
        TextReporter reporter = new TextReporter();
        Map<String, ModifyType> map = createDummyMap();
        reporter.report(map);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出力ファイルをオープンできること
     */
    @Test
    public void openTest() throws Exception
    {
        final String MESSAGE = "出力ファイルをオープンできること";
        Path path = Paths.get(TEST_REPORT);
        Map<String, ModifyType> map = createDummyMap();
        TextReporter reporter = new TextReporter(path);
        reporter.close();
        Assert.assertTrue(MESSAGE, Files.exists(path));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * レポート出力件数が等しいこと
     */
    @Test
    public void reportCountTest() throws Exception
    {
        final String MESSAGE = "レポート出力件数が等しいこと";
        Path path = Paths.get(TEST_REPORT);
        Map<String, ModifyType> map = createDummyMap();
        try(TextReporter reporter = new TextReporter(path))
        {
            reporter.report(map);
        }
        List<String> list = Files.readAllLines(path, StandardCharsets.UTF_8);
        Assert.assertEquals(MESSAGE, list.size(), map.size());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出力されたレポートの内容が正しいこと
     */
    @Test
    public void reportContentTest() throws Exception
    {
        final String MESSAGE = "出力されたレポートの内容が正しいこと";
        Path path = Paths.get(TEST_REPORT);
        Map<String, ModifyType> map = createDummyMap();
        try(TextReporter reporter = new TextReporter(path))
        {
            reporter.report(map);
        }
        List<String> list = Files.readAllLines(path, StandardCharsets.UTF_8);
        for(String record : list)
        {
            String[] data = record.split("\t");
            String filePath = data[1];
            String modifyType = data[0];
            ModifyType type = map.get(filePath);
            String expect = String.format("[%s]", type);
            Assert.assertEquals(MESSAGE, expect, modifyType);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用のパラメータMapを構築する
     * @return テスト用のパラメータMap
     */
    public static Map<String, ModifyType> createDummyMap()
    {
        Map<String, ModifyType> map = new HashMap<>();
        map.put("/var/log/modify", ModifyType.MODIFY);
        map.put("/var/log/add1", ModifyType.ADD);
        map.put("/var/log/add2", ModifyType.ADD);
        map.put("/var/log/delete1", ModifyType.DELETE);
        map.put("/var/log/delete2", ModifyType.DELETE);
        map.put("/var/log/delete3", ModifyType.DELETE);
        return map;
    }
}
