////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * TextReporterFactoryのテスト
 * @author はらだ　たかひこ
 */
public class TextReporterFactoryTest
{
    /**
     * TextReporterのインスタンスを構築できること
     */
    @Test
    public void createReporterTest() throws Exception
    {
        final String MESSAGE= "TextReporterのインスタンスを構築できること";
        Map<String, String> map = new HashMap<>();
        map.put("file", "work/test.txt");
        TextReporterFactory factory = new TextReporterFactory();
        Object obj = factory.createReporter(map);
        Assert.assertTrue(MESSAGE, obj instanceof TextReporter);
    }
}
