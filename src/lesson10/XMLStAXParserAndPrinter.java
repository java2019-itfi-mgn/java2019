package lesson10;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

public class XMLStAXParserAndPrinter {
	private static void parseXML() throws IOException, XMLStreamException {	// <1>
		try(final InputStream		is = XMLStAXParserAndPrinter.class.getResourceAsStream("example.xml")){
			final XMLInputFactory 	factory = XMLInputFactory.newInstance();
			final XMLStreamReader 	reader = factory.createXMLStreamReader(is);
	         
			while(reader.hasNext()){		// <2>
			  final int event = reader.next();
			   
			  switch(event){				// <3>
			    case XMLStreamConstants.START_ELEMENT:
			    	System.err.println("Start local name: " + reader.getLocalName());
			    	break;
			    case XMLStreamConstants.CHARACTERS:
			    	System.err.println("Text: "+reader.getText().trim());
			    	break;
			    case XMLStreamConstants.END_ELEMENT:
			    	System.err.println("End local name: " + reader.getLocalName());
			    	break;
			    case XMLStreamConstants.START_DOCUMENT:
			    	break;
			  }
			}
	  }
	}

	private static void printXML() throws IOException, XMLStreamException {	// <4>
		XMLOutputFactory 	factory = XMLOutputFactory.newInstance();
		XMLEventFactory  	eventFactory = XMLEventFactory.newInstance();
	    XMLEvent 			event;

		try{XMLEventWriter writer = factory.createXMLEventWriter(System.err);	// <5>

		    event = eventFactory.createStartDocument(); 	// <6>
		    writer.add(event);
		    event = eventFactory.createStartElement("test", "http://test.com", "document"); 	
		    writer.add(event);
		    event = eventFactory.createNamespace("test", "http://test.com"); 					
		    writer.add(event);
		    event = eventFactory.createAttribute("attributeName", "attributeValue"); 
		    writer.add(event);
		    event = eventFactory.createEndElement("test", "http://test.com", "document"); 
		    writer.add(event);

		    writer.flush();
		} catch (XMLStreamException e) {
		    e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws IOException, XMLStreamException {
		parseXML();
		printXML();
	}
}
