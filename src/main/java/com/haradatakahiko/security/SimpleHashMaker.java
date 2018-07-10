////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security;
////////////////////////////////////////////////////////////////////////////////
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.DigestInputStream;
import java.security.NoSuchAlgorithmException;
////////////////////////////////////////////////////////////////////////////////
/**
 * ハッシュ値計算の単純な実装クラス
 * @author はらだ　たかひこ
 */
public class SimpleHashMaker implements IHashMaker
{
    /** ハッシュアルゴリズム */
    protected MessageDigest messageDigest = null;
    //--------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param algorithm ハッシュアルゴリズム種別を表す文字列
     */
    public SimpleHashMaker(String algorithm) throws NoSuchAlgorithmException
    {
        messageDigest = MessageDigest.getInstance(algorithm);
    }
    //--------------------------------------------------------------------------
    /**
     * ファイルを読み込んでハッシュ値を計算して返す
     * @param path 対象ファイルのパス
     * @throws IOException 読込エラー
     */
    @Override
    public byte[] hash(Path path) throws IOException
    {
        if(path == null)
        {
            throw new IllegalArgumentException("argument is null");
        }
        byte[] buffer = Files.readAllBytes(path);
        int readCount = 0;
        try(ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
            DigestInputStream dis = new DigestInputStream(stream, messageDigest))
        {
            dis.on(true);
            while(true)
            {
                if((readCount = dis.read(buffer)) <= 0)
                {
                    break;
                }
            }
        }
        return messageDigest.digest();
    }
}
