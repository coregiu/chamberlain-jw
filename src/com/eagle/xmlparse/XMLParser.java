package com.eagle.xmlparse;

import java.util.*;

import org.apache.log4j.Logger;
import org.dom4j.*;

import com.eagle.action.PlainAction;
import com.eagle.data.*;
import com.eagle.prop.NameProp;
import com.eagle.util.CoderUtil;
import com.eagle.util.FileUtil;
import com.eagle.util.XMLUtil;

/**
 * 解析XML数据
 * */
public class XMLParser{
	private static Logger log = Logger.getLogger(XMLParser.class);
	/**
	 * 解析用户信息列表
	 * */
	public static Hashtable<String, UserBean> getUserInfoList(Document doc){
		Hashtable<String, UserBean> userInfoList = new Hashtable<String, UserBean>();
		try{
			Element rootEle = doc.getRootElement();
			Iterator it = rootEle.elementIterator();
			while(it.hasNext()){
				UserBean userBean = new UserBean();
				Element aUser = (Element)it.next();
				userBean.setUsername(aUser.element("username").getTextTrim());
				userBean.setRealname(aUser.element("realname").getTextTrim());
				userBean.setPassword(aUser.element("password").getTextTrim());
				userBean.setFilename(aUser.element("filename").getTextTrim());
				userBean.setRelation(aUser.element("relation").getTextTrim());
				
				userInfoList.put(userBean.getUsername(), userBean);
			}
		}catch(Exception e){
			log.error(e);
		}
		return userInfoList;
	}
	/**
	 * 解析用户信息列表
	 * */
	public static Hashtable<String, UserBean> getUserInfoList(){
		Document doc = XMLUtil.getDocument(ConfBean.userInfoStr);
		return  getUserInfoList(doc);
	}
	/**
	 * 解析用户信息的SCHEMA
	 * */
	public static Vector<String> getUserInfoSchema(){
		Vector<String> userInfoSchema = new Vector<String>();
		return userInfoSchema;
	}
	
	/**
	 * 获取收入支出信息 i-收入 o-支出
	 * */
	@SuppressWarnings("unchecked")
	public static Hashtable<String, IODataBean> getIODataAsHash(Document doc, char _type){
		Hashtable<String, IODataBean> ioInfoList = new Hashtable<String, IODataBean>();
		try{
			Node node;
			if(_type =='i' ){
				node = doc.selectSingleNode("//ins");
				if (node instanceof Element) {
					Iterator<Element> rowSeq = ((Element) node).elementIterator();
					while (rowSeq.hasNext()) {
						IODataBean ioDataBean = new IODataBean();
						Element field = rowSeq.next();
						
						ioDataBean.setIId(field.element("iId").getTextTrim());
						ioDataBean.setIName(field.element("iName").getTextTrim());
						ioDataBean.setIAllMoney(field.element("iAllMoney").getTextTrim());
						ioDataBean.setICleanMoney(field.element("iCleanMoney").getTextTrim());
						ioDataBean.setIType(field.element("iType").getTextTrim());
						ioDataBean.setIResume(field.element("iResume").getTextTrim());
						ioDataBean.setIDate(field.element("iDate").getTextTrim());
						
						ioInfoList.put(ioDataBean.getIId(), ioDataBean);
					}
				}
			}else{
				node = doc.selectSingleNode("//outs");
				if (node instanceof Element) {
					Iterator<Element> rowSeq = ((Element) node).elementIterator();
					while (rowSeq.hasNext()) {
						IODataBean ioDataBean = new IODataBean();
						Element field = rowSeq.next();
						
						ioDataBean.setOId(field.element("oId").getTextTrim());
						ioDataBean.setOName(field.element("oName").getTextTrim());
						ioDataBean.setOMoney(field.element("oMoney").getTextTrim());
						ioDataBean.setOType(field.element("oType").getTextTrim());
						ioDataBean.setOResume(field.element("oResume").getTextTrim());
						ioDataBean.setODate(field.element("oDate").getTextTrim());
						
						ioInfoList.put(ioDataBean.getIId(), ioDataBean);
					}
				}
			}
			 
			
		}catch(Exception e){
			log.error(e);
		}
		return ioInfoList;
	}
	
