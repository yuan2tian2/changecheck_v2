////////////////////////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.util;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.namespace.NamespaceContext;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * NamespaceContextインタフェースの実装クラス
 * <pre>作成日：2018.04.29</pre>
 * @author はらだ　たかひこ
 */
public class NamespaceContextImpl implements NamespaceContext
{
    /** URIマップ（キーとURIのマップ） */
    protected Map<String, String> uriMap = new HashMap<>();
    
    //----------------------------------------------------------------------------------------------
    /**
     * ディフォルトコンストラクタ
     */
    public NamespaceContextImpl()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    public NamespaceContextImpl(Map<String, String> map)
    {
        this();
        uriMap = map;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 現在のスコープ内の接頭辞にバインドされている名前空間URIを取得します。
     * @param prefix 検索する接頭辞
     * @return 現在のスコープ内の接頭辞にバインドされた名前空間URI
     */
    @Override
    public String getNamespaceURI(String prefix) throws IllegalArgumentException
    {
        if(prefix == null)
        {
            throw new IllegalArgumentException();
        }
        return uriMap.get(prefix);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 現在のスコープ内の名前空間URIにバインドされている接頭辞を取得します。
     * @param namespaceURI 検索する名前空間のURI
     * @return 現在のコンテキスト内の名前空間URIにバインドされた接頭辞
     * @throws IllegalArgumentException namespaceURIがnullの場合
     */
    @Override
    public String getPrefix(String namespaceUri) throws IllegalArgumentException
    {
        if(namespaceUri == null)
        {
            throw new IllegalArgumentException();
        }
        for(Map.Entry<String, String> entry : uriMap.entrySet())
        {
            if(namespaceUri.equals(entry.getValue()))
            {
                return entry.getKey();
            }
        }
        return null;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 現在のスコープ内の名前空間URIにバインドされているすべての接頭辞を取得します。
     * @param namespaceUri 検索する名前空間のURI
     * @return 現在のスコープ内の名前空間URIにバインドされているすべての接頭辞のIterator
     */
    @Override
    public Iterator<String> getPrefixes(String namespaceUri)
    {
        if(namespaceUri == null)
        {
            throw new IllegalArgumentException();
        }
        return uriMap.entrySet().stream()
                    .filter(e -> namespaceUri.equals(e.getValue()))
                    .collect(Collectors.toMap(e -> e.getKey(), e->e.getValue()))
                    .keySet()
                    .iterator();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 接頭辞とURIとの関連Mapをセットする
     * @param map 接頭辞とURIとの関連Map
     */
    public void setUriMap(Map<String, String> map)
    {
        uriMap = map;
    }
}
