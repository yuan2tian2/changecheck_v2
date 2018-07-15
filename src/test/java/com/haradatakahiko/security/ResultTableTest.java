////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ResultTableクラスのテスト
 * @author はらだ　たかひこ
 */
public class ResultTableTest
{
    /** テストで使用する結果ファイル */
    public static final String DATA_FILE = "work/testresult%d.ser";
    
    /** テスト用に無視する拡張子Set */
    public static final Set<String> IGNORE_SET = Set.of("log", "pid");
    
    /** テスト用結果ファイルの番号 */
    public static int count = 0;
    //----------------------------------------------------------------------------------------------
    /**
     * 初期状態でカウントが0であること
     */
    @Test
    public void countOnInitializeTest() throws Exception
    {
        final String MESSAGE = "初期状態でカウントが0であること";
        ResultTable<String, String> table = new ResultTable<>();
        Assert.assertEquals(MESSAGE, 0, table.getCount());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 初期状態で変更チェックをすると「追加」と判定されること
     */
    @Test
    public void detectModifyOnInitialTest() throws Exception
    {
        final String MESSAGE = "初期状態で変更チェックをすると「追加」と判定されること";
        ResultTable<String, String> table = new ResultTable<>();
        SimpleHashMaker hashMaker = new SimpleHashMaker(SimpleHashMakerTest.ALGORITHM);
        Path path = Paths.get(SimpleHashMakerTest.EXAMPLE_FILE1);
        String hash = IHashMaker.toHexString(hashMaker.hash(path));
        ModifyType actual = table.detectModify(SimpleHashMakerTest.EXAMPLE_FILE1, hash);
        Assert.assertEquals(MESSAGE, ModifyType.ADD, actual);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 同一ファイルを再読込した場合「同一」と判定されること
     */
    @Test
    public void detectModifyWithSameHashTest() throws Exception
    {
        final String MESSAGE = "同一ファイルを再読込した場合「同一」と判定されること";
        ResultTable<String, String> table = new ResultTable<>();
        SimpleHashMaker hashMaker = new SimpleHashMaker(SimpleHashMakerTest.ALGORITHM);
        Path path = Paths.get(SimpleHashMakerTest.EXAMPLE_FILE2);
        String hash = IHashMaker.toHexString(hashMaker.hash(path));
        table.addItem(SimpleHashMakerTest.EXAMPLE_FILE2, hash);
        ModifyType actual = table.detectModify(SimpleHashMakerTest.EXAMPLE_FILE2, hash);
        Assert.assertEquals(MESSAGE, ModifyType.SAME, actual);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ハッシュ値を変えると「変更」と判定されること
     */
    @Test
    public void detectModifyWithDifferenceTest() throws Exception
    {
        final String MESSAGE = "ハッシュ値を変えると「変更」と判定されること";
        ResultTable<String, String> table = new ResultTable<>();
        table.addItem(SimpleHashMakerTest.EXAMPLE_FILE1, "DUMMY-HASH-VALUE");
        SimpleHashMaker hashMaker = new SimpleHashMaker(SimpleHashMakerTest.ALGORITHM);
        Path path = Paths.get(SimpleHashMakerTest.EXAMPLE_FILE1);
        String hash = IHashMaker.toHexString(hashMaker.hash(path));
        ModifyType actual = table.detectModify(SimpleHashMakerTest.EXAMPLE_FILE1, hash);
        Assert.assertEquals(MESSAGE, ModifyType.MODIFY, actual);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 無視する対象拡張子でない場合にshouldIgnore()がfalseを返すこと
     */
    @Test
    public void shoudIgnoreWithNotTargetTest() throws Exception
    {
        final String MESSAGE = "無視する対象拡張子でない場合にshouldIgnore()がfalseを返すこと";
        ResultTable<String, String> table = new ResultTable<>();
        table.setIgnoreExtensionSet(IGNORE_SET);
        Assert.assertFalse(MESSAGE, table.shoudIgnore("hello.java"));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 無視する対象拡張子の場合、shouldIgnore()がtrueを返すこと
     */
    @Test
    public void shoudIgnoreWithTargetTest() throws Exception
    {
        final String MESSAGE = "無視する対象拡張子の場合、shouldIgnore()がtrueを返すこと";
        ResultTable<String, String> table = new ResultTable<>();
        table.setIgnoreExtensionSet(IGNORE_SET);
        Assert.assertTrue(MESSAGE, table.shoudIgnore("hello.log"));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * shouldIgnore(null)がfalseを返すこと
     */
    @Test
    public void shoudIgnoreWithNullTest() throws Exception
    {
        final String MESSAGE = "shouldIgnore(null)がfalseを返すこと";
        ResultTable<String, String> table = new ResultTable<>();
        table.setIgnoreExtensionSet(IGNORE_SET);
        Assert.assertFalse(MESSAGE, table.shoudIgnore(null));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 検査結果をエクスポートできること
     */
    @Test
    public void exportResultTest() throws Exception
    {
        final String MESSAGE = "検査結果をエクスポートできること";
        ResultTable<String, String> table = new ResultTable<>();
        String dataFile = String.format(DATA_FILE, count++);
        table.setDataFile(dataFile);
        SimpleHashMaker hashMaker = new SimpleHashMaker(SimpleHashMakerTest.ALGORITHM);
        Path path = Paths.get(SimpleHashMakerTest.EXAMPLE_FILE1);
        String hash = IHashMaker.toHexString(hashMaker.hash(path));
        ModifyType actual = table.detectModify(SimpleHashMakerTest.EXAMPLE_FILE1, hash);
        table.exportResult();
        Assert.assertTrue(MESSAGE, Files.exists(Paths.get(dataFile)));
        Files.deleteIfExists(Paths.get(dataFile));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * エクスポートした検査結果をインポートできること
     */
    @Test
    public void importResultTest() throws Exception
    {
        final String MESSAGE = "エクスポートした検査結果をインポートできること";
        ResultTable<String, String> table = new ResultTable<>();
        String dataFile = String.format(DATA_FILE, count++);
        table.setDataFile(dataFile);
        SimpleHashMaker hashMaker = new SimpleHashMaker(SimpleHashMakerTest.ALGORITHM);
        Path path = Paths.get(SimpleHashMakerTest.EXAMPLE_FILE1);
        String hash = IHashMaker.toHexString(hashMaker.hash(path));
        ModifyType actual = table.detectModify(SimpleHashMakerTest.EXAMPLE_FILE1, hash);
        table.exportResult();
        ResultTable<String, String> table2 = new ResultTable<>();
        table2.setDataFile(dataFile);
        table2.importResult();
        Assert.assertEquals(MESSAGE, table.getCount(), table2.getCount());
        Files.deleteIfExists(Paths.get(dataFile));
    }
}
