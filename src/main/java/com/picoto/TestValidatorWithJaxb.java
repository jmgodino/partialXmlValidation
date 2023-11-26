package com.picoto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class TestValidatorWithJaxb {

	private Validator initValidator(String xsdPath) throws SAXException, IOException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Source schemaSource = new StreamSource(getFile(xsdPath));
		Schema schema = factory.newSchema(schemaSource);
		return schema.newValidator();
	}

	private InputStream getFile(String location) throws IOException {
		return new FileInputStream(location);
	}

	public static void main(String args[]) throws Exception {
		TestValidatorWithJaxb tv = new TestValidatorWithJaxb();

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader reader1 = xmlInputFactory.createXMLStreamReader(new FileInputStream("./test.xml"));

		Validator validator1 = tv.initValidator("./test.xsd");
		long ini = getCurrentTime();
		while (reader1.hasNext()) {
			if (reader1.isStartElement()) {
				if (reader1.getName().getLocalPart().equals("address")) {
					validator1.validate(new StAXSource(reader1));
				}
			}
			reader1.next();
		}
		long fin = getCurrentTime();
		debug("Tiempo total validación por bloques: " + (fin - ini) + "ms");

		Validator validator2 = tv.initValidator("./test.xsd");
		XMLStreamReader reader2 = xmlInputFactory.createXMLStreamReader(new FileInputStream("./test.xml"));

		ini = getCurrentTime();
		validator2.validate(new StAXSource(reader2));

		fin = getCurrentTime();
		debug("Tiempo total validación única: " + (fin - ini) + "ms");

	}

	private static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	private static void debug(String string) {
		System.out.println(string);
	}

}
