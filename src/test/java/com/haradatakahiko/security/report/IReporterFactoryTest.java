////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * IReporterFactoryインタフェースのテスト
 * @author 久保　由仁
 */
public class IReporterFactoryTest
{
    /**
     * TEXTの場合、TextReporterFactoryが返されること
     */
    @Test
    public void getInstanceWithTextTest() throws Exception
    {
        final String MESSAGE = "getInstance(ReportType.TEXT)がTextReporterFactoryを返すこと";
        Object obj = IReporterFactory.getInstance(ReportType.TEXT);
        Assert.assertTrue(MESSAGE, obj instanceof TextReporterFactory);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * MAILタイプの場合、MailReporterFactoryが返されること
     */
    @Test
    public void getInstanceWithMailTest() throws Exception
    {
        final String MESSAGE = "getInstance(ReportType.MAIL)がMailReporterFactoryを返すこと";
        Object obj = IReporterFactory.getInstance(ReportType.MAIL);
        Assert.assertTrue(MESSAGE, obj instanceof MailReporterFactory);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * NULLタイプの場合、NullReporterFactoryが返されること
     */
    @Test
    public void getInstanceWithNullTest() throws Exception
    {
        final String MESSAGE = "getInstance(ReportType.NULL)がNullReporterFactoryが返すこと";
        Object obj = IReporterFactory.getInstance(ReportType.NULL);
        Assert.assertTrue(MESSAGE, obj instanceof NullReporterFactory);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * getInstance(null)が例外をスローすること
     */
    @Test(expected = IllegalArgumentException.class)
    public void getInstanceWithInvalidTest() throws Exception
    {
        final String MESSAGE = "getInstance(null)が例外をスローすること";
        Object obj = IReporterFactory.getInstance(null);
    }
}
