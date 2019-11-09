package com.eagle.action;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.eagle.data.ConfBean;
import com.eagle.data.ImageData;
import com.eagle.data.PlainData;
import com.eagle.util.CoderUtil;
import com.eagle.util.FileUtil;
import com.eagle.util.StringUtil;
import com.eagle.util.XMLUtil;
import com.eagle.xmlparse.XMLParser;
import com.eagle.ui.plain.IconNode;

/**
 * 工作学习计划
 * @author Administrator
 *
 */
public class PlainAction {
	private Logger log = Logger.getLogger(PlainAction.class);
	private Hashtable<String, Hashtable<String, Vector<String>>> menuData;
	public PlainAction(){
		
	}
	
	/**
	 * 获取学习工作计划的表头信息
	 * 
	 * @return
	 */
	public Vector<String> getPlainHead(){
		Vector<String> iTHead = new Vector<String>();
		iTHead.add("编号");
		iTHead.add("标题");
		iTHead.add("内容");
		iTHead.add("是否完成");
		return iTHead;
	}
	
	/**
	 * 获取学习工作计划的表头信息
	 * 
	 * @return
	 */
	public Vector<String> getExcelPlainHead(){
		Vector<String> iTHead = new Vector<String>();
		iTHead.add("日期");
		iTHead.add("编号");
		iTHead.add("标题");
		iTHead.add("内容");
		iTHead.add("是否完成");
		return iTHead;
	}
	
