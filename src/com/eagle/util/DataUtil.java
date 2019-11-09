package com.eagle.util;

import java.util.*;
import javax.swing.*;
import com.eagle.xmlparse.XMLParser;

public class DataUtil {
	/**
	 * 设置收入信息类型的列表
	 * */
	public static void setIType(JComboBox box){
		Vector<String> iList = XMLParser.getIODataType('i');
		for(int i=0; i<iList.size(); i++){
			box.addItem(iList.get(i));
		}
	}
	
	/**
	 * 设置支出信息类型的列表
	 * */
	public static void setOType(JComboBox box){
		Vector<String> oList = XMLParser.getIODataType('o');
		for(int i=0; i<oList.size(); i++){
			box.addItem(oList.get(i));
		}
	}
}