	/**
	 * 获取收入支出信息 i-收入 o-支出
	 * */
	@SuppressWarnings("unchecked")
	public static Vector<Vector> getIODataAsVec(char _type){
		Document doc = XMLUtil.getDocument(getIOText());
		Vector<Vector> ioInfoList = new Vector<Vector>();
		LinkedList<Vector> list = new LinkedList<Vector>();
		try{
			Node node;
			if(_type =='i' ){
				node = doc.selectSingleNode("//ins");
				if (node instanceof Element) {
					Iterator<Element> rowSeq = ((Element) node).elementIterator();
					while (rowSeq.hasNext()) {
						Vector<String> userInfo = new Vector<String>();
						Element field = rowSeq.next();
						
						userInfo.add(field.element("iId").getTextTrim());
						userInfo.add(field.element("iName").getTextTrim());
						userInfo.add(field.element("iAllMoney").getTextTrim());
						userInfo.add(field.element("iCleanMoney").getTextTrim());
						userInfo.add(field.element("iType").getTextTrim());
						userInfo.add(field.element("iResume").getTextTrim());
						userInfo.add(field.element("iDate").getTextTrim());
						
						list.add(userInfo);
					}
				}
			}else{
				node = doc.selectSingleNode("//outs");
				if (node instanceof Element) {
					Iterator<Element> rowSeq = ((Element) node).elementIterator();
					while (rowSeq.hasNext()) {
						Vector<String> userInfo = new Vector<String>();
						Element field = rowSeq.next();
						
						userInfo.add(field.element("oId").getTextTrim());
						userInfo.add(field.element("oName").getTextTrim());
						userInfo.add(field.element("oMoney").getTextTrim());
						userInfo.add(field.element("oType").getTextTrim());
						userInfo.add(field.element("oResume").getTextTrim());
						userInfo.add(field.element("oDate").getTextTrim());
						
						list.add(userInfo);
					}
				}
			}
			// 重新排序
			for(int i = list.size()-1; i>=0 ; i--){
				ioInfoList.add(list.get(i));
			}
		}catch(Exception e){
			log.error(e);
		}
		return ioInfoList;
	}
	
	/**
	 * 获取收入支出的类型信息 i-收入 o-支出
	 * */
	@SuppressWarnings("unchecked")
	public static Vector<String> getIODataType(char _type){
		Document doc = XMLUtil.getDocument(getIOText());
		Vector<String> ioInfoList = new Vector<String>();
		try{
			List node;
			if(_type =='i' ){
				node = doc.selectNodes("//ins/in/iType");
				for(int i=0;i<node.size();i++){
					Element aIOData = (Element)node.get(i);
					String iInfo = aIOData.getTextTrim();
					boolean flag = true;
					for(int j=0; j<ioInfoList.size(); j++){
						if(iInfo.equals(ioInfoList.get(j))){
							flag = false;
							break;
						}
					}
					if(flag){
						ioInfoList.add(iInfo);
					}else{
						continue;
					}
				}
			}else{
				node = doc.selectNodes("//outs/out/oType");
				for(int i=0;i<node.size();i++){
					Element aIOData = (Element)node.get(i);
					String oInfo = aIOData.getTextTrim();
					boolean flag = true;
					for(int j=0; j<ioInfoList.size(); j++){
						if(oInfo.equals(ioInfoList.get(j))){
							flag = false;
							break;
						}
					}
					if(flag){
						ioInfoList.add(oInfo);
					}else{
						continue;
					}
				}
			}	
		}catch(Exception e){
			log.error(e);
		}
		return ioInfoList;
	}
	
