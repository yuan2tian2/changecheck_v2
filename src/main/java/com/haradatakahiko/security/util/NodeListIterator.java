////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.util;
////////////////////////////////////////////////////////////////////////////////
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
////////////////////////////////////////////////////////////////////////////////
/**
 * XMLノードリストのイテレータ
 * @author はらだ　たかひこ
 * @version 1.0.0
 */
public class NodeListIterator implements Iterator<Node>
{
    /** ノードリスト */
    private NodeList nodeList;
    
    /** 現在のインデックス */
    private int index = 0;
    
    /** 現在指しているNode */
    private Node cache = null;
    //--------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param nodeList ノードリスト
     */
    public NodeListIterator(NodeList nodeList)
    {
        this.nodeList = nodeList;
    }
    //--------------------------------------------------------------------------
    /**
     * 次のノードがあるか否かを返す
     * @return true:次のノードがある, false:次のノードがない
     */
    public boolean hasNext()
    {
        return index < nodeList.getLength();
    }
    //--------------------------------------------------------------------------
    /**
     * 次のノードを返す
     * @return 次のノード
     */
    public Node next()
    {
        cache = nodeList.item(index++);
        if(cache == null)
        {
             throw new NoSuchElementException();
        }
        return cache;
    }
    //--------------------------------------------------------------------------
    /**
     * ノードを削除する
     */
    public void remove()
    {
        if(cache == null)
        {
            throw new IllegalStateException();
        }
        cache.getParentNode().removeChild(cache);
        cache = null;
        index--;
    }
}
////////////////////////////////////////////////////////////////////////////////
