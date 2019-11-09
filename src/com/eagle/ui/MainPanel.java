package com.eagle.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;

import com.eagle.action.CalendarAction;
import com.eagle.action.PlainAction;
import com.eagle.data.ConfBean;
import com.eagle.data.ImageData;
import com.eagle.util.StringUtil;

@SuppressWarnings("serial")
public class MainPanel extends JPanel{
	@SuppressWarnings("unused")
	private String listStr;
	public MainPanel(){
		//this.listArr = listArr;
		CalendarAction action = new CalendarAction();
		Calendar cal = new GregorianCalendar();
		String arr[] = action.getCalendarText(cal);
		listStr = arr[2];
		if("0".equals(ConfBean.autoRemind)){
			PlainAction pAction = new PlainAction();
			listStr = listStr+pAction.getFinishItem(cal.getTime())+",";
		}
	}
	
	public void paint(Graphics g) {
		// 清屏
		super.paint(g);
		g.drawImage(ImageData.bodyPic.getImage(), 0, 0, null);
		if(StringUtil.isNotEmpty(listStr)){
			listStr = listStr.substring(0, listStr.length()-1);
			String listArr[] = listStr.split(",");
			int i=0;
			for(String aList:listArr){
				if(!"1".equals(ConfBean.type)){
					g.setColor(Color.RED);
				}else{
					g.setColor(Color.BLUE);
				}
				g.drawString(aList, 550, (++i)*15);
			}
		}
	}
}
