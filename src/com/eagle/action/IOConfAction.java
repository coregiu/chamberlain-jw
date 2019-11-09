package com.eagle.action;

import java.util.*;

import org.apache.commons.logging.*;
import org.dom4j.*;

import com.eagle.data.ConfBean;
import com.eagle.data.IODataBean;
import com.eagle.util.CoderUtil;
import com.eagle.util.FileUtil;
import com.eagle.util.XMLUtil;
import com.eagle.xmlparse.XMLParser;

/**
 * 收支信息管理
 * */
public class IOConfAction {
	private Log log = LogFactory.getLog(IOConfAction.class);
	
	/**
	 * 获取收入信息的表头信息
	 * */
	public Vector<String> getITHead(){
		Vector<String> iTHead = new Vector<String>();
		iTHead.add("收入编号");
		iTHead.add("收入名称");
		iTHead.add("收入总额");
		iTHead.add("纯收入额");
		iTHead.add("收入类型");
		iTHead.add("备注");
		iTHead.add("时间");
		return iTHead;
	}
	
	/**
	 * 获取支出信息的表头信息
	 * */
	public Vector<String> getOTHead(){
		Vector<String> oTHead = new Vector<String>();
		oTHead.add("支出编号");
		oTHead.add("支出名称");
		oTHead.add("支出金额");
		oTHead.add("支出类型");
		oTHead.add("备注");
		oTHead.add("时间");
		return oTHead;
	}
	
	/**
	 * 获取收入信息
	 * */
	public Vector<Vector> getIData(){
		return XMLParser.getIODataAsVec('i');
	}
	
	/**
	 * 获取支入信息
	 * */
	public Vector<Vector> getOData(){
		return XMLParser.getIODataAsVec('o');
	}
	
