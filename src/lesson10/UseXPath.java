package lesson10;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UseXPath {
	public static void main(String[] args) { // <1>
		try(final InputStream				is = XMLDOMParserAndPrinter.class.getResourceAsStream("example.xml")){
			final DocumentBuilderFactory 	dbFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder 			dBuilder = dbFactory.newDocumentBuilder();
			final Document 					doc = dBuilder.parse(is);
			 
			doc.getDocumentElement().normalize();
			
			final XPath 					xpath = XPathFactory.newInstance().newXPath();		// <2>
			final XPathExpression 			expr = xpath.compile("/*/*[@id='2']");
			final NodeList 					nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

	         for (int temp = 0; temp < nList.getLength(); temp++) {	// <3>
	             final Node 		nNode = nList.item(temp);
	             System.err.println("\nCurrent Element :" + nNode.getNodeName());
	             
	             if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                final Element 	eElement = (Element) nNode;
	                
	                System.err.println("Person id: " + eElement.getAttribute("id"));
	                System.err.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
	                System.err.println("Last Name : " + eElement .getElementsByTagName("lastname").item(0).getTextContent());
	                System.err.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
	                System.err.println("Age : " + eElement.getElementsByTagName("age").item(0).getTextContent());
	             }
	          }
			
		} catch (Exception e) {
		      e.printStackTrace();
		}
	}
}
