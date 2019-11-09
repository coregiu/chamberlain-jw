package com.eagle.util;

import java.text.*;
import java.util.*;
import org.apache.commons.logging.*;

/**
 * @author szhang
 * 日期时间操作
 * */
public class DateUtil {
	private static Log log = LogFactory.getLog(DateUtil.class);

	/**
	 * 计算两个时间的差
	 * @param string 前一时间
	 * @param string 后一时间
	 * @return double 时间差，单位为小时
	 * */
	public static double subTimeAsDouble(String timePre, String timeNex) {
		try {
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 前一时间
			Calendar calPre = new GregorianCalendar();
			calPre.setTime(format.parse(timePre));
			// 后一时间
			Calendar calNex = new GregorianCalendar();
			calNex.setTime(format.parse(timeNex));
			
			long intPre = calPre.getTimeInMillis();
			long intNex = calNex.getTimeInMillis();
			long subValue = intPre - intNex;
			double returnValue = ((double) subValue) / 3600000;
			DecimalFormat deFormat = new DecimalFormat("#.##");
			return Double.parseDouble(deFormat.format(returnValue));
		} catch (Exception e) {
			return 0.0;
		}
	}
	
	/**
	 * 计算两个时间的差
	 * @param string 前一时间
	 * @param string 后一时间
	 * @return double 时间差，单位为小时
	 * */
	public static double subTimeAsDouble(Calendar calPre, Calendar calNex) {
		try {
			long intPre = calPre.getTimeInMillis();
			long intNex = calNex.getTimeInMillis();
			long subValue = intPre - intNex;
			double returnValue = ((double) subValue) / 3600000;
			DecimalFormat deFormat = new DecimalFormat("#.##");
			return Double.parseDouble(deFormat.format(returnValue));
		} catch (Exception e) {
			return 0.0;
		}
	}
	
	/**
	 * 计算两个时间的差
	 * @param string 前一时间
	 * @param string 后一时间
	 * @return double 时间差，单位为小时
	 * */
	public static int subTimeAsInt(Calendar calPre, Calendar calNex) {
		try {
			long intPre = calPre.getTimeInMillis();
			long intNex = calNex.getTimeInMillis();
			long subValue = intPre - intNex;
			double returnValue = ((double) subValue) / 3600000;
			DecimalFormat deFormat = new DecimalFormat("#");
			return Integer.parseInt(deFormat.format(returnValue));
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 计算一时间加或减一段时间后的时间
	 * @param string 时间
	 * @param long 	 时间差 单位为小时
	 * @return string 时间
	 * */
	public static String addTime(String time, long value){
		try {
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calPre = new GregorianCalendar();
			calPre.setTime(format.parse(time));
			Calendar calNex = new GregorianCalendar();
			
			long intPre = calPre.getTimeInMillis();
			long intNex = intPre+(value*3600000);
			
			calNex.setTimeInMillis(intNex);
			
			return format.format(calNex.getTime());
		} catch (Exception e) {
			return time;
		}
	}
	
	/**
	 * 计算一时间加或减一段时间后的时间
	 * @param string 时间
	 * @param int 	 时间差 单位为月
	 * @return string 时间
	 * */
	public static String addTime(String time, int value){
		try {
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calPre = new GregorianCalendar();
			calPre.setTime(format.parse(time));
			calPre.add(Calendar.MONTH, value);
			
			return format.format(calPre.getTime());
		} catch (Exception e) {
			return time;
		}
	}
	
	/**
	 * 计算一周第N天的日期
	 * @param date--计算周次包含的时间
	 * @param int--1-7
	 * @return string 时间
	 * */
	public static String getDayofWeek(Date date,int day) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK,day);
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		return format.format(c.getTime());
	}
	
	/**
	 * 计算一周第N天的日期
	 * @param string--计算周次包含的时间
	 * @param int--1-7
	 * @return string 时间
	 * */
	public static String getDayofWeek(String date,int day) throws Exception {		
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date vDate=df.parse(date);		
		return getDayofWeek(vDate,day);
	}

	/**
	 * 计算当前日期所在月份的天数
	 * @param string--时间
	 * @return int 月份所含的天数
	 * */
	public static int getDaysOfMonth(String date) throws Exception{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date vDate=df.parse(date);		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(vDate);
		int month=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return month;
	}
	
	public static int getHourOfTime(Date date){
		DateFormat format = new SimpleDateFormat("HH");
		String hourStr = format.format(date);
		return Integer.parseInt(hourStr);
	}
	
	public static String getTimeNow(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	public static void main(String[]args){
		try{
			//log.info(DateUtil.getDaysOfMonth("2009-08-06 12:00:00"));
			log.info(DateUtil.getDayofWeek("2009-08-06 12:00:00",2));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
