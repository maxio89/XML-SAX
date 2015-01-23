package com.agh.xmlj;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CustomHandler extends DefaultHandler {

    int currentIndent = 0;

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentIndent++;
        System.out.println(getIndent(currentIndent) + qName + ":");
        int attrNo = attributes.getLength();
        for (int i = 0; i < attrNo; i++) {
            System.out.println(getIndent(currentIndent + 1) + "@" + attributes.getQName(i) + "=" + attributes.getValue(i));
        }
    }

    public void endElement(String uri, String localName,
                           String qName) throws SAXException {
        currentIndent--;
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        String val = new String(ch, start, length);
        if (val.trim().length() > 0) {
            System.out.println(getIndent(currentIndent + 1) + "\"" + new String(ch, start, length) + "\"");
        }
    }

    private String getIndent(int val) {
        String ident = " ";
        for (int i = 0; i < val; i++) {
            ident = ident + " ";
        }
        return ident;
    }
}
