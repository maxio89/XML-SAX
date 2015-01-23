package com.agh.xmlj;

import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SAXParserTest {

    public static void main(String[] argv) {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            //factory.setValidating(true);
            //factory.setNamespaceAware(true);
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new CustomHandler();

            saxParser.parse("src/main/resources/file.xml", handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
