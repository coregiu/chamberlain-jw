package com.eagle.action;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.eagle.data.ConfBean;
import com.eagle.data.DiaryData;
import com.eagle.prop.NameProp;
import com.eagle.util.CoderUtil;
import com.eagle.util.FileUtil;
import com.eagle.util.StringUtil;
import com.eagle.util.XMLUtil;
import com.eagle.xmlparse.XMLParser;

/**
 * 日记本
 * 
 * <PrivateData> 
 *	 <Diary> 
 *	  <Time id="2010-11-03"> 
 *	   <Item id="Default">03日</Item> 
 *	   <Item id="COME ON">03日</Item> 
 *	  </Time>  
 *	  <Time id="2010-11-17"> 
 *	   <Item id="Default">17日</Item> 
 *	  </Time>  
 *	  <Time id="2010-11-18"> 
 *	   <Item id="Default">18日</Item> 
 *	  </Time> 
 *	 </Diary>  
 *	</PrivateData>
 * @author szhang
 *
 */
public class DiaryAction {
	private Logger log = Logger.getLogger(DiaryAction.class);
	//private Logger log = Logger.getLogger(DiaryAction.class);
	private Hashtable<String, Hashtable<String,Hashtable<String, Vector<String>>>> menuData;
	public DiaryAction(){
		
	}
	
