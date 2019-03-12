/**
 * 
 */
package com.scent.feedservice.Util;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class has the utility method for XPath processing. It uses
 * javax.xml.xpath.XPath api for parsing the xml and extracting the information
 * from it.
 * 
 * @author pmis30
 *
 */
public final class XMLPathUtil {

    private static final Logger LOG = LogManager.getLogger(XMLPathUtil.class);

    /**
     * No arg constructor
     * 
     */
    private XMLPathUtil() {
        super();
    }

    /**
     * This method will execute the xpath query on given xml file, and return
     * the xml nodes.
     * 
     * @param xmlFileName
     *            the xml file name
     * @param xpathQuery
     *            the xpath query
     * @return the xml node list
     */
    public static NodeList executeXPathQuery(String xmlFileName, String xpathQuery) {
        NodeList nodeList = null;

        if (StringUtils.isBlank(xmlFileName) || StringUtils.isBlank(xpathQuery)) {
            return nodeList;
        }

        try {
            File inputFile = new File(CommonUtil.class.getResource(CommonUtil.getConfigDir()).getPath()
                    .concat(Constants.FORWARD_SLASH.concat(xmlFileName)));
            // File inputFile = new File(
            // CommonUtil.class.getResource(Constants.FORWARD_SLASH.concat(xmlFileName)).getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);

            XPath xPath = XPathFactory.newInstance().newXPath();
            nodeList = (NodeList) xPath.compile(xpathQuery).evaluate(doc, XPathConstants.NODESET);

        } catch (ParserConfigurationException e) {
            LoggerUtil.error(LOG,
                    String.format(
                            "ParserConfigurationException in executeXPathQuery for xmlFileName: [%s], xpathQuery: [%s]",
                            xmlFileName, xpathQuery),
                    e);
        } catch (SAXException e) {
            LoggerUtil.error(LOG,
                    String.format("SAXException in executeXPathQuery for xmlFileName: [%s], xpathQuery: [%s]",
                            xmlFileName, xpathQuery),
                    e);
        } catch (IOException e) {
            LoggerUtil.error(LOG,
                    String.format("IOException in executeXPathQuery for xmlFileName: [%s], xpathQuery: [%s]",
                            xmlFileName, xpathQuery),
                    e);
        } catch (XPathExpressionException e) {
            LoggerUtil.error(LOG,
                    String.format(
                            "XPathExpressionException in executeXPathQuery for xmlFileName: [%s], xpathQuery [%s]",
                            xmlFileName, xpathQuery),
                    e);
        }

        return nodeList;
    }

    /**
     * This method will return the xml element array of strings from given xml
     * file.
     * 
     * @param xmlFileName
     *            the xml file name
     * @param xpathQuery
     *            the xpath query
     * @return the xml element set
     */
    public static Set<String> getXmlElementArray(String xmlFileName, String xpathQuery) {
        Set<String> elementSet = new HashSet<>();
        NodeList nodeList = executeXPathQuery(xmlFileName, xpathQuery);

        if (nodeList != null && nodeList.getLength() > 0) {

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                String nodeText = getTextFromNode(node);
                if (StringUtils.isNotBlank(nodeText)) {
                    elementSet.add(nodeText);
                }
            }
        }
        return elementSet;
    }

    /**
     * Returns any not null and not empty value from the given node.
     * 
     * @param node
     *            the node to be checked for text content
     * @return the text content from the node
     */
    private static String getTextFromNode(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            if (StringUtils.isNotBlank(element.getTextContent())) {
                return element.getTextContent().trim();
            }
        }
        return null;
    }

    /**
     * This method will return the xml child element text from given parent.
     * 
     * @param parentElement
     *            the parent element
     * @param childElement
     *            the child element
     * @return the child element text
     */
    public static String getXmlChildElementText(Element parentElement, String childElement) {
        String text = "";

        if (parentElement != null && parentElement.getElementsByTagName(childElement).item(0) != null) {
            text = parentElement.getElementsByTagName(childElement).item(0).getTextContent();
            text = StringUtils.trimToEmpty(text);
        }

        return text;
    }

}
