package com.agh.xmlj;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import java.io.File;

/**
 * Created by rafal on 1/10/15.
 */
public class Start2 {

    public static void main(String[] args) {

        try {

            File fXmlFile = new File("src/main/resources/main.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("include");

            while(nList.getLength() > 0) {
                Node currentNode = nList.item(0);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) currentNode;
                    final String filename = eElement.getAttribute("filename");
                    System.out.println("Filename to include: " + filename);
                    Node parent = currentNode.getParentNode();

                    //parse included doc
                    Document includedDoc = dBuilder.parse(new File("src/main/resources/" + filename));
                    includedDoc.getDocumentElement().normalize();
                    Node parentNodeToInclude = includedDoc.getDocumentElement();

                    //import
                    Node imported = doc.importNode(parentNodeToInclude, true);
                    parent.replaceChild(imported, currentNode);
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/main/resources/result.xml"));
            transformer.transform(source, result);
            System.out.println("Done");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