	/**
	 * 获取日记目录数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Hashtable<String, Hashtable<String,Hashtable<String, Vector<String>>>> getDiaryMenuAsVec(){
		Hashtable<String, Hashtable<String,Hashtable<String, Vector<String>>>> diaryHash = 
			new Hashtable<String, Hashtable<String,Hashtable<String, Vector<String>>>>();
		String diaryData = getDiaryText();
		//log.info(diaryData);
		Document doc = XMLUtil.getDocument(diaryData);
		try{
			List nodeList = doc.selectNodes("//PrivateData/Diary/Time");
			Iterator<Element> rowSeq;
			
			String timeNode;
			Vector<String> nameVec;
			String nameStr;
			
			Hashtable<String,Hashtable<String, Vector<String>>> monthHash;
			Hashtable<String, Vector<String>> dayHash;
			String dayArr[];
			Element node;
			for(int i=0;i<nodeList.size();i++){
				node = (Element)nodeList.get(i);
				timeNode = node.attribute("id").getText();
				rowSeq = node.elementIterator();
				nameVec = new Vector<String>();
				// 日记名
				while (rowSeq.hasNext()) {
					nameStr = ((Element)rowSeq.next()).attribute("id").getText();
					nameVec.add(nameStr);
				}
				// 年月日三级目录
				dayArr = timeNode.split("-");
				monthHash = diaryHash.get(dayArr[0]);
				if(monthHash==null){
					dayHash = new Hashtable<String, Vector<String>>();
					dayHash.put(dayArr[2], nameVec);
					monthHash = new Hashtable<String,Hashtable<String, Vector<String>>>();
					monthHash.put(dayArr[1], dayHash);
				}else{
					dayHash = monthHash.get(dayArr[1]);
					if(dayHash==null){
						dayHash = new Hashtable<String, Vector<String>>();
					}
					dayHash.put(dayArr[2], nameVec);
					monthHash.put(dayArr[1], dayHash);
				}
				diaryHash.put(dayArr[0], monthHash);
			}
		}catch(Exception e){
			log.error(e);
		}
		return diaryHash;
	}
	
	/**
	 * 获取日记内容
	 * @param date
	 * @param item
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String getDiaryData(String date, String item){
		String diaryData = getDiaryText();
		Document doc = XMLUtil.getDocument(diaryData);
		String content = null;
		try{
			List nodeList = doc.selectNodes("//PrivateData/Diary/Time");
			Iterator<Element> rowSeq;
			
			Element node;
			String nodeTimeStr;
			Element rowEle;
			for(int i=0;i<nodeList.size();i++){
				node = (Element)nodeList.get(i);
				nodeTimeStr = node.attribute("id").getText();
				if(date.equals(nodeTimeStr)){
					rowSeq = node.elementIterator();
					while(rowSeq.hasNext()){
						rowEle = (Element)rowSeq.next();
						if(item.equals(rowEle.attribute("id").getText())){
							content = rowEle.getTextTrim();
							break;
						}
					}
				}
			}
		}catch(Exception e){
			log.error(e);
		}
		return content;
	}
	
	/**
	 * 获取工作学习计划目录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Hashtable<String, Hashtable<String, Vector<String>>> getPlainMenuAsVec(){
		Hashtable<String, Hashtable<String,Vector<String>>> plainHash = 
			new Hashtable<String, Hashtable<String,Vector<String>>>();
		String plainData = getPlainText();
		Document doc = XMLUtil.getDocument(plainData);
		try{
			Iterator<Element> rowSeq = doc.getRootElement().elementIterator();
			
			String timeNode;
			Vector<String> dayVec;
			
			Hashtable<String, Vector<String>> monthHash;
			String dayArr[];
			Element node;
			String year;
			while(rowSeq.hasNext()){
				node = (Element)rowSeq.next();
				timeNode = node.getName();
				
				// 年月日三级目录
				dayArr = timeNode.split("-");
				year = dayArr[0].substring(1);
				monthHash = plainHash.get(year);
				if(monthHash==null){
					dayVec = new Vector<String>();
					dayVec.add(dayArr[2]);
					monthHash = new Hashtable<String,Vector<String>>();
					monthHash.put(dayArr[1], dayVec);
				}else{
					dayVec = monthHash.get(dayArr[1]);
					if(dayVec==null){
						dayVec = new Vector<String>();
					}
					dayVec.add(dayArr[2]);
					monthHash.put(dayArr[1], dayVec);
				}
				plainHash.put(year, monthHash);
			}
		}catch(Exception e){
			log.error(e);
		}
		return plainHash;
	}
	
	/**
	 * 获取计划内容
	 * 
	 * @param date
	 * @return
	 */
	public static Vector<Vector<String>> getPlainAsVec(String date){
		Vector<Vector<String>> plainVec = new Vector<Vector<String>>();
		try{
			String plainData = getPlainText();
			Document doc = XMLUtil.getDocument(plainData);
			Element ele = doc.getRootElement().element(date);
			if(ele!=null){
				Iterator it = ele.elementIterator();
				Element aPlain;
				
				Vector<String> aPlainVec;
				while(it.hasNext()){
					aPlain = (Element)it.next();
					aPlainVec = new Vector<String>();
					aPlainVec.add(aPlain.element("id").getTextTrim());
					aPlainVec.add(aPlain.element("title").getTextTrim());
					aPlainVec.add(aPlain.element("content").getTextTrim().replaceAll(PlainAction.flag, "\n"));
					aPlainVec.add(aPlain.element("completed").getTextTrim());
					plainVec.add(aPlainVec);
				}
			}
		}catch(Exception e){
			log.error(e);
		}
		return plainVec;
	}
	/**
	 * 获取整个日记内容
	 * @return
	 */
	public static String getDiaryText(){
		return new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, FileUtil.getFileAsString(ConfBean.diaryFilePath)));
	}
	
	/**
	 * 获取整个收支内容
	 * @return
	 */
	public static String getIOText(){
		return new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, FileUtil.getFileAsString(ConfBean.ioDataFilePath)));
	}
	
	/**
	 * 获取整个工作计划内容
	 * @return
	 */
	public static String getPlainText(){
		return new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, FileUtil.getFileAsString(ConfBean.plainFilePath)));
	}
	
	/**
	 * 获取扩展数据
	 * @return
	 */
	public static String getExtText(){
		return new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, FileUtil.getFileAsString(ConfBean.extFilePath)));
	}
	
	/**
	 * 获取扩展数据
	 * @return
	 */
	public static Element getExtText(String extInfo) throws Exception{
		String extStr = getExtText();
		Document doc = XMLUtil.getDocument(extStr);
		return doc.getRootElement().element(extInfo);
	}
}
