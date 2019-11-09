package com.eagle.data;

import java.util.*;

public class ClockData {
	// 提醒方式:0-闹钟式；1-右下角便签式
	private String remindType;
	
	// 提醒日期
	private String remindDate;
	
	// 提醒时间，时：分
	private String remindTime;
	
	// 是否重复, 非重复型提醒在闹钟提醒后自动清除闹钟：0-每天都提醒；1-工作提醒；2-非工作日提醒；yyyy-MM-dd特定的某天
	private String isRepeat;
	
	// 提醒内容
	private String remindContent;
	
	// 是否有效
	private String isValid;

	// 闹钟列表
	private Hashtable<String, ClockData> clkDataList;
	
	public String getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(String isRepeat) {
		this.isRepeat = isRepeat;
	}

	public String getRemindDate() {
		return remindDate;
	}

	public void setRemindDate(String remindDate) {
		this.remindDate = remindDate;
	}

	public String getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(String remindTime) {
		this.remindTime = remindTime;
	}

	public String getRemindType() {
		return remindType;
	}

	public void setRemindType(String remindType) {
		this.remindType = remindType;
	}

	public Hashtable<String, ClockData> getClkDataList() {
		return clkDataList;
	}

	public void setClkDataList(Hashtable<String, ClockData> clkDataList) {
		this.clkDataList = clkDataList;
	}

	public String getRemindContent() {
		return remindContent;
	}

	public void setRemindContent(String remindContent) {
		this.remindContent = remindContent;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
}
