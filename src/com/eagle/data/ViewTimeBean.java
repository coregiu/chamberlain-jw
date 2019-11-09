package com.eagle.data;

import java.util.Date;

/**
 * 查询时间
 */
public class ViewTimeBean {
	// 统计类型 0-详情；1-统计
	private char type;

	// 统计起始时间
	private String bgTime;

	// 统计起始时间
	private String edTime;

	// 统计起始时间
	private Date bgCal;

	// 统计起始时间
	private Date edCal;

	// 统计详细类型
	private String dtType;

	// 统计时段
	private String dpType;

	public String getDpType() {
		return dpType;
	}

	public void setDpType(String dpType) {
		this.dpType = dpType;
	}

	public String getDtType() {
		return dtType;
	}

	public void setDtType(String dtType) {
		this.dtType = dtType;
	}

	public String getBgTime() {
		return bgTime;
	}

	public void setBgTime(String bgTime) {
		this.bgTime = bgTime;
	}

	public String getEdTime() {
		return edTime;
	}

	public void setEdTime(String edTime) {
		this.edTime = edTime;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public Date getBgCal() {
		return bgCal;
	}

	public void setBgCal(Date bgCal) {
		this.bgCal = bgCal;
	}

	public Date getEdCal() {
		return edCal;
	}

	public void setEdCal(Date edCal) {
		this.edCal = edCal;
	}
}
