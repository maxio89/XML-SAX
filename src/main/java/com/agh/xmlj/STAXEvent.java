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
public class STAXEvent {

    public static final String OUTPUT_PATH = "src/main/resources/result_event.xml";
    public static final String SOURCE_PATH = "src/main/resources/main.xml";
    public static final String INCLUDE_PATH = "src/main/resources/";
    public static final String INCLUDE_TAG = "include";
    public static final String FILENAME_ATTR_NAME = "filename";

    public static void main(String[] argv) {

        final File outputFile = new File(OUTPUT_PATH);

        try {
            final XMLInputFactory inFactory = XMLInputFactory.newInstance();
            final XMLEventReader eventReader = inFactory.createXMLEventReader(new FileInputStream(SOURCE_PATH));

            final XMLOutputFactory factory = XMLOutputFactory.newInstance();
            final XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(outputFile));


            boolean flag = false;
            while (eventReader.hasNext()) {
                final XMLEvent event = eventReader.nextEvent();
                final int eventType = event.getEventType();
                //check if this include
                if (eventType == XMLEvent.START_ELEMENT && event.asStartElement().getName().toString().equalsIgnoreCase(INCLUDE_TAG)) {
                    flag = true;
                    //read file name
                    final Attribute filenameAttr = event.asStartElement().getAttributeByName(new QName(FILENAME_ATTR_NAME));
                    final String filenameValue = filenameAttr.getValue();
                    //read other file
                    final XMLEventReader includedFileReader = inFactory.createXMLEventReader(new FileInputStream(INCLUDE_PATH + filenameValue));
                    while (includedFileReader.hasNext()) {
                        XMLEvent inclEvent = includedFileReader.nextEvent();
                        rewriteEvent(inclEvent, writer, true);
                    }
                } else if (eventType == XMLStreamConstants.END_ELEMENT && event.asEndElement().getName().toString().equalsIgnoreCase(INCLUDE_TAG)) {
                    flag = false;
                } else if (!flag) {
                    rewriteEvent(event, writer, true);
                }
            }
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void rewriteEvent(final XMLEvent event, final XMLEventWriter writer, final boolean skipStartAndEndDocument) throws XMLStreamException {
        final int type = event.getEventType();
        if (!skipStartAndEndDocument || (type != XMLEvent.END_DOCUMENT && type != XMLEvent.START_DOCUMENT)) {
            writer.add(event);
        }
    }

}
