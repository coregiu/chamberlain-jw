package com.eagle.ui.tablemodel;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

public class XSDRowDefine {

	@SuppressWarnings("unchecked")
	public static Vector<RowDefine> getRowdefine(String xsdString) {
		Vector<RowDefine> define = new Vector<RowDefine>();

		SAXReader reader = new SAXReader();
		Document doc;
		Namespace xsdNS = DocumentHelper.createNamespace("xs",
				"http://www.w3.org/2001/XMLSchema");
		QName schemaElement = DocumentHelper.createQName("element", xsdNS);

		try {
			doc = reader.read(new StringReader(xsdString));
			Node node = doc.selectSingleNode("//xs:element[@name=\"row\"]/xs:complexType/xs:sequence");
			if (node instanceof Element) {
				Iterator<Element> rowSeq = ((Element) node).elementIterator();
				while (rowSeq.hasNext()) {
					Element field = rowSeq.next();
					if (field.getQName().equals(schemaElement)) {
						Element fieldEle = null;

						if (field.attributeValue("name") != null) {
							fieldEle = field;
						} else {
							String ref = field.attributeValue("ref");
							if (ref != null) {
								String fieldName = "//xs:element[@name=\""
										+ ref + "\"]";
								Node fieldNode = doc
										.selectSingleNode(fieldName);
								if (fieldNode instanceof Element) {
									fieldEle = (Element) fieldNode;
								}
							}
						}
						RowDefine r = getDefineValue(fieldEle, define.size());
						if (r != null)
							define.add(r);
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
		return define;
	}

	static RowDefine getDefineValue(Element field, int i) {
		String colName = field.attributeValue("name");
		String ann = colName;
		Node n = field.selectSingleNode("xs:annotation/xs:documentation");
		if (n != null)
			ann = n.getText();
		String type = field.attributeValue("type");
		QName typeV;
		if (type != null) {
			typeV = field.getQName(type);
		} else {
			typeV = new QName("xs:string");
		}

		String fixed = field.attributeValue("fixed");
		boolean fixedV = true;
		if (fixed != null) {
			if (fixed.equalsIgnoreCase("false"))
				fixedV = false;
		}

		RowDefine r = new RowDefine(colName, ann, i, typeV.getName(), fixedV);

		Node maxLenEle = field.selectSingleNode("xs:restriction/xs:maxLength");
		if (maxLenEle != null)
			r.setMaxLen(((Element) maxLenEle).attributeValue("value"));

		Node fracDigEle = field
				.selectSingleNode("xs:restriction/xs:fractionDigits");
		if (fracDigEle != null) {
			Element ele = (Element) field.selectSingleNode("xs:restriction");
			type = ele.attributeValue("type");
			typeV = ele.getQName(type);
			r.setDataType(typeV.getName());
			r.setFracDig(((Element) fracDigEle).attributeValue("value"));
		}

		return r;
	}
}
