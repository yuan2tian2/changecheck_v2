////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    /** ファイルのパス */
    private static final String FILE_PATH = "work/test.txt";
    //----------------------------------------------------------------------------------------------
    /**
     * TextReporterのインスタンスを構築できること
     */
    @Test
    public void createReporterTest() throws Exception
    {
        final String MESSAGE= "TextReporterのインスタンスを構築できること";
        Map<String, String> map = new HashMap<>();
        map.put("file", FILE_PATH);
        TextReporterFactory factory = new TextReporterFactory();
        try(Closeable obj = factory.createReporter(map))
        {
            Assert.assertTrue(MESSAGE, obj instanceof TextReporter);
        }
        catch(IOException e)
        {
            throw e;
        }
        finally
        {
            Files.deleteIfExists(Paths.get(FILE_PATH));
        }
    }
}
