////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.Assert;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * FileChangeCheckerのテスト
 * @author はらだ　たかひこ
 */
public class FileChangeCheckerTest
{
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
}
