package com.eagle.util;

import java.io.*;
import java.sql.Timestamp;

import org.xml.sax.SAXException;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.*;
import org.dom4j.io.*;
import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;

/**
 * @author szhang
 * XML操作
 * */
public class XMLUtil {
	private static Log log = LogFactory.getLog(XMLUtil.class);

	/**
	 * 
	 * */
	public static String createXmlFile(File file, Document doc)
	{
		OutputFormat format = OutputFormat.createPrettyPrint(); // ����XML�ĵ�����ʽ
		format.setEncoding("UTF-8"); // ����XML�ĵ��ı�������
		format.setTrimText(true);
		format.setSuppressDeclaration(false);
		format.setIndent(true); // �����Ƿ����
		format.setIndent(" "); // �Կո�ʽʵ�����
		format.setNewlines(true); // �����Ƿ���
		try {
			FileWriter result = new FileWriter(file);
			XMLWriter xmlWriter = new XMLWriter(result, format);
			//xmlWriter.startDocument();
			xmlWriter.write(doc);
			xmlWriter.close();
			return result.toString();
		}	
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *
	 * @param org.dom4j.Document
	 * @return String
	 * */
	public static String createXmlString(Document doc)
	{
		OutputFormat format = OutputFormat.createPrettyPrint(); // ����XML�ĵ�����ʽ
		format.setEncoding("UTF-8"); // ����XML�ĵ��ı�������
		format.setTrimText(true);
		format.setSuppressDeclaration(false);
		format.setIndent(true); // �����Ƿ����
		format.setIndent(" "); // �Կո�ʽʵ�����
		format.setNewlines(true); // �����Ƿ���
		StringWriter result = new StringWriter();
		XMLWriter xmlWriter = new XMLWriter(result, format);
		try {
			//xmlWriter.startDocument();
			xmlWriter.write(doc);
			xmlWriter.close();
			return result.toString();
		}	
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param java.io.BufferedReader
	 * @return org.dom4j.Document
	 * */
	public static Document getDocument(BufferedReader xmlStringBuffer) {
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(xmlStringBuffer);
			return document;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * ����
	 * @param String
	 * @return org.dom4j.Document
	 * */
	public static Document getDocument(String xmlString) {
		Reader reader=new StringReader(xmlString);
		BufferedReader buffer=new BufferedReader(reader);
		return getDocument(buffer);
	}
	/**
	 * ���SCHEMA���XML�Ƿ�Ϸ�
	 * @param schema
	 * @param xml
	 * @return true/false
	 * */
	public static boolean checkXml(String xmlSchema, String xmlDoc) {
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");        
        javax.xml.validation.Schema schema;
		try {
			schema = factory.newSchema(new StreamSource(new StringReader(xmlSchema)));
	        Validator validator = schema.newValidator();
	        Source source = new StreamSource(new StringReader(xmlDoc));
	        try {
	            validator.validate(source);
	            return true;
	        }
	        catch (SAXException ex) {
	            log.info(ex.getMessage());
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return false;
    }
	
	// tablemodel中用
	public static Object getElementData(String dataType, String dataValue){
		if (dataType.equalsIgnoreCase("boolean")){
			return Boolean.valueOf(dataValue);
		}else if (dataType.equalsIgnoreCase("dateTime")){
			javax.xml.datatype.DatatypeFactory d = new DatatypeFactoryImpl();
			XMLGregorianCalendar xmldate = d.newXMLGregorianCalendar(dataValue);
			
			Timestamp date = new Timestamp(xmldate.toGregorianCalendar().getTimeInMillis());
			return date;
		}else if (dataType.equalsIgnoreCase("decimal")){
			return Double.valueOf(dataValue);
		}	
		return dataValue;
	}
	
	// tablemodel中用
	public static Object getDefaultElementData(String dataType){
		if (dataType.equalsIgnoreCase("boolean")){
			String dataValue = "true";
			return Boolean.valueOf(dataValue);
		}else if (dataType.equalsIgnoreCase("dateTime")){
			String dataValue = "1970-01-01";
			
			javax.xml.datatype.DatatypeFactory d = new DatatypeFactoryImpl();
			XMLGregorianCalendar xmldate = d.newXMLGregorianCalendar(dataValue);
			
			Timestamp date = new Timestamp(xmldate.toGregorianCalendar().getTimeInMillis());
			return date;
		}else if (dataType.equalsIgnoreCase("decimal")){
			String dataValue = "0";
			return Double.valueOf(dataValue);
		}	
		return "";
	}
}
