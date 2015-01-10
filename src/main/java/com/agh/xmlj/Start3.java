package com.agh.xmlj;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 * Created by rafal on 1/10/15.
 */
public class Start3 {

    public static void main(String[] argv) {

        File outputFile = new File("src/main/resources/result2.xml");

        try {
            XMLInputFactory inFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inFactory.createXMLEventReader(new FileInputStream("src/main/resources/main.xml"));
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(outputFile));
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();


            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

//                if (event.getEventType() == XMLEvent.START_ELEMENT) {
//                    if (event.asStartElement().getName().toString().equalsIgnoreCase("book")) {
//                        writer.add(eventFactory.createStartElement("", null, "index"));
//                        writer.add(eventFactory.createEndElement("", null, "index"));
//                    }
//                }
                //check if this include
                if (event.getEventType() == XMLEvent.START_ELEMENT && event.asStartElement().getName().toString().equalsIgnoreCase("include")) {
                    //
                    final Attribute filenameAttr = event.asStartElement().getAttributeByName(new QName("filename"));
                    final String filenameValue = filenameAttr.getValue();
                    //read other file
                    XMLEventReader includedFileReader = inFactory.createXMLEventReader(new FileInputStream("src/main/resources/" + filenameValue));
                    while (includedFileReader.hasNext()) {
                        writer.add(includedFileReader.nextEvent());
                    }
                } else {
                    writer.add(event);
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
