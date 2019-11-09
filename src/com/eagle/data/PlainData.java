package com.eagle.data;

import java.util.List;

/**
 * 工作学习计划数据
 * 
 * @author szhang
 *
 */
public class PlainData {
	private String date;
	
	private String id;
	
	private String title;
	
	private String content;
	
	private String completed;
	
	private List plainData;

	private PlainData  currentPlainData;
	
	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List getPlainData() {
		return plainData;
	}

	public void setPlainData(List plainData) {
		this.plainData = plainData;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public PlainData getCurrentPlainData() {
		return currentPlainData;
	}

	public void setCurrentPlainData(PlainData currentPlainData) {
		this.currentPlainData = currentPlainData;
	}
}
