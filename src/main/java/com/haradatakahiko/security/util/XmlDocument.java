////////////////////////////////////////////////////////////////////////////////
// (C) Copyright HARADA, Takahiko 2018 All rights reserved.
////////////////////////////////////////////////////////////////////////////////
package com.haradatakahiko.security.util;
////////////////////////////////////////////////////////////////////////////////
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import javax.xml.namespace.NamespaceContext;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * XML管理オブジェクト
 * <pre>作成日：2018.03.31</pre>
 * <pre>更新日：2018.04.29 名前空間に対応</pre>
 * @author はらだ　たかひこ
 */
public class XmlDocument
{
    /** XML Document */
    protected Document document = null;
    
    /** 名前空間コンテキスト */
    protected NamespaceContext namespaceContext = null;
    //----------------------------------------------------------------------------------------------
    /**
     * ディフォルトコンストラクタ
     */
    public XmlDocument()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param arg Dom Document
     */
    public XmlDocument(Document arg)
    {
        document = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 入力ストリームからXMLをロードする
     * @param stream 入力ストリーム
     * @throws IOException 読込失敗
     */
    public void load(InputStream stream) throws IOException
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(stream);
        }
        catch(ParserConfigurationException | SAXException e)
        {
            throw new IOException(e);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * XMLファイルからXMLをロードする
     * @param path XMLファイルのPathオブジェクト
     * @throws IOException 読込失敗
     */
    public void load(Path path) throws IOException
    {
        try(InputStream stream = Files.newInputStream(path))
        {
            load(stream);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * xpathを使ってノードリストを取得する
     * @param xpath ノードリストのXPath
     * @return ノードリスト
     * @throws XPathExpressionException XPathの文法エラー
     */
    public IterableNodeList getNodeList(final String xpath) throws XPathExpressionException
    {
        if(document == null)
        {
            throw new IllegalStateException("XML not loaded");
        }
        XPathExpression expression = compileXPath(xpath);
        Object result = expression.evaluate(document, XPathConstants.NODESET);
        return new IterableNodeList((NodeList)result);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * XPathを使って単一のノードを取得する
     * @param xpath XPath文字列
     * @return ノード
     * @throws XPathExpressionException XPathの文法エラー
     */
    public Node getSingleNode(final String xpath) throws XPathExpressionException
    {
        if(document == null)
        {
            throw new IllegalStateException("XML not loaded");
        }
        XPathExpression expression = compileXPath(xpath);
        Object result = expression.evaluate(document, XPathConstants.NODE);
        return (Node)result;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * XPathを使って単一ノードのテキストを取得する
     * @param xpath XPath文字列
     * @return ノードのテキスト
     * throws XPathExpressionException XPathの文法エラー
     */
    public String getNodeText(final String xpath) throws XPathExpressionException
    {
        Node node = getSingleNode(xpath);
        return getNodeText(node);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * XMLノードの属性を取得する
     * @param node 対象のノード
     * @param attributeName 属性名
     * @return 属性値
     */
    public static String getAttribute(Node node, final String attributeName)
    {
        NamedNodeMap attributes = node.getAttributes();
        Node attributeNode = attributes.getNamedItem(attributeName);
        return (attributeNode != null) ? attributeNode.getTextContent() : null;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 対象ノードのテキストを取り出す
     * @param node 処理対象ノード
     * @return 取り出したテキスト
     */
    public static String getNodeText(final Node node)
    {
        if(node == null)
        {
            return null;
        }
        return node.getTextContent();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 対象ノードからの相対XPathによって子ノードを取得する
     * @param node 処理対象ノード
     * @param xpath 処理対象ノードからの相対xpath文字列
     * @return 取得したノード
     * @throws XPathExpressionException XPath文法エラー
     */
    public static Node getSingleNode(Node node, String xpath, NamespaceContext context) 
                                        throws XPathExpressionException
    {
        if(node == null)
        {
            throw new IllegalArgumentException();
        }
        XPathExpression expression = compileXPath(xpath, context);
        Object result = expression.evaluate(node, XPathConstants.NODE);
        return (result != null) ? (Node)result : null;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 対象ノードからの相対XPathによって子ノードを取得する
     * @param node 処理対象ノード
     * @param xpath 処理対象ノードからの相対xpath文字列
     * @return 取得したノード
     * @throws XPathExpressionException XPath文法エラー
     */
    public static Node getSingleNode(Node node, String xpath) throws XPathExpressionException
    {
        return getSingleNode(node, xpath, null);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * XPath文字列をコンパイルする
     * @param arg XPath文字列
     * @param context 名前空間コンテキスト
     * @return コンパイル済のXPathExpressionオブジェクト
     * @throws XPathExpressionException XPathの文法エラー
     */
    private static XPathExpression compileXPath(final String arg, NamespaceContext context) 
                                                        throws XPathExpressionException
    {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        if(context != null)
        {
            xpath.setNamespaceContext(context);
        }
        return xpath.compile(arg);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * Xpathをコンパイルする
     * @parm arg Xpath文字列
     * @return コンパイル済xpath
     * @throws XPathExpressionException Xpath式の文法エラー
     */
    private XPathExpression compileXPath(final String arg) throws XPathExpressionException
    {
        return compileXPath(arg, namespaceContext);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 名前空間コンテキストをセットする
     * @param arg 名前空間コンテキスト
     */
    public void setNamespaceContext(NamespaceContext arg)
    {
        namespaceContext = arg;
    }
}
