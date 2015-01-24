/*
 * Copyright (c) 2006 Sun Microsystems, Inc.  All rights reserved.  U.S.
 * Government Rights - Commercial software.  Government users are subject
 * to the Sun Microsystems, Inc. standard license agreement and
 * applicable provisions of the FAR and its supplements.  Use is subject
 * to license terms.
 *
 * This distribution may include materials developed by third parties.
 * Sun, Sun Microsystems, the Sun logo, Java and J2EE are trademarks
 * or registered trademarks of Sun Microsystems, Inc. in the U.S. and
 * other countries.
 *
 * Copyright (c) 2006 Sun Microsystems, Inc. Tous droits reserves.
 *
 * Droits du gouvernement americain, utilisateurs gouvernementaux - logiciel
 * commercial. Les utilisateurs gouvernementaux sont soumis au contrat de
 * licence standard de Sun Microsystems, Inc., ainsi qu'aux dispositions
 * en vigueur de la FAR (Federal Acquisition Regulations) et des
 * supplements a celles-ci.  Distribue par des licences qui en
 * restreignent l'utilisation.
 *
 * Cette distribution peut comprendre des composants developpes par des
 * tierces parties. Sun, Sun Microsystems, le logo Sun, Java et J2EE
 * sont des marques de fabrique ou des marques deposees de Sun
 * Microsystems, Inc. aux Etats-Unis et dans d'autres pays.
 */

import jaxb.Pliki;
import jaxb.Zawartosc;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class App {

    private static final String RESOURCES_DIR = "src/main/resources/";

    public static void main(String[] argv)
    {
        try {

            File inFile = new File(RESOURCES_DIR + "in.xml");
            File outputFile = new File(RESOURCES_DIR + "out.xml");

            JAXBContext jaxbContext = null;

            jaxbContext = JAXBContext.newInstance(Pliki.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            Pliki pliki = (Pliki) jaxbUnmarshaller.unmarshal(inFile);

            int i = 0;
            for (Zawartosc zawartosc : pliki.getZawartosc()) {

                File includedFile = new File(RESOURCES_DIR + zawartosc.getPlik());
                Zawartosc includedZawartosc = (Zawartosc) jaxbUnmarshaller.unmarshal(includedFile);

                pliki.getZawartosc().set(i, includedZawartosc);

                i++;
            }
            jaxbMarshaller.marshal(pliki, outputFile);
            System.out.println(pliki);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
