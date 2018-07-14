////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.Assert;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * NullReporterFactoryのテスト
 * @author はらだ　たかひこ
 */
public class NullReporterFactoryTest
{
    /**
     * NullReporterのインスタンスを作成できること
     */
    @Test
    public void createReporterTest() throws Exception
    {
        final String MESSAGE = "NullReporterのインスタンスを作成できること";
        NullReporterFactory factory = new NullReporterFactory();
        IResultReporter reporter = factory.createReporter(null);
        Assert.assertTrue(MESSAGE, reporter instanceof NullReporter);
    }
}