	public boolean checkToday(PlainData currentPlainData){
		try{
		String date = "p"+currentPlainData.getDate();
		String plainData = XMLParser.getPlainText();
		Document doc = XMLUtil.getDocument(plainData);
		Element rootEle = doc.getRootElement();
		if(!isExists(rootEle, date)){
			newPlainDay(currentPlainData.getDate());
		}
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Vector> getExportData(String date){
		Vector<Vector> plainDataVec = new Vector<Vector>();
		try{
			date = "p"+date;
			String plainData = XMLParser.getPlainText();
			Document doc = XMLUtil.getDocument(plainData);
			Element rootEle = doc.getRootElement();
			Iterator<Element> it = rootEle.elementIterator();
			Element aPlainDay;
			Element aPlainEle;
			Vector<String> aPlainVec ; 
			Iterator<Element> aPlainIt;
			while(it.hasNext()){
				aPlainDay = it.next();
				if(aPlainDay.getName().indexOf(date)!=-1){
					aPlainIt = aPlainDay.elementIterator();
					while(aPlainIt.hasNext()){
						aPlainEle = aPlainIt.next();
						aPlainVec = new Vector<String>();
						aPlainVec.add(aPlainDay.getName().substring(1));
						aPlainVec.add(aPlainEle.element("id").getTextTrim());
						aPlainVec.add(aPlainEle.element("title").getTextTrim());
						aPlainVec.add(aPlainEle.element("content").getTextTrim().replaceAll(flag, "\n"));
						aPlainVec.add(aPlainEle.element("completed").getTextTrim());
						plainDataVec.add(aPlainVec);
					}
				}
			}
		}catch(Exception e){
			log.error(e);
		}
		return plainDataVec;
	}
	
	/**
	 * 获取学习工作计划的内容
	 * 
	 * @param date
	 * @return
	 */
	public Vector<Vector<String>> getPlainData(String date){
		date = "p"+date;
		return XMLParser.getPlainAsVec(date);
	}
	
	/**
	 * 新建计划日期
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public boolean newPlainDay(String date){
		try{
			date = "p"+date;
			String plainData = XMLParser.getPlainText();
			Document doc = XMLUtil.getDocument(plainData);
			Element rootEle = doc.getRootElement();
			if(isExists(rootEle, date)){
				return false;
			}
			rootEle.addElement(date);
			String plainStr = XMLUtil.createXmlString(doc);
			FileUtil.save(ConfBean.plainFilePath, CoderUtil.encode(plainStr, false));
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	/**
	 * 检查计划日期是否已存在
	 * @param diaryData
	 * @return
	 * @throws Excepion
	 */
	public boolean isExists(Element rootEle, String date) throws Exception{
		Element dateEle = rootEle.element(date);
		if(dateEle == null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 删除计划日期
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean deletePlainDay(String date) {
		try{
			date = "p"+date;
			String plainData = XMLParser.getPlainText();
			Document doc = XMLUtil.getDocument(plainData);
			Element rootEle = doc.getRootElement();
			Iterator<Element> it = rootEle.elementIterator();
			Element aPlainEle;
			while(it.hasNext()){
				aPlainEle = it.next();
				if(aPlainEle.getName().indexOf(date)!=-1){
					rootEle.remove(aPlainEle);
				}
			}
			String plainStr = XMLUtil.createXmlString(doc);
			FileUtil.save(ConfBean.plainFilePath, CoderUtil.encode(plainStr, false));
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	/**
	 * 导出计划
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PlainData exportPlainDay(String date) {
		PlainData plainBean = new PlainData();
		try{
			date = "p"+date;
			plainBean.setDate(date);
			String plainData = XMLParser.getPlainText();
			Document doc = XMLUtil.getDocument(plainData);
			Iterator<Element> plainIt = doc.getRootElement().elementIterator();
			Element aPlain;
			PlainData aPlainBean;
			List<PlainData> plainBeanList = new ArrayList<PlainData>();
			while(plainIt.hasNext()){
				aPlain = plainIt.next();
				aPlainBean = new PlainData();
				aPlainBean.setId(aPlain.element("id").getTextTrim());
				aPlainBean.setTitle(aPlain.element("title").getTextTrim());
				aPlainBean.setContent(aPlain.element("content").getTextTrim().replaceAll(flag, "\n"));
				aPlainBean.setCompleted(aPlain.element("completed").getTextTrim());
				
				plainBeanList.add(aPlainBean);
			}
			plainBean.setPlainData(plainBeanList);
		}catch(Exception e){
			log.error(e);
		}
		return plainBean;
	}
	
	/**
	 * 新建工作计划
	 * 
	 * @param plainData
	 * @return
	 * @throws Exception
	 */
	public boolean newPlain(PlainData plainData) {
		try{
			String plainDataStr = XMLParser.getPlainText();
			Document doc = XMLUtil.getDocument(plainDataStr);
			Element rootEle = doc.getRootElement();
			if(isExists(rootEle, plainData.getDate())){
				newPlainDay(plainData.getDate());
			}
			Element dayEle = rootEle.element("p"+plainData.getDate());
	
			Document aPlainDoc = DocumentHelper.createDocument();
			aPlainDoc.addElement("plainInfo");
			Element plainEle = aPlainDoc.getRootElement();
			plainEle.addElement("id");
			plainEle.element("id").setText(plainData.getId());
			plainEle.addElement("title");
			plainEle.element("title").setText(plainData.getTitle());
			plainEle.addElement("content");
			plainEle.element("content").setText(plainData.getContent().replaceAll("\n", flag));
			plainEle.addElement("completed");
			plainEle.element("completed").setText("否");
			
			dayEle.add(aPlainDoc.getRootElement());
			String plainStr = XMLUtil.createXmlString(doc);
			FileUtil.save(ConfBean.plainFilePath, CoderUtil.encode(plainStr, false));
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	/**
	 * 修改工作计划
	 * 
	 * @param plainData
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean updatePlain(PlainData plainData) {
		try{
			String plainDataStr = XMLParser.getPlainText();
			Document doc = XMLUtil.getDocument(plainDataStr);
			Element rootEle = doc.getRootElement();
			Element dayEle = rootEle.element("p"+plainData.getDate());
			Iterator<Element> plainIt = dayEle.elementIterator();
			Element aPlainEle;
			while(plainIt.hasNext()){
				aPlainEle = plainIt.next();
				String id = aPlainEle.element("id").getTextTrim();
				if(id.equals(plainData.getId())){
					aPlainEle.element("title").setText(plainData.getTitle());
					aPlainEle.element("content").setText(plainData.getContent().replaceAll("\n", flag));
					aPlainEle.element("completed").setText(plainData.getCompleted());
					break;
				}
			}
			
			String plainStr = XMLUtil.createXmlString(doc);
			FileUtil.save(ConfBean.plainFilePath, CoderUtil.encode(plainStr, false));
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	/**
	 * 删除工作计划
	 * 
	 * @param plainData
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean deletePlain(PlainData plainData) {
		try{
			String plainDataStr = XMLParser.getPlainText();
			Document doc = XMLUtil.getDocument(plainDataStr);
			Element rootEle = doc.getRootElement();
			Element dayEle = rootEle.element("p"+plainData.getDate());
			Iterator<Element> plainIt = dayEle.elementIterator();
			Element aPlainEle;
			while(plainIt.hasNext()){
				aPlainEle = plainIt.next();
				String id = aPlainEle.element("id").getTextTrim();
				if(id.equals(plainData.getId())){
					dayEle.remove(aPlainEle);
					break;
				}
			}
			
			String plainStr = XMLUtil.createXmlString(doc);
			FileUtil.save(ConfBean.plainFilePath, CoderUtil.encode(plainStr, false));
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	/**
	 * 确认完成工作计划
	 * 
	 * @param plainData
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean completePlain(PlainData plainData) {
		try{
			String plainDataStr = XMLParser.getPlainText();
			Document doc = XMLUtil.getDocument(plainDataStr);
			Element rootEle = doc.getRootElement();
			Element dayEle = rootEle.element("p"+plainData.getDate());
			Iterator<Element> plainIt = dayEle.elementIterator();
			Element aPlainEle;
			while(plainIt.hasNext()){
				aPlainEle = plainIt.next();
				String id = aPlainEle.element("id").getTextTrim();
				if(id.equals(plainData.getId())){
					aPlainEle.element("completed").setText("是");
					break;
				}
			}
			
			String plainStr = XMLUtil.createXmlString(doc);
			FileUtil.save(ConfBean.plainFilePath, CoderUtil.encode(plainStr, false));
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	/**
	 * 获取日期节点上时间
	 * @param node IconNode
	 * @return
	 */
	public String getDateFromNode(IconNode node) throws Exception{
		StringBuffer dateBuffer = new StringBuffer();
		dateBuffer.append(node.getParent().getParent().toString());
    	dateBuffer.append(node.getParent().toString());
    	dateBuffer.append(node.toString());
    	return dateBuffer.toString().replace("年", "-").replace("月", "-").replace("日", "");
	}
	
	/**
	 * 获取工作计划目录
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public void setPlainMenu(IconNode root) throws Exception{
		menuData = XMLParser.getPlainMenuAsVec();
		Enumeration year = menuData.keys();
		String yearArr[] = new String[menuData.size()];	
		String aYear;
		int i=0;
		while(year.hasMoreElements()){
			aYear = (String)year.nextElement();
			yearArr[i++] = aYear;
		}
		String temp;
		for(i=0;i<yearArr.length;i++){
			for(int j=0;j<yearArr.length-i-1;j++){
				if(Integer.parseInt(yearArr[j])<Integer.parseInt(yearArr[j+1])){
					temp = yearArr[j];
					yearArr[j] = yearArr[j+1];
					yearArr[j+1] = temp;
				}
			}
		}
		// 每一年
		for(String aYearDiary: yearArr){
			IconNode yearNode = new IconNode(aYearDiary+"年");
			setMonth(aYearDiary, yearNode);
			root.add(yearNode);
		}
	}
	
	/**
	 * 设置月份节点
	 * @param year
	 * @param yearNode
	 * @throws Exception
	 */
	private void setMonth(String year, IconNode yearNode) throws Exception{
		Hashtable<String,  Vector<String>> monthHash = menuData.get(year);
		Enumeration monthEn = monthHash.keys();
		String monthArr[] = new String[monthHash.size()];	
		String aMonth;
		int i=0;
		while(monthEn.hasMoreElements()){
			aMonth = (String)monthEn.nextElement();
			monthArr[i++] = aMonth;
		}
		String temp;
		for(i=0;i<monthArr.length;i++){
			for(int j=0;j<monthArr.length-i-1;j++){
				if(Integer.parseInt(monthArr[j])<Integer.parseInt(monthArr[j+1])){
					temp = monthArr[j];
					monthArr[j] = monthArr[j+1];
					monthArr[j+1] = temp;
				}
			}
		}
		// 每一月
		for(String aMonthDiary: monthArr){
			IconNode aMonthDiaryNode = new IconNode(aMonthDiary+"月");
			setDay(year, aMonthDiary, monthHash, aMonthDiaryNode);
			yearNode.add(aMonthDiaryNode);
		}
	}
	
	/**
	 * 设置日期节点
	 * @param year
	 * @param month
	 * @param monthNode
	 * @throws Exception
	 */
	private void setDay(String year, String month, Hashtable<String, Vector<String>> monthHash, IconNode monthNode) throws Exception{
		Vector<String> dayVec = monthHash.get(month);
		String[] dayArr = new String[dayVec.size()];
		for(int i=0;i<dayVec.size();i++){
			dayArr[i]=dayVec.get(i);
		}
		String temp;
		for(int i=0;i<dayArr.length;i++){
			for(int j=0;j<dayArr.length-i-1;j++){
				if(Integer.parseInt(dayArr[j])<Integer.parseInt(dayArr[j+1])){
					temp = dayArr[j];
					dayArr[j] = dayArr[j+1];
					dayArr[j+1] = temp;
				}
			}
		}
		
		for(String aDay:dayArr){
			IconNode aDayNode = new IconNode(aDay+"日");
			String isCompleted = isCompleted(year+"-"+month+"-"+aDay);
			if("0".equals(isCompleted)){
				aDayNode.setIcon(ImageData.completed);
			}else if("1".equals(isCompleted)){
				aDayNode.setIcon(ImageData.delete);
			}else if("2".equals(isCompleted)){
				aDayNode.setIcon(ImageData.unplain);
			}
			monthNode.add(aDayNode);
		}
	}
	
	/**
	 * 判断当前日期是否有未完成的工作
	 * @param date
	 * @return 0-已全部完成，1-有未完成的工作，2-尚未安排工作
	 */
	@SuppressWarnings("unchecked")
	public String isCompleted(String date){
		try{
			String plainDataStr = XMLParser.getPlainText();
			Document doc = XMLUtil.getDocument(plainDataStr);
			Element rootEle = doc.getRootElement();
			Element dayEle = rootEle.element("p"+date);
			Iterator<Element> plainIt = dayEle.elementIterator();
			Element aPlainEle;
			boolean isPlained = false;
			while(plainIt.hasNext()){
				isPlained = true;
				aPlainEle = plainIt.next();
				String completed = aPlainEle.element("completed").getTextTrim();
				if(!"是".equals(completed)){
					return "1";
				}
			}
			
			String plainStr = XMLUtil.createXmlString(doc);
			FileUtil.save(ConfBean.plainFilePath, CoderUtil.encode(plainStr, false));
			if(isPlained){
				return "0";
			}else{
				return "2";
			}
		}catch(Exception e){
			log.error(e);
		}
		return "2";
	}
	@SuppressWarnings("unchecked")
	public IconNode getDayNode(String date, IconNode plainRoot){
		if(StringUtil.isEmpty(date)){
			date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		IconNode todayNode = null;
		String dateArr[] = date.split("-");
		Enumeration<IconNode> en = plainRoot.children();
		IconNode yearNode;
		IconNode monthNode;
		IconNode dayNode;
		Object yearStr;
		Object monthStr;
		Object dayStr;
		while(en.hasMoreElements()){
			yearNode = en.nextElement();
			yearStr = yearNode.getUserObject();
			if(yearStr.toString().indexOf(dateArr[0])==-1){
				continue;
			}
			Enumeration<IconNode> mthEn = yearNode.children();
			while(mthEn.hasMoreElements()){
				monthNode = mthEn.nextElement();
				monthStr = monthNode.getUserObject();
				if(monthStr.toString().indexOf(dateArr[1])==-1){
					continue;
				}
				Enumeration<IconNode> dayEn = monthNode.children();
				while(dayEn.hasMoreElements()){
					dayNode = dayEn.nextElement();
					dayStr = dayNode.getUserObject();
					if(dayStr.toString().indexOf(dateArr[2])==-1){
						continue;
					}
					todayNode = dayNode;
				}
			}
		}
		return todayNode;
	}
	
	/**
	 * 闹钟提醒内容
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getFinishItem(Date date) {
		try{
			if(ConfBean.loginUserInfo == null){
				return "";
			}
			String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
			String plainDataStr = XMLParser.getPlainText();
			Document doc = XMLUtil.getDocument(plainDataStr);
			Element rootEle = doc.getRootElement();
			Element dayEle = rootEle.element("p"+dateStr);
			if(dayEle == null){
				return "今天没有安排工作学习计划！";
			}
			
			Iterator<Element> plainIt = dayEle.elementIterator();
			Element aPlainEle;
			int i=0;
			int j=0;
			while(plainIt.hasNext()){
				i++;
				aPlainEle = plainIt.next();
				if("否".equals(aPlainEle.element("completed").getTextTrim())){
					j++;
				}
			}
			if(i==0){
				return "今天没有安排工作学习计划！";
			}else if(j==0){
				// 工作已全部完成
				return "";
			}else{
				return "今天有"+i+"项工作学习计划,\n当前还有"+j+"项没有完成!";
			}
		}catch(Exception e){
			log.error(e);
		}
		return "";
	}
	
	public static final String flag = "&nbsp;";
}
