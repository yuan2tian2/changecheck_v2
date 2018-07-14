////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.Assert;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * IHashMakerのテスト
 * @author はらだ　たかひこ
 */
public class IHashMakerTest
{
    /** 0xFF */
    public static final byte MAX_VALUE = -1;
    //----------------------------------------------------------------------------------------------
    /**
     * 空の配列を16進数文字列に変換した場合に空文字列が返されること
     */
    @Test
    public void toHexStringWithEmptyTest() throws Exception
    {
        final String MESSAGE = "空の配列を16進数文字列に変換した場合に空文字列が返されること";
        Assert.assertEquals(MESSAGE, "", IHashMaker.toHexString(null));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 0xFFFFのバイト配列を16進数文字列へ変換できること
     */
    @Test
    public void toHexWithHighValueTest() throws Exception
    {
        final String MESSAGE = "0xFFFFのバイト配列を16進数文字列へ変換できること";
        byte[] value = new byte[]{MAX_VALUE, MAX_VALUE};
        final String EXPECT = "ffff";
        String actual = IHashMaker.toHexString(value);
        Assert.assertEquals(MESSAGE, EXPECT, actual);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 0x000000のバイト配列を16進数文字列へ変換できること
     */
    @Test
    public void toHexWithZeroValueTest() throws Exception
    {
        final String MESSAGE = "0x000000のバイト配列を16進数文字列へ変換できること";
        byte[] value = new byte[]{0x0, 0x0, 0x0};
        final String EXPECT = "000000";
        String actual = IHashMaker.toHexString(value);
        Assert.assertEquals(MESSAGE, EXPECT, actual);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 0x123456789ABCDEF0のバイト配列を16進数文字列に変換できること
     */
    @Test
    public void toHexWithValueTest() throws Exception
    {
        final String MESSAGE = "0x123456789ABCDEF0のバイト配列を16進数文字列に変換できること";
        byte[] value = new byte[]{(byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78,
                                  (byte)0x9A, (byte)0xBC, (byte)0xDE, (byte)0xF0};
        final String EXPECT = "123456789abcdef0";
        String actual = IHashMaker.toHexString(value);
        Assert.assertEquals(MESSAGE, EXPECT, actual);
    }
}
