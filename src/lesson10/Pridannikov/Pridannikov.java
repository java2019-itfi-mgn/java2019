package Pridannikov;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

public class Pridannikov {
	public static void main(String[] args) { // <1>
      try(final InputStream				is = Pridannikov.class.getResourceAsStream("exercise.svg")){
    	 final DocumentBuilderFactory 	dbFactory = DocumentBuilderFactory.newInstance();
         final DocumentBuilder 			dBuilder = dbFactory.newDocumentBuilder();
         final Document 				doc = dBuilder.parse(is);
         
      
         FileWriter writer = new FileWriter("D:\\ex.txt",false); 
         
         doc.getDocumentElement().normalize();	// <2>
         String text="Root element: " + doc.getDocumentElement().getNodeName();
        
         writer.write(text);
         
         final NodeList 				nList = doc.getElementsByTagName("text");
         
         text=System.lineSeparator()+"----------------------------";
         writer.write(text);
         
         for (int temp = 0; temp < nList.getLength(); temp++) {	// <3>
            final Node 		nNode = nList.item(temp);
           
            text=System.lineSeparator()+"Current Element: " + nNode.getTextContent().toLowerCase();
            writer.write(text);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               final Element 	eElement = (Element) nNode;
               
		         
              
                  // запись всей строки
                    text = System.lineSeparator()+"x: " + eElement.getAttribute("x")+System.lineSeparator()+
                		   "y: " + eElement.getAttribute("y")+System.lineSeparator()+
                		   "font-family: " + eElement.getAttribute("font-family")+System.lineSeparator()
                		   +"font-weight: " + eElement.getAttribute("font-weight")+System.lineSeparator()
                		   +"font-style: " + eElement.getAttribute("font-style")+System.lineSeparator()
                		   +"fill: " + eElement.getAttribute("fill")+System.lineSeparator()+
                		   "transform: " + eElement.getAttribute("transform")+System.lineSeparator()
                		   ;
                   
                   writer.write(text);
                   // запись по символам
              
                   
                    
                   writer.flush();
             
              
            
            }
         }
         
        
      } catch (Exception e) {
         e.printStackTrace();
      }
	}
}