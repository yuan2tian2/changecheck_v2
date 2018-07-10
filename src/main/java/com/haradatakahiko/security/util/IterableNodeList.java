////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.util;
////////////////////////////////////////////////////////////////////////////////
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
////////////////////////////////////////////////////////////////////////////////
/**
 * 反復可能ノードリスト
 * @author はらだ　たかひこ
 * @version 1.0.0
 */
public final class IterableNodeList implements Iterable<Node>, NodeList
{
    /** XMLノードリスト */
    private NodeList nodeList = null;
    //--------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param nodeList XMLノードリスト
     */
    public IterableNodeList(NodeList nodeList)
    {
        this.nodeList = nodeList;
    }
    //--------------------------------------------------------------------------
    /**
     * iteratorを返す
     * @return ノードのiterator
     */
    public Iterator<Node> iterator()
    {
        return new NodeListIterator(nodeList);
    }
    //--------------------------------------------------------------------------
    /**
     * 指定されたインデックスのNodeを返す
     * @param index インデックス(0-)
     * @return Node
     */
    public Node item(final int index)
    {
        return nodeList.item(index);
    }
    //--------------------------------------------------------------------------
    /**
     * 要素の数を返す
     * @return 要素数
     */
    public int getLength()
    {
        return nodeList.getLength();
    }
}
