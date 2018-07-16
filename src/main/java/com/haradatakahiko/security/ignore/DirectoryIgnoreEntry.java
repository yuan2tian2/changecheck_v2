////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.ignore;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ディレクトリを無視するエントリークラス
 * @author はらだ　たかひこ
 */
public class DirectoryIgnoreEntry extends AbstractIgnoreEntry implements Serializable
{
    //----------------------------------------------------------------------------------------------
    /**
     * ディフォルトコンストラクタ
     */
    public DirectoryIgnoreEntry()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param path パス
     * @param type 無視するタイプ
     */
    public DirectoryIgnoreEntry(String path, IgnoreType type)
    {
        super(path, type);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 無視対象化どうかを判定する
     * @param arg チェック対象のパス
     * @return 判定結果(true:無視する, false:無視しない)
     */
    @Override
    public boolean shoudIgnore(final String arg)
    {
        Path ignorePath = Paths.get(path);
        Iterator<Path> iterator = Paths.get(arg).iterator();
        while(iterator.hasNext())
        {
            Path sub = iterator.next();
            if(sub.equals(ignorePath))
            {
                return true;
            }
        }
        return false;
    }
}
