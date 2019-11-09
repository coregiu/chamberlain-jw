package com.eagle.ui.tablemodel;

import java.io.*;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import org.dom4j.*;
import org.dom4j.io.*;

import com.eagle.util.XMLUtil;

public class XMLTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 4478382949735093130L;

	public XMLTableModel(String headerXml, String bodyXml) {
		columnNames = new Vector<String>();
		datax = new Vector<Vector>();
		setHeader(headerXml);
		setTableData(bodyXml);
	}
	
	Document document;

	Vector<String> columnNames;

	Vector<Vector> datax;

	Vector<RowDefine> header;

	public static <M,I> RowFilter<M,I> regexFilter(String text)
	{
		return RowFilter.regexFilter(text);
	}
	
	Document getDocument(BufferedReader xmlString) {
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(xmlString);
			return document;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	void fillTableData() {
		List l = document.selectNodes("//rows/row");

		for (Iterator i = l.iterator(); i.hasNext();) {
			Element row = (Element) i.next();
			Vector<Object> temp = new Vector<Object>(header.size() + 1);
			for (int j = 0; j < header.size() - 1; j++) {
				
				temp.add(XMLUtil.getDefaultElementData(header.get(j).getDataType()));
			}
			temp.add(row);
			for (int j = 0, size = row.nodeCount(); j < size; j++) {
				Node node = row.node(j);
				if (node instanceof Element) {
					Element e = (Element) node;
					String s = e.getTextTrim();

					int i1 = getPos(e.getName());

					if (i1 >= 0) {
						RowDefine r = header.get(i1);
						Object o = XMLUtil.getElementData(r.getDataType(), s);
						temp.setElementAt(o, i1);
					}
				}
			}
			datax.add(temp);
		}
	}

	public int getPos(String colname) {
		for (int i = 0; i < header.size(); i++) {
			if (header.get(i).getColname().equalsIgnoreCase(colname)) {
				return i;
			}
		}
		return -1;
	}

	void setTableData(String bodyXml) {
		BufferedReader br;
		br = new BufferedReader(new StringReader(bodyXml));
		document = getDocument(br);
		fillTableData();
	}

	void setHeader(String headerXml) {
		// BufferedReader br;
		// br = new BufferedReader(new StringReader(headerXml));
		// header = XSDTree.getRowdefine(br);

		header = XSDRowDefine.getRowdefine(headerXml);
		for (int i = 0; i < header.size(); i++) {
			RowDefine r = header.get(i);
			columnNames.add(r.getAnnotation());
		}
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		return datax.size();
	}

	public String getColumnName(int col) {
		return (String) columnNames.get(col);
	}

	public Object getValueAt(int row, int col) {
		Vector temp = (Vector) datax.get(row);
		Object o = temp.get(col);

		if (o != null) {
			if (o instanceof Timestamp) {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String s = sf.format(o);
				return s;
			} else if (o instanceof Double) {
				RowDefine r = header.get(col);
				if (r.getFracDig() != 0) {
					String d = r.getFracDigStr();
					try
					{
					   DecimalFormat df = new DecimalFormat("." + d);
					   return df.format(o);
					}
					catch (Exception e)
					{
					   e.printStackTrace();	
					}
					return "0.0";
				} else
					return o;
			}
		} else
			o = "";
		return o;
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	public Class<?> getColumnClass(int c) {
		/*
		if (header.get(c).getFixed()) {
			return String.class;
		} else {
			Object o = getValueAt(0, c);
			return o.getClass();
		}*/
		Object o = getValueAt(0, c);				
		if (o == null)
		   return String.class;
		else
		   return o.getClass();
		// return String.class;
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (header.get(col).getFixed()) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public void setValueAt(Object value, int row, int col) {
		Vector data = datax.get(row);
		if (data.get(col) instanceof Integer && !(value instanceof Integer)) {
			try {
				data.set(col, new Integer(value.toString()));
				fireTableCellUpdated(row, col);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
			}
		} else {
			data.set(col, value);
			fireTableCellUpdated(row, col);
		}
	}
	
	public Element getRowElement(int row) {
		Vector data = datax.get(row);
		int size = data.size();
		return (Element) data.get(size - 1);
	}
}
