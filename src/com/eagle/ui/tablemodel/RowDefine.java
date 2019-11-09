package com.eagle.ui.tablemodel;

public class RowDefine {
	String colname;

	String annotation;

	int pos;

	String dataType;

	boolean fixed;

	String maxLen = null;

	int fracDig = 0;

	String fracDigStr = "";

	public boolean getFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getColname() {
		return colname;
	}

	public void setColname(String colname) {
		this.colname = colname;
	}

	public RowDefine() {
		super();
	}

	public RowDefine(String colname, String annotation, int pos,
			String dataType, boolean fixed) {
		super();
		this.colname = colname;
		this.annotation = annotation;
		this.pos = pos;
		this.dataType = dataType;
		this.fixed = fixed;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(String maxLen) {
		this.maxLen = maxLen;
	}

	/*
	 * @Override public boolean equals(Object obj) { return this.colname.equals(obj); }
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getAnnotation();
	}

	public int getFracDig() {
		return fracDig;
	}

	public void setFracDig(String fracDig) {
		this.fracDig = Integer.valueOf(fracDig);
		this.fracDigStr = "";
		for (int i = 0; i < this.fracDig; i++) {
			this.fracDigStr += "#";
		}
	}

	public String getFracDigStr() {
		return fracDigStr;
	}
}
