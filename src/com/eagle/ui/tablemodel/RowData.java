package com.eagle.ui.tablemodel;

import java.util.Vector;

public class RowData {
	Vector<Object> cells;

	int size;

	int row;

	public RowData(int row, int size) {
		super();
		this.size = size;
		cells = new Vector<Object>();

	}

	public void addValue(Object obj) {
		cells.add(obj);
	}

	public int updateValue(int col, Object obj) {
		if (col < size)
			cells.set(col, obj);
		return size - col;
	}
}
