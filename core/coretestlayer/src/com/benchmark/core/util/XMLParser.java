

package com.benchmark.core.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * It is used to traverse elements and attributes of an XML document. XPath
 * provides various type of expressions which can be used to enquire relevant
 * information from the XML document.
 */
public class XMLParser {

	/**
	 * Method to get Value from in XML file for XPATHs defined
	 * 
	 * @param xPathNodeName
	 * @param xPathNodeValue
	 * @param valueOfNodeName
	 * @param xmlFile
	 * @return
	 */
	public static String getValueInXMLForXpaths(String xPathNodeName,
			String xPathNodeValue, String valueOfNodeName, String xmlFile) {
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = null;
			builder = builderFactory.newDocumentBuilder();
			File inputFile = new File(xmlFile);
			Document xmlDocument = builder.parse(inputFile);
			XPath xPath = XPathFactory.newInstance().newXPath();

			System.out.println(xPathNodeName);
			NodeList nodeList = (NodeList) xPath.compile(xPathNodeName)
					.evaluate(xmlDocument, XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getFirstChild().getNodeValue()
						.equals(valueOfNodeName)) {
					NodeList nodeList2 = (NodeList) xPath.compile(
							xPathNodeValue).evaluate(xmlDocument,
							XPathConstants.NODESET);
					System.out.println("Value found"
							+ nodeList.item(i).getFirstChild().getNodeValue());
					return nodeList2.item(i).getFirstChild().getNodeValue();
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException
				| XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Method to replace Value in XML file for XPATHs defined
	 * 
	 * @param xPathNodeName
	 * @param xPathNodeValue
	 * @param valueOfNodeName
	 * @param valueOfNodeToUpdate
	 * @param xmlFile
	 */
	public static void replaceValueInXMLForXpaths(String xPathNodeName,
			String xPathNodeValue, String valueOfNodeName,
			String valueOfNodeToUpdate, String xmlFile) {
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = null;
			builder = builderFactory.newDocumentBuilder();
			File inputFile = new File(xmlFile);
			Document xmlDocument = builder.parse(inputFile);
			XPath xPath = XPathFactory.newInstance().newXPath();

			System.out.println(xPathNodeName);
			NodeList nodeList = (NodeList) xPath.compile(xPathNodeName)
					.evaluate(xmlDocument, XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getFirstChild().getNodeValue()
						.equals(valueOfNodeName)) {
					NodeList nodeList2 = (NodeList) xPath.compile(
							xPathNodeValue).evaluate(xmlDocument,
							XPathConstants.NODESET);
					System.out.println("Value found"
							+ nodeList.item(i).getFirstChild().getNodeValue());
					System.out.println("Value replaced is "
							+ nodeList2.item(i).getFirstChild().getNodeValue());
					nodeList2.item(i).getFirstChild()
							.setNodeValue(valueOfNodeToUpdate);
					System.out.println("Value replaced is "
							+ nodeList2.item(i).getFirstChild().getNodeValue());
				}
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDocument);
			StreamResult result = new StreamResult(inputFile);
			transformer.transform(source, result);

			System.out.println("Done");

		} catch (ParserConfigurationException | SAXException | IOException
				| XPathExpressionException | TransformerException e) {
			e.printStackTrace();
		}
	}

}
