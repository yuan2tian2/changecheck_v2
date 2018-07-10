////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security;
////////////////////////////////////////////////////////////////////////////////
import java.nio.file.Path;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
////////////////////////////////////////////////////////////////////////////////
/**
 * ハッシュ値計算インタフェース
 * @author はらだ　たかひこ
 */
public interface IHashMaker
{
    /**
     * 対象ファイルのハッシュ値を計算する
     * @param path 対象ファイルのパス
     * @throws IOException 読込エラー
     * @throws NoSuchAlgorithmException 不正アルゴリズム
     */
    byte[] hash(Path path) throws IOException, NoSuchAlgorithmException;
    //--------------------------------------------------------------------------
    /**
     * バイト配列を16進数文字列に変換する
     * @param byteArray バイト配列
     * @return 16進数文字列
     */
    public static String toHexString(final byte[] byteArray)
    {
        if(byteArray == null)
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(byte b : byteArray)
        {
            sb.append(Integer.toHexString(0xFF & (int)b));
        }
        return sb.toString();
    }
}
