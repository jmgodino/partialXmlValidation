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
		Source schemaFile = new StreamSource(getFile(xsdPath));
		Schema schema = factory.newSchema(schemaFile);
		return schema.newValidator();
	}

	private InputStream getFile(String location) throws IOException {

		return new FileInputStream(location);

	}

	public static void main(String args[]) throws Exception {
		TestValidatorWithJaxb tv = new TestValidatorWithJaxb();

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new FileInputStream("./test.xml"));

		Validator validator2 = tv.initValidator("./test.xsd");
		long ini = System.currentTimeMillis();
		while (reader.hasNext()) {
			if (reader.isStartElement()) {
				if (reader.getName().getLocalPart().equals("address")) {
					validator2.validate(new StAXSource(reader));
				}
			}
			reader.next();
		}
		long fin = System.currentTimeMillis();
		System.out.println("Tiempo total validación por bloques: " + (fin - ini) + "ms");

		Validator validator3 = tv.initValidator("./test.xsd");
		XMLStreamReader reader3 = xmlInputFactory.createXMLStreamReader(new FileInputStream("./test.xml"));

		ini = System.currentTimeMillis();
		validator3.validate(new StAXSource(reader3));

		fin = System.currentTimeMillis();
		System.out.println("Tiempo total validación única: " + (fin - ini) + "ms");

	}

}
