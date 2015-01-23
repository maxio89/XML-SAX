package com.agh.xmlj;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by rafal on 1/10/15.
 */
public class DOM {

    public static void main(String[] args) {

        try {
            final File fXmlFile = new File("src/main/resources/main.xml");
            final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            final Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            final NodeList nList = doc.getElementsByTagName("include");

            while (nList.getLength() > 0) {
                final Node currentNode = nList.item(0);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element eElement = (Element) currentNode;
                    final String filename = eElement.getAttribute("filename");
                    System.out.println("Filename to include: " + filename);
                    final Node parent = currentNode.getParentNode();

                    //parse included doc
                    final Document includedDoc = dBuilder.parse(new File("src/main/resources/" + filename));
                    includedDoc.getDocumentElement().normalize();
                    final Node parentNodeToInclude = includedDoc.getDocumentElement();

                    //import
                    final Node imported = doc.importNode(parentNodeToInclude, true);
                    parent.replaceChild(imported, currentNode);
                }
            }

            // write the content into xml file
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMSource source = new DOMSource(doc);
            final StreamResult result = new StreamResult(new File("src/main/resources/result.xml"));
            transformer.transform(source, result);
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
