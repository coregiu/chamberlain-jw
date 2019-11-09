package com.eagle.data;

import java.text.*;
import java.util.*;

/**
 * 日记数据
 * 
 * @author szhang
 * 
 */
public class DiaryData {
	private boolean diaryFlag;
	
	private String date;

	private String item;

	private String content;
	
	private String rename;
	
	private Hashtable<String, Hashtable<String, DiaryData>> diaryData = new Hashtable<String, Hashtable<String, DiaryData>>();
	
	private Hashtable<String, Vector<String>> diaryMenu = new Hashtable<String, Vector<String>>();
	
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private List<DiaryData> childData;

	public List<DiaryData> getChildData() {
		return childData;
	}

	public void setChildData(List<DiaryData> childData) {
		this.childData = childData;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	
	public String parseDate(Date date){
		return df.format(date);
	}

	public Hashtable<String, Hashtable<String, DiaryData>> getDiaryData() {
		return diaryData;
	}

	public void setDiaryData(Hashtable<String, Hashtable<String, DiaryData>> diaryData) {
		this.diaryData = diaryData;
	}

	public Hashtable<String, Vector<String>> getDiaryMenu() {
		return diaryMenu;
	}

	public void setDiaryMenu(Hashtable<String, Vector<String>> diaryMenu) {
		this.diaryMenu = diaryMenu;
	}

	public String getRename() {
		return rename;
	}

	public void setRename(String rename) {
		this.rename = rename;
	}

	public boolean isDiaryFlag() {
		return diaryFlag;
	}

	public void setDiaryFlag(boolean diaryFlag) {
		this.diaryFlag = diaryFlag;
	}
}
