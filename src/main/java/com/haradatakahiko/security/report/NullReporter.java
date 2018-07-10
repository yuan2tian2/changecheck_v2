////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.ModifyType;

import java.io.IOException;
import java.util.Map;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ファイル変更レポートを出力するダミーレポーター
 * @author はらだ　たかひこ
 */
public class NullReporter implements IResultReporter
{
    /**
     * レポートを出力するダミーメソッド
     * @param modifyMap 変更チェック結果のMap
     * @throws IOException ありえない
     */
    @Override
    public void report(Map<String, ModifyType> modifyMap) throws IOException
    {
        // do nothing
    }
    //----------------------------------------------------------------------------------------------
    /**
     * クローズするダミーメソッド
     */
    @Override
    public void close()
    {
        // do nothing
    }
    //----------------------------------------------------------------------------------------------
    /**
     * オープンするダミーメソッド
     * @throws IOException ありえない
     */
    @Override
    public void open() throws IOException
    {
        //do nothing
    }
}
