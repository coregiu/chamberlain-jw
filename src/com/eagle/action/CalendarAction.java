package com.eagle.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.dom4j.*;

import com.eagle.data.ConfBean;
import com.eagle.data.MmrDayData;
import com.eagle.ui.mycalendar.ConvertDate;
import com.eagle.ui.mycalendar.SolarTerm;
import com.eagle.util.CoderUtil;
import com.eagle.util.FileUtil;
import com.eagle.util.StringUtil;
import com.eagle.util.XMLUtil;
import com.eagle.xmlparse.XMLParser;

public class CalendarAction {
	private static Logger log = Logger.getLogger(CalendarAction.class);

	private DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");

	public CalendarAction(){
		feastTab.put("正月初一", "春节");
		feastTab.put("正月十五", "元宵节");
		feastTab.put("五月初五", "端午节");
		feastTab.put("七月初七", "情人节");
		feastTab.put("七月十五", "鬼节");
		feastTab.put("八月十五", "中秋节");
		feastTab.put("九月初九", "重阳节");
		feastTab.put("腊月初八", "腊八节");
		feastTab.put("腊月卅十", "除夕");
		feastTab.put("01月01日", "元旦");
		feastTab.put("02月14日", "情人节");
		feastTab.put("03月08日", "妇女节");
		feastTab.put("03月12日", "植树节");
		feastTab.put("04月01日", "愚人节");
		feastTab.put("05月01日", "劳动节");
		feastTab.put("05月04日", "青年节");
		feastTab.put("06月01日", "儿童节");
		feastTab.put("07月01日", "建党节");
		feastTab.put("08月01日", "建军节");
		feastTab.put("09月10日", "教师节");
		feastTab.put("10月01日", "国庆节");
		feastTab.put("11月11日", "光棍节");
		feastTab.put("12月24日", "平安夜");
		feastTab.put("12月25日", "圣诞节");
		if(ConfBean.loginUserInfo!=null){
			setMyFeast();
		}
	}
	/**
	 * 获取显示内容
	 * 
	 * @param cp
	 *            日期单元格
	 * @param cal
	 *            当前日期
	 * 
	 * @return
	 */
	public String[] getCalendarText(Calendar cal) {
		String arr[] = new String[3];
		try {
			String feast = "";
			StringBuffer toolTipText = new StringBuffer();
			StringBuffer toolTipStr = new StringBuffer();
			String worldDay = df.format(cal.getTime());

			// 计算农历
			ConvertDate cd = new ConvertDate(cal);
			String chinaDate = cd.toString();
			String solarTerm = (new SolarTerm(cal)).getSloarTrem();
			Enumeration en = feastTab.keys();
			String key;
			while (en.hasMoreElements()) {
				key = (String) en.nextElement();
				if (chinaDate.indexOf(key) != -1) {
					feast = feast + " " + feastTab.get(key);
				}
				if (df.format(cal.getTime()).indexOf(key) != -1) {
					feast = feast + " " + feastTab.get(key);
				}
			}

			// 添加tooltip
			toolTipText.append("<html><body>");
			toolTipText.append("公元 ");
			toolTipStr.append("公元 ");
			toolTipText.append(worldDay);
			toolTipStr.append(worldDay+",");
			toolTipText.append(" <br> ");
			toolTipText.append("农历 ");
			toolTipStr.append("农历 ");
			toolTipText.append(chinaDate);
			toolTipStr.append(chinaDate+",");
			toolTipText.append("<br>");
			toolTipText.append("生肖 " + cd.animalsYear());
			toolTipStr.append("生肖 " + cd.animalsYear()+",");
			toolTipText.append("<br>");
			toolTipText.append(cd.cyclYear() + "年  " + cd.cyclMonth() + "月  "
					+ cd.cyclDay() + "日");
			toolTipStr.append(cd.cyclYear() + "年  " + cd.cyclMonth() + "月  "
					+ cd.cyclDay() + "日"+",");
			toolTipText.append("<br>");
			if (StringUtil.isNotEmpty(solarTerm)) {
				toolTipText.append(solarTerm);
				toolTipStr.append(solarTerm+",");
			}
			if (StringUtil.isNotEmpty(feast)) {
				toolTipText.append(" " + feast);
				toolTipStr.append(" " + feast+",");
			}
			toolTipText.append("</body></html>");
			arr[0] = toolTipText.toString();

			// 设置显示
			toolTipText = new StringBuffer();
			toolTipText
					.append("<html>"
							+ "<body>"
							+ "<table border='0' cellspacing='0'>"
							+ "<tr>"
							+ "<td style=TEXT-ALIGN:center;>"
							+ "<span style=font-size:14px;text-underline-position:bellow;text-decoration:underline;>");
			toolTipText.append(cal.get(Calendar.DAY_OF_MONTH));
			toolTipText
					.append("</span><br><span style=font-size:8px;text-underline-position:bellow;text-decoration:underline;>");
			String chinaDay = ConvertDate.getChinaDayString(cd.day);
			if (StringUtil.isNotEmpty(solarTerm)) {
				toolTipText.append("<span style=color:blue;>" + solarTerm
						+ "</span>");
			} else if (StringUtil.isNotEmpty(feast)) {
				feast = feast.trim().length() > 5 ? feast.substring(0, 4)
						+ ".." : feast;
				toolTipText.append("<span style=color:blue;>" + feast
						+ "</span>");
			} else if ("初一".equals(chinaDay)) {
				toolTipText.append((cd.leap ? "闰" : "")
						+ ConvertDate.chineseMonthNumber[cd.month - 1] + "月");
			} else {
				toolTipText.append(chinaDay);
			}
			toolTipText.append("</span></td></tr></table></body></html>");
			arr[1] = toolTipText.toString();
			arr[2] = toolTipStr.toString();
		} catch (Exception e) {
			log.error(e);
		}
		return arr;
	}

