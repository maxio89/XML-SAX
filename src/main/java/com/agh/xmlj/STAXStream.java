package com.agh.xmlj;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 * Created by rafal on 1/10/15.
 */
public class STAXStream {

    public static final String OUTPUT_PATH = "src/main/resources/result_stream.xml";
    public static final String SOURCE_PATH = "src/main/resources/main.xml";
    public static final String INCLUDE_PATH = "src/main/resources/";
    public static final String INCLUDE_TAG_NAME = "include";

    public static void main(String[] argv) {

        final File outputFile = new File(OUTPUT_PATH);

        try {
            final XMLInputFactory inFactory = XMLInputFactory.newInstance();
            final XMLStreamReader streamReader = inFactory.createXMLStreamReader(new FileInputStream(SOURCE_PATH));

            final XMLOutputFactory factory = XMLOutputFactory.newInstance();
            final XMLStreamWriter streamWriter = factory.createXMLStreamWriter(new FileWriter(outputFile));


            boolean flag = false;
            while (streamReader.hasNext()) {
                final int eventType = streamReader.next();
                //check if this include
                if (eventType == XMLStreamConstants.START_ELEMENT && streamReader.getName().toString().equalsIgnoreCase(INCLUDE_TAG_NAME)) {
                    flag = true;
                    //read file name
                    final String filenameValue = streamReader.getAttributeValue(0);
                    //read other file
                    final XMLStreamReader includedFileReader = inFactory.createXMLStreamReader(new FileInputStream(INCLUDE_PATH + filenameValue));
                    while (includedFileReader.hasNext()) {
                        int inclEventType = includedFileReader.next();
                        rewriteEvent(inclEventType, includedFileReader, streamWriter, true);
                    }
                } else if (eventType == XMLStreamConstants.END_ELEMENT && streamReader.getName().toString().equalsIgnoreCase(INCLUDE_TAG_NAME)) {
                    flag = false;
                } else if (!flag) {
                    rewriteEvent(eventType, streamReader, streamWriter, false);
                }
            }
            streamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void rewriteEvent(final int eventType, final XMLStreamReader reader, final XMLStreamWriter writer, final boolean skipStartAndEndDocument) throws XMLStreamException {
        switch (eventType) {
            case XMLStreamConstants.START_DOCUMENT: {
                if (!skipStartAndEndDocument) writer.writeStartDocument();
                break;
            }
            case XMLStreamConstants.START_ELEMENT: {
                writer.writeStartElement(reader.getLocalName());
                break;
            }
            case XMLStreamConstants.ATTRIBUTE: {
                writer.writeAttribute(reader.getAttributeLocalName(0), reader.getAttributeValue(0));
                break;
            }
            case XMLStreamConstants.CHARACTERS: {
                writer.writeCharacters(reader.getText());
                break;
            }
            case XMLStreamConstants.END_ELEMENT: {
                writer.writeEndElement();
                break;
            }
            case XMLStreamConstants.END_DOCUMENT: {
                if (!skipStartAndEndDocument) writer.writeEndDocument();
                break;
            }
            default: {
                System.out.println("Skipped type: " + eventType);
            }
        }
    }

}
