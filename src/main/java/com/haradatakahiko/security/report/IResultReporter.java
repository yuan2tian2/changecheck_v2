////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.report;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.haradatakahiko.security.ModifyType;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ファイル変更チェック結果のレポートを作成するインタフェース
 * @author はらだ　たかひこ
 */
public interface IResultReporter extends Closeable
{
    /**
     * レポートをオープンする
     * @throws IOException オープンに失敗
     */
    void open() throws IOException;
    //----------------------------------------------------------------------------------------------
    /**
     * レポートに出力する
     * @param modifyMap 変更チェック結果のMap
     * @throws IOException 出力エラー
     */
    void report(Map<String, ModifyType> modifyMap) throws IOException;
}