	// 节假、纪念日
	private Hashtable<String, String> feastTab = new Hashtable<String, String>();

	// 动态添加个人定制的纪念日
	public void setMyFeast() {
		try {
			Vector<Vector> feastList = this.getMmrDateData();
			String dayArr[];
			String item[] = new String[2];
			for (Vector aMmrDay : feastList) {
				if("否".equals(aMmrDay.get(4))){
					continue;
				}
				dayArr = aMmrDay.get(1).toString().split("-");
				if("阳历".equals(aMmrDay.get(3))){
					if(dayArr.length==3){
						item[0] = dayArr[0]+"年"+dayArr[1]+"月"+dayArr[2]+"日";
					}else{
						item[0] = dayArr[0]+"月"+dayArr[1]+"日";
					}
				}else{
					if(dayArr.length==3){
						int month = Integer.parseInt(dayArr[1])-1;
						int day = Integer.parseInt(dayArr[2]);
						item[0] = dayArr[0]+"年"+ConvertDate.chineseMonthNumber[month]+"月"+ConvertDate.getChinaDayString(day);
					}else{
						int month = Integer.parseInt(dayArr[0])-1;
						int day = Integer.parseInt(dayArr[1]);
						item[0] = ConvertDate.chineseMonthNumber[month]+"月"+ConvertDate.getChinaDayString(day);
					}
				}
				item[1] = aMmrDay.get(2).toString();
				feastTab.put(item[0].trim(), item[1].trim().length() > 5 ? 
										item[1].trim().substring(0, 5) : item[1].trim());
			}
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	/**
	 * 获取纪念日信息的表头信息
	 * 
	 * @return
	 */
	public Vector<String> getMmrDateTHead() {
		Vector<String> iTHead = new Vector<String>();
		// iTHead.add("提醒日期");
		iTHead.add("序号");
		iTHead.add("日期");
		iTHead.add("纪念日名称");
		iTHead.add("历法");
		iTHead.add("是否有效");
		return iTHead;
	}

	/**
	 * 获取纪念日表体数据
	 * <mmrDay>
	 * 	<item id="四月初四">
	 * 		<type>阴历</type>
	 * 		<content>老婆生日</content>
	 * 		<valid>是</valid>
	 *  </item>
	 * </mmrDay>
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Vector<Vector> getMmrDateData() {
		Vector<Vector> clkData = new Vector<Vector>();
		Vector<String> aClkVec;
		try {
			Element mrDateEle = XMLParser.getExtText("mmrDay");

			Iterator<Element> mrIt = mrDateEle.elementIterator();
			Element aClkEle;
			int i=1;
			while (mrIt.hasNext()) {
				aClkEle = mrIt.next();
				aClkVec = new Vector<String>();
				aClkVec.add(String.valueOf(i++));
				aClkVec.add(aClkEle.attribute("id").getText());
				aClkVec.add(aClkEle.element("content").getTextTrim());
				aClkVec.add(aClkEle.element("type").getTextTrim());
				aClkVec.add(aClkEle.element("valid").getTextTrim());

				clkData.add(aClkVec);
			}
		} catch (Exception e) {
			log.error(e);
		}
		return clkData;
	}
	
	/**
	 * 删除纪念日
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean deleteMmyDay(MmrDayData data){
		try {
			String extStr = XMLParser.getExtText();
			Document doc = XMLUtil.getDocument(extStr);
			
			Element mrDateEle = doc.getRootElement().element("mmrDay");
			Iterator<Element> mrIt = mrDateEle.elementIterator();
			Element aClkEle;
			while (mrIt.hasNext()) {
				aClkEle = mrIt.next();
				Attribute attr = aClkEle.attribute("id");
				if(attr!=null&&attr.getText().equals(data.getId())){
					mrDateEle.remove(aClkEle);
					break;
				}
			}
			FileUtil.save(ConfBean.extFilePath, CoderUtil.encode(doc.asXML(), false));
			return true;
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
	
	/**
	 * 判断纪念日是否存在
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isRepeatMmyDay(Element mrDateEle, MmrDayData data){
		try {
			Iterator<Element> mrIt = mrDateEle.elementIterator();
			Element aClkEle;
			while (mrIt.hasNext()) {
				aClkEle = mrIt.next();
				Attribute attr = aClkEle.attribute("id");
				if(attr!=null&&attr.getText().equals(data.getId())){
					return true;
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
	
	/**
	 * 添加纪念日
	 * @param data
	 * @return
	 */
	public boolean addMmyDay(MmrDayData data){
		try {
			String extStr = XMLParser.getExtText();
			Document doc = XMLUtil.getDocument(extStr);
			
			Element mrDateEle = doc.getRootElement().element("mmrDay");

			if(isRepeatMmyDay(mrDateEle, data)){
				return false;
			}
			Document aDoc = DocumentHelper.createDocument();
			aDoc.addElement("item");
			Element itemEle = aDoc.getRootElement();
			itemEle.addAttribute("id", "id");
			itemEle.attribute("id").setText(data.getId());
			
			itemEle.addElement("content");
			itemEle.element("content").setText(data.getContent());
			itemEle.addElement("type");
			itemEle.element("type").setText(data.getType());
			itemEle.addElement("valid");
			itemEle.element("valid").setText(data.getValid());
			
			mrDateEle.add(aDoc.getRootElement());
			
			FileUtil.save(ConfBean.extFilePath, CoderUtil.encode(doc.asXML(), false));
			return true;
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
	
	/**
	 * 修改纪念日
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean updateMmyDay(MmrDayData data){
		try {
			String extStr = XMLParser.getExtText();
			Document doc = XMLUtil.getDocument(extStr);
			
			Element mrDateEle = doc.getRootElement().element("mmrDay");
			Iterator<Element> mrIt = mrDateEle.elementIterator();
			Element itemEle;
			while (mrIt.hasNext()) {
				itemEle = mrIt.next();
				Attribute attr = itemEle.attribute("id");
				if(attr!=null&&attr.getText().equals(data.getId())){
					itemEle.element("content").setText(data.getContent());
					itemEle.element("type").setText(data.getType());
					itemEle.element("valid").setText(data.getValid());
					break;
				}
			}
			FileUtil.save(ConfBean.extFilePath, CoderUtil.encode(doc.asXML(), false));
			return true;
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
}