	/**
	 * 添加收入信息
	 * */
	public boolean addIInfo(IODataBean ioDataBean){
		Document doc = XMLUtil.getDocument(XMLParser.getIOText());
		Element rootEle = doc.getRootElement();
		try{
			/*Iterator it = rootEle.elementIterator();
			while(it.hasNext()){
				Element aIOData = (Element)it.next();
				if(aIOData.element("iId").getTextTrim().equals(ioDataBean.getIId())){
					return false;
				}
			}*/
			Document iDataDoc = DocumentHelper.createDocument();
			iDataDoc.addElement("in");
			Element userEle = iDataDoc.getRootElement();
			
			userEle.addElement("iId");
			userEle.element("iId").setText(ioDataBean.getIId());			
			userEle.addElement("iName");
			userEle.element("iName").setText(ioDataBean.getIName());
			userEle.addElement("iAllMoney");
			userEle.element("iAllMoney").setText(ioDataBean.getIAllMoney());
			userEle.addElement("iCleanMoney");
			userEle.element("iCleanMoney").setText(ioDataBean.getICleanMoney());
			userEle.addElement("iType");
			userEle.element("iType").setText(ioDataBean.getIType());
			userEle.addElement("iResume");
			userEle.element("iResume").setText(ioDataBean.getIResume());
			userEle.addElement("iDate");
			userEle.element("iDate").setText(ioDataBean.getIDate());
			
			rootEle.element("ins").add(iDataDoc.getRootElement());
			String ioDataStr = XMLUtil.createXmlString(doc);
			
			FileUtil.save(ConfBean.ioDataFilePath, CoderUtil.encode(ioDataStr, false));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 添加支出信息
	 * */
	public boolean addOInfo(IODataBean ioDataBean){
		Document doc = XMLUtil.getDocument(XMLParser.getIOText());
		Element rootEle = doc.getRootElement();
		try{
			Document iDataDoc = DocumentHelper.createDocument();
			iDataDoc.addElement("out");
			Element userEle = iDataDoc.getRootElement();
			
			userEle.addElement("oId");
			userEle.element("oId").setText(ioDataBean.getOId());			
			userEle.addElement("oName");
			userEle.element("oName").setText(ioDataBean.getOName());
			userEle.addElement("oMoney");
			userEle.element("oMoney").setText(ioDataBean.getOMoney());
			userEle.addElement("oType");
			userEle.element("oType").setText(ioDataBean.getOType());
			userEle.addElement("oResume");
			userEle.element("oResume").setText(ioDataBean.getOResume());
			userEle.addElement("oDate");
			userEle.element("oDate").setText(ioDataBean.getODate());
			
			rootEle.element("outs").add(iDataDoc.getRootElement());
			String ioDataStr = XMLUtil.createXmlString(doc);
			
			FileUtil.save(ConfBean.ioDataFilePath,CoderUtil.encode(ioDataStr, false));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 修改收入信息
	 * */
	@SuppressWarnings("unchecked")
	public boolean updateIInfo(IODataBean ioDataBean){
		Document doc = XMLUtil.getDocument(XMLParser.getIOText());
		try{
			Node node = doc.selectSingleNode("//ins");
			if (node instanceof Element) {
				Iterator<Element> rowSeq = ((Element) node).elementIterator();
				while(rowSeq.hasNext()){
					Element aIOData = (Element)rowSeq.next();
					if(aIOData.element("iId").getTextTrim().equals(ioDataBean.getIId())){
						aIOData.element("iName").setText(ioDataBean.getIName());
						aIOData.element("iAllMoney").setText(ioDataBean.getIAllMoney());
						aIOData.element("iCleanMoney").setText(ioDataBean.getICleanMoney());
						aIOData.element("iType").setText(ioDataBean.getIType());
						aIOData.element("iResume").setText(ioDataBean.getIResume());
						aIOData.element("iDate").setText(ioDataBean.getIDate());
						break;
					}
				}
			}
			String ioDataStr = XMLUtil.createXmlString(doc);
			
			FileUtil.save(ConfBean.ioDataFilePath,CoderUtil.encode(ioDataStr, false));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 修改支出信息
	 * */
	@SuppressWarnings("unchecked")
	public boolean updateOInfo(IODataBean ioDataBean){
		Document doc = XMLUtil.getDocument(XMLParser.getIOText());
		try{
			Node node = doc.selectSingleNode("//outs");
			if (node instanceof Element) {
				Iterator<Element> rowSeq = ((Element) node).elementIterator();
				while(rowSeq.hasNext()){
					Element aIOData = (Element)rowSeq.next();
					if(aIOData.element("oId").getTextTrim().equals(ioDataBean.getOId())){
						aIOData.element("oName").setText(ioDataBean.getOName());
						aIOData.element("oMoney").setText(ioDataBean.getOMoney());
						aIOData.element("oType").setText(ioDataBean.getOType());
						aIOData.element("oResume").setText(ioDataBean.getOResume());
						aIOData.element("oDate").setText(ioDataBean.getODate());
						break;
					}
				}
			}
			
			String ioDataStr = XMLUtil.createXmlString(doc);
			
			FileUtil.save(ConfBean.ioDataFilePath,CoderUtil.encode(ioDataStr, false));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 删除收入信息
	 * */
	@SuppressWarnings("unchecked")
	public boolean deleteIInfo(IODataBean ioDataBean){
		Document doc = XMLUtil.getDocument(XMLParser.getIOText());
		log.info(XMLParser.getIOText());
		Element rootEle = doc.getRootElement();
		try{
			// 先删除该数据
			Node node = doc.selectSingleNode("//ins");
			if (node instanceof Element) {
				Iterator<Element> rowSeq = ((Element) node).elementIterator();
				while(rowSeq.hasNext()){
					Element aIOData = (Element)rowSeq.next();
					if(aIOData.element("iId").getTextTrim().equals(ioDataBean.getIId())){
						rootEle.element("ins").remove(aIOData);
						break;
					}
				}
			}
			
			String ioDataStr = XMLUtil.createXmlString(doc);
			
			FileUtil.save(ConfBean.ioDataFilePath,CoderUtil.encode(ioDataStr, false));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 删除支出信息
	 * */
	@SuppressWarnings("unchecked")
	public boolean deleteOInfo(IODataBean ioDataBean){
		Document doc = XMLUtil.getDocument(XMLParser.getIOText());
		Element rootEle = doc.getRootElement();
		try{
			// 先删除该数据
			Node node = doc.selectSingleNode("//outs");
			if (node instanceof Element) {
				Iterator<Element> rowSeq = ((Element) node).elementIterator();
				while(rowSeq.hasNext()){
					Element aIOData = (Element)rowSeq.next();
					if(aIOData.element("oId").getTextTrim().equals(ioDataBean.getOId())){
						rootEle.element("outs").remove(aIOData);
						break;
					}
				}
			}
			
			String ioDataStr = XMLUtil.createXmlString(doc);
			
			FileUtil.save(ConfBean.ioDataFilePath,CoderUtil.encode(ioDataStr, false));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
