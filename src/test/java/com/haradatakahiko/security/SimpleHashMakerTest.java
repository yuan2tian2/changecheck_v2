////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * SimpleHashMakerのテスト
 * @author はらだ　たかひこ
 */
public class SimpleHashMakerTest
{
    /** テストに用いるファイル1 */
    public static final String EXAMPLE_FILE1 = "lib/log4j-core-2.11.0.pom";
    
    /** テストに用いるファイル2 */
    public static final String EXAMPLE_FILE2 = "lib/log4j-jcl-2.11.0.pom";
    
    /** テストに使用するアルゴリズム */
    public static final String ALGORITHM = "SHA-256";
    //----------------------------------------------------------------------------------------------
    /**
     * SHA-1アルゴリズムでSimpleHashMakerオブジェクトが作成できること
     */
    @Test
    public void constructorWithSha1Test() throws Exception
    {
        final String MESSAGE = "SHA-1アルゴリズムでSimpleHashMakerオブジェクトが作成できること";
        Object obj = new SimpleHashMaker("SHA-1");
        Assert.assertTrue(MESSAGE, obj instanceof SimpleHashMaker);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 無効なアルゴリズムを指定すると例外がスローされること
     */
    @Test(expected = NoSuchAlgorithmException.class)
    public void constructorWithInvalidAlgorighmTest() throws Exception
    {
        final String MESSAGE = "無効なアルゴリズムを指定すると例外がスローされること";
        Object obj = new SimpleHashMaker("INVALID-ALGORITHM");
    }
    //----------------------------------------------------------------------------------------------
    /**
     * hash(null)がIllegalArgumentExceptionをスローすること
     */
    @Test(expected = IllegalArgumentException.class)
    public void hashWithNullTest() throws Exception
    {
        final String MESSAGE = "hash(null)がIllegalArgumentExceptionをスローすること";
        SimpleHashMaker hashMaker = new SimpleHashMaker(ALGORITHM);
        byte[] result = hashMaker.hash(null);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 内容の異なるファイルでハッシュ値が異なること
     */
    @Test
    public void calculateHashWithDifferenceTest() throws Exception
    {
        final String MESSAGE = "内容の異なるファイルでハッシュ値が異なること";
        SimpleHashMaker hashMaker = new SimpleHashMaker(ALGORITHM);
        String result1 = IHashMaker.toHexString(hashMaker.hash(Paths.get(EXAMPLE_FILE1)));
        String result2 = IHashMaker.toHexString(hashMaker.hash(Paths.get(EXAMPLE_FILE2)));
        Assert.assertFalse(MESSAGE, result1.equals(result2));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 同一ファイルのハッシュ値が同じであること
     */
    @Test
    public void calculateHashWithSameFileTest() throws Exception
    {
        final String MESSAGE = "同一ファイルのハッシュ値が同じであること";
        SimpleHashMaker hashMaker = new SimpleHashMaker(ALGORITHM);
        String result1 = IHashMaker.toHexString(hashMaker.hash(Paths.get(EXAMPLE_FILE1)));
        String result2 = IHashMaker.toHexString(hashMaker.hash(Paths.get(EXAMPLE_FILE1)));
        Assert.assertTrue(MESSAGE, result1.equals(result2));
    }
}
