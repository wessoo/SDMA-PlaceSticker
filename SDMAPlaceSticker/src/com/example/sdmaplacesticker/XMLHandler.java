package com.example.sdmaplacesticker;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {
	Boolean parsing = false;
	String elementValue = null;
	public static Work data = null;
	
	public static Work getXMLData() {
		return data;
	}
	
	public static void setXMLData(Work data) {
		XMLHandler.data = data;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		System.out.println("start element");
		parsing = true;
		
		if(localName.equals("metadata")) {
			data = new Work(); 
		} else if(localName.equals("work")) {
			
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) {
		parsing = false;
		
		if(localName.equalsIgnoreCase("title")) {
			data.setTitle(elementValue);
		} else if (localName.equalsIgnoreCase("artist")) {
			data.setArtist(elementValue);
		} else if (localName.equalsIgnoreCase("date")) {
			data.setDate(elementValue);
		} else if (localName.equalsIgnoreCase("place")) {
			data.setPlace(elementValue);
		} else if (localName.equalsIgnoreCase("dimensions")) {
			data.setDimensions(elementValue);
		} else if (localName.equalsIgnoreCase("description")) {
			data.setDescription(elementValue);
		}
	}
	
	@Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (parsing) {
            elementValue = new String(ch, start, length);
            parsing = false;
        }
    }
}