	/**
	 * 创建日记
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public boolean newDiary(DiaryData data) throws Exception{
		String diaryData = new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, FileUtil.getFileAsString(ConfBean.diaryFilePath)));
		Document doc = XMLUtil.getDocument(diaryData);
		if(!"Default".equals(data.getItem())){
			Iterator diaryELe = doc.getRootElement().element("Diary").elementIterator();
			Element aTime;
			while(diaryELe.hasNext()){
				aTime = (Element)diaryELe.next();
				if(data.getDate().equals(aTime.attribute("id").getText())){
					Document iniDoc = DocumentHelper.createDocument();
					iniDoc.addElement("Item");
					
					Element diaryEle = iniDoc.getRootElement();
					diaryEle.addAttribute("id", data.getItem());
					
					aTime.add(iniDoc.getRootElement());
					break;
				}
			}
		}else{
			Document iniDoc = DocumentHelper.createDocument();
			iniDoc.addElement("Time");
			
			Element diaryEle = iniDoc.getRootElement();
			diaryEle.addAttribute("id", data.getDate());
			diaryEle.addElement("Item");
			diaryEle.element("Item").addAttribute("id", data.getItem());
			doc.getRootElement().element("Diary").add(iniDoc.getRootElement());
		}
		String diaryStr = XMLUtil.createXmlString(doc);
		FileUtil.save(ConfBean.diaryFilePath, CoderUtil.encode(diaryStr, false));
		return true;
	}
	
	/**
	 * 重命名
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public boolean renameDiary(DiaryData data) throws Exception{
		String diaryData = new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, FileUtil.getFileAsString(ConfBean.diaryFilePath)));
		Document doc = XMLUtil.getDocument(diaryData);
		Iterator diaryELe = doc.getRootElement().element("Diary").elementIterator();
		Element aTime;
		Element aItem;
		boolean flag = false;
		while(diaryELe.hasNext()){
			aTime = (Element)diaryELe.next();
			if(data.getDate().equals(aTime.attribute("id").getText())){
				Iterator timeELe = aTime.elementIterator();
				while(timeELe.hasNext()){
					aItem = (Element)timeELe.next();
					if(data.getItem().equals(aItem.attribute("id").getText())){
						aItem.attribute("id").setText(data.getRename());
						flag = true;
						break;
					}
				}
			}
			if(flag){
				break;
			}
		}
			
		String diaryStr = XMLUtil.createXmlString(doc);
		FileUtil.save(ConfBean.diaryFilePath, CoderUtil.encode(diaryStr, false));
		return true;
	}
	
	/**
	 * 删除日记
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public boolean deleteDiary(DiaryData data) throws Exception{
		String diaryData = new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, FileUtil.getFileAsString(ConfBean.diaryFilePath)));
		Document doc = XMLUtil.getDocument(diaryData);
		Element diaryEle = doc.getRootElement().element("Diary");
		Iterator diaryELe = diaryEle.elementIterator();
		Element aTime;
		Element aItem;
		while(diaryELe.hasNext()){
			aTime = (Element)diaryELe.next();
			if(aTime.attribute("id").getText().indexOf(data.getDate())== 0){
				// 删除日期以上的日记
				if(StringUtil.isEmpty(data.getItem())||"Default".equals(data.getItem())){
					diaryEle.remove(aTime);
				}
				// 删除某具体日记
				else{
					Iterator timeELe = aTime.elementIterator();
					while(timeELe.hasNext()){
						aItem = (Element)timeELe.next();
						if(data.getItem().equals(aItem.attribute("id").getText())){
							aTime.remove(aItem);
							break;
						}
					}
				}	
			}
		}
			
		String diaryStr = XMLUtil.createXmlString(doc);
		FileUtil.save(ConfBean.diaryFilePath, CoderUtil.encode(diaryStr, false));
		return true;
	}
	
	/**
	 * 根据节点构造日记BEAN
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public DiaryData getDiaryData(DefaultMutableTreeNode node) throws Exception{
		DiaryData diaryData = new DiaryData();
		if(node.getLevel()==1){
			diaryData.setDate(node.toString().replace("年", ""));
		}else if(node.getLevel()==2){
			diaryData.setDate((node.getParent().toString()+"-"+node.toString()).replace("年", "").replace("月", ""));
		}else if(node.getLevel()==3){
			diaryData.setDate(this.getDateFromNode(node));
			diaryData.setItem(NameProp.DEFAULT);
		}else if(node.getLevel()==4){
			diaryData.setDate(this.getDateFromNode(node));
			diaryData.setItem(node.toString());
		}
		return diaryData;
	}
	
	/**
	 * 保存日记
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public boolean saveDiary(DiaryData data) throws Exception{
		String diaryData = new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, FileUtil.getFileAsString(ConfBean.diaryFilePath)));
		Document doc = XMLUtil.getDocument(diaryData);
		Iterator diaryELe = doc.getRootElement().element("Diary").elementIterator();
		Element aTime;
		Element aItem;
		boolean flag = false;
		while(diaryELe.hasNext()){
			aTime = (Element)diaryELe.next();
			if(data.getDate().equals(aTime.attribute("id").getText())){
				Iterator timeELe = aTime.elementIterator();
				while(timeELe.hasNext()){
					aItem = (Element)timeELe.next();
					if(data.getItem().equals(aItem.attribute("id").getText())){
						String cnt = data.getContent();
						if(StringUtil.isNotEmpty(cnt)){
							cnt = cnt.replaceAll("\n", linFlag);
						}
						aItem.setText(cnt);
						flag = true;
						break;
					}
				}
			}
			if(flag){
				break;
			}
		}
			
		String diaryStr = XMLUtil.createXmlString(doc);
		FileUtil.save(ConfBean.diaryFilePath, CoderUtil.encode(diaryStr, false));
		return true;
	}
	
	/**
	 * 检查日记是否重复
	 * @param diaryData
	 * @return
	 * @throws Excepion
	 */
	public boolean isRepeat(DiaryData diaryData) throws Exception{
		if(XMLParser.getDiaryData(diaryData.getDate(), diaryData.getItem()) == null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 获取日期节点上时间
	 * @param node DefaultMutableTreeNode
	 * @return
	 */
	public String getDateFromNode(DefaultMutableTreeNode node) throws Exception{
		if(node.getLevel()==3){
			StringBuffer dateBuffer = new StringBuffer();
			dateBuffer.append(node.getParent().getParent().toString());
	    	dateBuffer.append(node.getParent().toString());
	    	dateBuffer.append(node.toString());
	    	return dateBuffer.toString().replace("年", "-").replace("月", "-").replace("日", "");
		}else if(node.getLevel()==4){
			StringBuffer dateBuffer = new StringBuffer();
			dateBuffer.append(node.getParent().getParent().getParent().toString());
	    	dateBuffer.append(node.getParent().getParent().toString());
	    	dateBuffer.append(node.getParent().toString());
	    	return dateBuffer.toString().replace("年", "-").replace("月", "-").replace("日", "");
		}
		return "";
	}
	
	/**
	 * 获取日记目录
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public void setDiaryMenu(DefaultMutableTreeNode root) throws Exception{
		menuData = XMLParser.getDiaryMenuAsVec();
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
			DefaultMutableTreeNode yearNode = new DefaultMutableTreeNode(aYearDiary+"年");
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
	private void setMonth(String year, DefaultMutableTreeNode yearNode) throws Exception{
		Hashtable<String, Hashtable<String, Vector<String>>> monthHash = menuData.get(year);
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
			DefaultMutableTreeNode aMonthDiaryNode = new DefaultMutableTreeNode(aMonthDiary+"月");
			setDay(aMonthDiary, monthHash, aMonthDiaryNode);
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
	private void setDay(String month, Hashtable<String, Hashtable<String, Vector<String>>> monthHash, DefaultMutableTreeNode monthNode) throws Exception{
		Hashtable<String, Vector<String>> dayHash = monthHash.get(month);
		Enumeration dayEn = dayHash.keys();
		String dayArr[] = new String[dayHash.size()];	
		String aDay;
		int i=0;
		while(dayEn.hasMoreElements()){
			aDay = (String)dayEn.nextElement();
			dayArr[i++] = aDay;
		}
		String temp;
		for(i=0;i<dayArr.length;i++){
			for(int j=0;j<dayArr.length-i-1;j++){
				if(Integer.parseInt(dayArr[j])<Integer.parseInt(dayArr[j+1])){
					temp = dayArr[j];
					dayArr[j] = dayArr[j+1];
					dayArr[j+1] = temp;
				}
			}
		}
		// 每一天
		for(String aDayDiary: dayArr){
			DefaultMutableTreeNode aDayNode = new DefaultMutableTreeNode(aDayDiary+"日");
			setDiary(aDayDiary, dayHash, aDayNode);
			monthNode.add(aDayNode);
		}
	}
	
	/**
	 * 设置日记节点
	 * @param aDayDiary
	 * @param dayHash
	 * @param aDayNode
	 */
	private void setDiary(String aDayDiary, Hashtable<String, Vector<String>>dayHash, DefaultMutableTreeNode aDayNode){
		Vector<String> dayDiary = dayHash.get(aDayDiary);
		for(String aDiary: dayDiary){
			if(!"Default".equals(aDiary)){
				aDayNode.add(new DefaultMutableTreeNode(aDiary));
			}
		}
	}
	
	/**
	 * 根据节点获取日记内容
	 * @param date
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public String getContent(DiaryData diaryData) {
		try{
			if(diaryData==null||diaryData.getDate()==null){
				return "";
			}
			if(diaryData.getItem()==null){
				diaryData.setItem("Default");
			}
			
			String cnt = XMLParser.getDiaryData(diaryData.getDate(), diaryData.getItem());
			if(StringUtil.isNotEmpty(cnt)){
				cnt = cnt.replaceAll(linFlag, "\n");
			}
			return cnt;
		}catch(Exception e){
			log.error(e);
		}
		return "";
	}
	
	/**
	 * 获取当前展开节点
	 * 
	 * @param date
	 * @param diaryRoot
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DefaultMutableTreeNode getDayNode(String date, DefaultMutableTreeNode diaryRoot){
		if(StringUtil.isEmpty(date)){
			date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		DefaultMutableTreeNode todayNode = null;
		String dateArr[] = date.split("-");
		Enumeration<DefaultMutableTreeNode> en = diaryRoot.children();
		DefaultMutableTreeNode yearNode;
		DefaultMutableTreeNode monthNode;
		DefaultMutableTreeNode dayNode;
		Object yearStr;
		Object monthStr;
		Object dayStr;
		while(en.hasMoreElements()){
			yearNode = en.nextElement();
			yearStr = yearNode.getUserObject();
			if(yearStr.toString().indexOf(dateArr[0])==-1){
				continue;
			}
			Enumeration<DefaultMutableTreeNode> mthEn = yearNode.children();
			while(mthEn.hasMoreElements()){
				monthNode = mthEn.nextElement();
				monthStr = monthNode.getUserObject();
				if(monthStr.toString().indexOf(dateArr[1])==-1){
					continue;
				}
				Enumeration<DefaultMutableTreeNode> dayEn = monthNode.children();
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
	 * 导出日记档案
	 * 
	 * @param filenme
	 * @param tabIdx
	 * @param currentNode
	 * @return
	 */
	public boolean grt2HTML(String filename, int tabIdx, DefaultMutableTreeNode currentNode){
		try{
			StringBuffer htmlBuf = new StringBuffer();
			htmlBuf.append("<html>");
			htmlBuf.append("<head>");
			htmlBuf.append("<title>我的管家数据导出</title>");
			htmlBuf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2132\">");
			htmlBuf.append("</head>");

			htmlBuf.append("<body bgcolor=\"#FFFFE8\" leftmargin=\"0\" topmargin=\"0\">");
			String title = "";
			if(tabIdx==0){
				// 导出日记
				if(currentNode == null){
					title = "我的日记";
				}else{
					title = this.getDateFromNode(currentNode)+"日记";
				}
				htmlBuf.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" bgcolor=\"#1071ac\">");
				htmlBuf.append("  <tr> ");
				htmlBuf.append("	<td height=\"63\">　<font color=\"#FFFFFF\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+title+"</font></td>");
				htmlBuf.append("  </tr>");
				htmlBuf.append("  <tr bgcolor=\"#000000\"> ");
				htmlBuf.append("	<td height=\"1\"></td>");
				htmlBuf.append("  </tr>");
				htmlBuf.append("</table>");
				htmlBuf.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"10\">");
				
				Vector<String[]> list = getExportDiaryData(currentNode);
				for(String [] aDiary:list){
					htmlBuf.append("  <tr valign=\"top\"> ");
					htmlBuf.append("	<td height=\"195\"> ");
					htmlBuf.append("	  <p> <font color=\"#0000FF\"><b><br><a name=\""+aDiary[0]+"\"></a> "+aDiary[0]+"</b></font><br><br>");
					htmlBuf.append("	  <p>"+aDiary[1]+"</p>");
					htmlBuf.append("	  </td>");
					htmlBuf.append("  </tr>");
				}
				htmlBuf.append("</table>");
			}else{
				// 导出档案
				if(currentNode == null){
					title = "我的档案";
				}else{
					title = currentNode.getUserObject().toString();
				}
				htmlBuf.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" bgcolor=\"#1071ac\">");
				htmlBuf.append("  <tr> ");
				htmlBuf.append("	<td height=\"63\">　<font color=\"#FFFFFF\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+title+"</font></td>");
				htmlBuf.append("  </tr>");
				htmlBuf.append("  <tr bgcolor=\"#000000\"> ");
				htmlBuf.append("	<td height=\"1\"></td>");
				htmlBuf.append("  </tr>");
				htmlBuf.append("</table>");
				
				htmlBuf.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"10\">");
				
				Vector<String[]> list = getExportArkData(currentNode);
				for(String [] aDiary:list){
					htmlBuf.append("  <tr valign=\"top\"> ");
					htmlBuf.append("	<td height=\"195\"> ");
					htmlBuf.append("	  <p> <font color=\"#0000FF\"><b><br><a name=\""+aDiary[0]+"\"></a> "+aDiary[0]+"</b></font><br><br>");
					htmlBuf.append("	  <p>"+aDiary[1]+"</p>");
					htmlBuf.append("	  </td>");
					htmlBuf.append("  </tr>");
				}
				htmlBuf.append("</table>");
			}
			htmlBuf.append("</body>");
			htmlBuf.append("</html>");
			
			String data = htmlBuf.toString();
			data = data.replaceAll("&nbsp;", "<br>");
			FileUtil.save(filename, data, "UTF-8");
		}catch(Exception e){
			log.error(e);
		}
		return true;
	}
	
	/**
	 * 获取要导出的日记数据
	 * 
	 * @param currentNode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Vector<String[]> getExportDiaryData(DefaultMutableTreeNode currentNode){
		Vector<String[]> listData = new Vector<String[]>();
		try {
			String diaryData = new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, FileUtil.getFileAsString(ConfBean.diaryFilePath)));
			Document doc = XMLUtil.getDocument(diaryData);
			Element diaryEle = doc.getRootElement().element("Diary");
			Iterator diaryELe = diaryEle.elementIterator();
			Element aTime;
			Element aItem;
			DiaryData data = getDiaryData(currentNode);
			String tmpArr[];
			while(diaryELe.hasNext()){
				aTime = (Element)diaryELe.next();
				if(aTime.attribute("id").getText().indexOf(data.getDate())== 0){
					// 加载日期以上的日记
					if(StringUtil.isEmpty(data.getItem())||"Default".equals(data.getItem())){
						Iterator<Element> it = aTime.elementIterator();
						Element itemEle;
						String itemName;
						while(it.hasNext()){
							itemEle = it.next();
							tmpArr = new String[2];
							itemName = itemEle.attribute("id").getText();
							if("Default".equals(itemName)){
								tmpArr[0] = aTime.attribute("id").getText();
								tmpArr[1] = itemEle.getTextTrim();
							}else{
								tmpArr[0] = itemName;
								tmpArr[1] = itemEle.getTextTrim();
							}
							listData.add(tmpArr);
						}
					}
					// 加载某具体日记
					else{
						Iterator timeELe = aTime.elementIterator();
						while(timeELe.hasNext()){
							aItem = (Element)timeELe.next();
							if(data.getItem().equals(aItem.attribute("id").getText())){
								tmpArr = new String[2];
								tmpArr[0] = aItem.attribute("id").getText();
								tmpArr[1] = aItem.getTextTrim();
								listData.add(tmpArr);
							}
						}
					}	
				}
			}
			
		} catch (Exception e) {
			log.error(e);
		}			
		return listData;
	}
	
	/**
	 * 加载要导出的档案数据
	 * 
	 * @param currentNode
	 * @return
	 */
	private Vector<String[]> getExportArkData(DefaultMutableTreeNode currentNode){
		Vector<String[]> listData = new Vector<String[]>();
		try {
			// 获取创建路径
			TreeNode path[] = currentNode.getPath();
			// 获取档案内容
			String prvData = XMLParser.getDiaryText();
			Document doc = XMLUtil.getDocument(prvData);
			Element arkEle = doc.getRootElement().element(NameProp.ARKITEM);
			Element subEle = arkEle;
			for(int i=1;i<path.length;i++){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)path[i];
				Iterator it = subEle.elementIterator();
				while(it.hasNext()){
					subEle = (Element)it.next();
					Attribute attr = subEle.attribute("id");
					if(attr!=null&&attr.getText().equals(node.getUserObject())){
						break;
					}
				}
			}
			
			if(subEle!=null){
				loadExpArkData(listData, subEle);				
			}
		} catch (Exception e) {
			log.error(e);
		}	
		return listData;
	}
	
	@SuppressWarnings("unchecked")
	private void loadExpArkData(Vector<String[]> listData, Element ele){
		if("item".equals(ele.getName())){
			String arr[] = new String[2];
			arr[0] = ele.attribute("id").getText();
			arr[1] = ele.element("content").getTextTrim();
			listData.add(arr);
			Iterator<Element> it = ele.elementIterator();
			while(it.hasNext()){
				loadExpArkData(listData, it.next());
			}
		}
	}
	
	private static String linFlag = "&nbsp;";
}
