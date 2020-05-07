package lesson10;
import java.io.InputStream;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLDOMParserAndPrinter {
	public static void main(String[] args) { // <1>
      try(final InputStream				is = XMLDOMParserAndPrinter.class.getResourceAsStream("example.xml")){
    	 final DocumentBuilderFactory 	dbFactory = DocumentBuilderFactory.newInstance();
         final DocumentBuilder 			dBuilder = dbFactory.newDocumentBuilder();
         final Document 				doc = dBuilder.parse(is);
         
         doc.getDocumentElement().normalize();	// <2>
         System.err.println("Root element :" + doc.getDocumentElement().getNodeName());
         
         final NodeList 				nList = doc.getElementsByTagName("person");
         System.err.println("----------------------------");
         
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
         
         final Node				newPerson = doc.createElement("person");	// <4> 
         final Node				firstName = doc.createElement("firstname"), lastName = doc.createElement("lastname"); 
         final Node				nickName = doc.createElement("nickname"), age = doc.createElement("age"); 
         
         firstName.setTextContent("new firstname");
         lastName.setTextContent("new lastname");
         nickName.setTextContent("new nick");
         age.setTextContent("100");
         
         newPerson.appendChild(firstName);
         newPerson.appendChild(lastName);
         newPerson.appendChild(nickName);
         newPerson.appendChild(age);
         
         final Node				attr = doc.createAttribute("id");		// <5>
         
         attr.setNodeValue("3");
         
         newPerson.getAttributes().setNamedItem(attr);
         
         doc.getDocumentElement().appendChild(newPerson);         		// <6>

         final Source 			source = new DOMSource(doc);			// <7>
         final Result 			result = new StreamResult(System.err);
         final Transformer 		xformer = TransformerFactory.newInstance().newTransformer();
         
         xformer.transform(source, result);         
      } catch (Exception e) {
         e.printStackTrace();
      }
	}
}