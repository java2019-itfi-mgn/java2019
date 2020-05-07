package lesson10;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLSAXParser {
	public static void main(String[] args){		// <1>
		try(final InputStream		is = XMLSAXParser.class.getResourceAsStream("example.xml")){	
			final SAXParserFactory 	factory = SAXParserFactory.newInstance();
			final SAXParser 		saxParser = factory.newSAXParser();
			          
			saxParser.parse(is, new MyHandler());     
		} catch (Exception e) {
			e.printStackTrace();
		}
	}   
}

class MyHandler extends DefaultHandler {	// <2>
	final StringBuilder	sb = new StringBuilder(); 

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName) {	// <3>
			case "firstname" : case "lastname" : case "nickname" : case "age" :
				sb.setLength(0);
				System.err.println("Start "+qName);
				break;
			case "person" :
				System.err.println("Start "+qName+" with ID="+attributes.getValue("id"));
				break;
			default :
				System.err.println("Start "+qName);
				break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {	// <4>
			case "firstname" : case "lastname" : case "nickname" : case "age" :
				System.err.println("Tag "+qName+" with value '"+sb.toString()+"' was processed");
				break;
			default :
				System.err.println("Tag "+qName+" was processed");
				break;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		sb.append(ch,start,length);	// <5>
	}
}