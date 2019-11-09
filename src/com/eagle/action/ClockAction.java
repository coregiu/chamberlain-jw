package com.eagle.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.dom4j.*;

import com.eagle.data.*;
import com.eagle.multiprocess.ClockProcess;
import com.eagle.util.CoderUtil;
import com.eagle.util.FileUtil;
import com.eagle.util.XMLUtil;
import com.eagle.xmlparse.XMLParser;

/**
 * 闹钟事件
 * 
 * @author szhang
 * 
 */
public class ClockAction {
	private Logger log = Logger.getLogger(ClockAction.class);

	private ClockData clockData;

	private ClockProcess clockProcess;

	public ClockAction() {
		this.clockData = new ClockData();
	}

	/**
	 * 初始化闹钟对象
	 * 
	 */
	public void init() {
		Hashtable<String, ClockData> clockTb = new Hashtable<String, ClockData>();
		// 加载闹钟提醒
		loadClockData(clockTb);

		clockData.setClkDataList(clockTb);
	}

	@SuppressWarnings("unchecked")
	public void loadClockData(Hashtable<String, ClockData> clockTb) {
		try {
			Calendar cal = new GregorianCalendar();
			Element clkEle = XMLParser.getExtText("clock");
			Iterator<Element> clkIt = clkEle.elementIterator();
			Element aClkEle;
			ClockData aClkData;
			while(clkIt.hasNext()){
				aClkEle = clkIt.next();
				aClkData = new ClockData();
				aClkData.setRemindTime(aClkEle.attribute("time").getText());
				aClkData.setRemindType(aClkEle.element("remindType").getTextTrim());
				aClkData.setIsRepeat(aClkEle.element("isRepeat").getTextTrim());
				aClkData.setRemindContent(aClkEle.element("remindContent").getTextTrim());
				aClkData.setIsValid(aClkEle.element("isValid").getTextTrim());
				boolean dateValid = true;
				if(aClkData.getIsRepeat().indexOf("2")==0||aClkData.getIsRepeat().indexOf("3")==0){
					try{
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						String clkDate = df.format(new Date());
						if(!clkDate.equals(aClkData.getIsRepeat())){
							dateValid = false;
						}
					}catch(Exception e){

					}
				}
				
				if("工作日提醒".equals(aClkData.getIsRepeat())){
					try{
						int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
						if(day_of_week==7||day_of_week==1){
							dateValid = false;
						}
					}catch(Exception e){

					}
				}
				
				if("非工作日提醒".equals(aClkData.getIsRepeat())){
					try{
						int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
						if(!(day_of_week==7||day_of_week==1)){
							dateValid = false;
						}
					}catch(Exception e){

					}
				}
				
				if(aClkData.getIsRepeat().indexOf("2")==0||aClkData.getIsRepeat().indexOf("3")==0){
					try{
						Date clkDate = new SimpleDateFormat("yyyy-MM-dd").parse(aClkData.getIsRepeat());
						if(clkDate.before(new Date())){
							dateValid = false;
						}
					}catch(Exception e){

					}
				}
				if("是".equals(aClkData.getIsValid())&&dateValid){
					clockTb.put(aClkData.getRemindTime(), aClkData);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
	}

	public void execute() {
		clockProcess = new ClockProcess(clockData);
		clockProcess.start();
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		if(clockProcess!=null){
			clockProcess.setRunFlag(false);
		}
	}

	public void restart(){
		stop();
		init();
		execute();
	}
	
	public void setClockData() {
		clockProcess.setClkData(clockData);
	}

	public void deleteClock(ClockData clockData) {

	}

	/**
	 * 获取闹钟信息的表头信息
	 * 
	 * @return
	 */
	public Vector<String> getClkTHead() {
		Vector<String> iTHead = new Vector<String>();
		// iTHead.add("提醒日期");
		iTHead.add("闹钟时间（时分）");
		iTHead.add("提醒方式");
		iTHead.add("闹钟类型");
		iTHead.add("提醒内容");
		iTHead.add("是否有效");
		return iTHead;
	}

	/**
	 * 获取闹钟表体数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Vector<Vector> getClkData() {
		Vector<Vector> clkData = new Vector<Vector>();
		Vector<String> aClkVec;
		try {
			Element clockEle = XMLParser.getExtText("clock");

			Iterator<Element> clkIt = clockEle.elementIterator();
			Element aClkEle;
			while (clkIt.hasNext()) {
				aClkEle = clkIt.next();
				aClkVec = new Vector<String>();
				// aClkVec.add(aClkEle.attribute("date").getText());
				aClkVec.add(aClkEle.attribute("time").getText());
				aClkVec.add(aClkEle.element("remindType").getTextTrim());
				aClkVec.add(aClkEle.element("isRepeat").getTextTrim());
				aClkVec.add(aClkEle.element("remindContent").getTextTrim());
				aClkVec.add(aClkEle.element("isValid").getTextTrim());

				clkData.add(aClkVec);
			}
		} catch (Exception e) {
			log.error(e);
		}
		return clkData;
	}

	/**
	 * 删除闹钟数据
	 * 
	 * @param clockData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean deleteIInfo(ClockData clockData) {
		try{
			String extStr = XMLParser.getExtText();
			Document doc = XMLUtil.getDocument(extStr);
			Element clkEle = doc.getRootElement().element("clock");
			Iterator<Element> clkIt = clkEle.elementIterator();
			Element aClkEle;
			String time;
			while (clkIt.hasNext()) {
				aClkEle = clkIt.next();
				time = aClkEle.attribute("time").getText();
				if(time.equals(clockData.getRemindTime())){
					clkEle.remove(aClkEle);
				}
			}
			FileUtil.save(ConfBean.extFilePath, CoderUtil.encode(doc.asXML(), false));
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}

	/**
	 * 增加闹钟数据
	 * 
	 * @param clockData
	 * @return
	 */
	public boolean add(ClockData clockData) {
		try{
			String extStr = XMLParser.getExtText();
			Document doc = XMLUtil.getDocument(extStr);
			// 是否重复
			Element clkEle = doc.getRootElement().element("clock");
			if(isRepeat(clkEle, clockData)){
				return false;
			}
			
			Document clkDoc = DocumentHelper.createDocument();
			
			clkDoc.addElement("aClock");
			Element aClkEle = clkDoc.getRootElement();
			aClkEle.addAttribute("date", "date");
			aClkEle.attribute("date").setText(clockData.getRemindDate());
			
			aClkEle.addAttribute("time", "time");
			aClkEle.attribute("time").setText(clockData.getRemindTime());
			
			aClkEle.addElement("remindType");
			aClkEle.element("remindType").setText(clockData.getRemindType());
			
			aClkEle.addElement("isRepeat");
			aClkEle.element("isRepeat").setText(clockData.getIsRepeat());
			
			aClkEle.addElement("remindContent");
			aClkEle.element("remindContent").setText(clockData.getRemindContent());
			
			aClkEle.addElement("isValid");
			aClkEle.element("isValid").setText(clockData.getIsValid());
			
			clkEle.add(aClkEle);
			FileUtil.save(ConfBean.extFilePath, CoderUtil.encode(doc.asXML(), false));
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}

	/**
	 * 修改闹钟数据
	 * 
	 * @param clockData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean update(ClockData clockData) {
		try{
			String extStr = XMLParser.getExtText();
			Document doc = XMLUtil.getDocument(extStr);
			
			Element clkEle = doc.getRootElement().element("clock");
			Iterator<Element> clkIt = clkEle.elementIterator();
			Element aClkEle;
			String time;
			while (clkIt.hasNext()) {
				aClkEle = clkIt.next();
				time = aClkEle.attribute("time").getText();
				if(time.equals(clockData.getRemindTime())){
					aClkEle.addAttribute("date", "date");
					aClkEle.attribute("date").setText(clockData.getRemindDate());
					
					aClkEle.addElement("remindType");
					aClkEle.element("remindType").setText(clockData.getRemindType());
					
					aClkEle.addElement("isRepeat");
					aClkEle.element("isRepeat").setText(clockData.getIsRepeat());
					
					aClkEle.addElement("remindContent");
					aClkEle.element("remindContent").setText(clockData.getRemindContent());
					
					aClkEle.addElement("isValid");
					aClkEle.element("isValid").setText(clockData.getIsValid());
				}
			}
			
			FileUtil.save(ConfBean.extFilePath, CoderUtil.encode(doc.asXML(), false));
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	/**
	 * 检查闹钟时间是否已配置
	 * @param clkEle
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isRepeat(Element clkEle, ClockData clockData){
		try{
			Iterator<Element> clkIt = clkEle.elementIterator();
			Element aClkEle;
			String time;
			while (clkIt.hasNext()) {
				aClkEle = clkIt.next();
				time = aClkEle.attribute("time").getText();
				if(time.equals(clockData.getRemindTime())){
					return true;
				}
			}
		}catch(Exception e){
			log.error(e);
		}
		return false;
	}
	
	public static void main(String[] args) {
		Calendar cal = new GregorianCalendar();
		System.out.println(cal.get(Calendar.DAY_OF_WEEK));
	}
}
