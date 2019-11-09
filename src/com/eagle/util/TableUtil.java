package com.eagle.util;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
/**
 * 将表格行之间用不同颜色区别
 * */
public class TableUtil {
	public static void paintRow(JTable table) {
        TableColumnModel tcm = table.getColumnModel();
        for (int i=0, n=tcm.getColumnCount();i<n;i++){
	        TableColumn tc = tcm.getColumn(i);
	        tc.setCellRenderer(new RowRenderer());
        }
    }
	
	public static void paintPlainRow(JTable table) {
        TableColumnModel tcm = table.getColumnModel();
        setColumnSize(tcm, 0, 100);
        setColumnSize(tcm, 1, 150);
        setColumnSize(tcm, 2, 235);
        setColumnSize(tcm, 3, 50);
        for (int i=0, n=tcm.getColumnCount();i<n;i++){
	        TableColumn tc = tcm.getColumn(i);
	        tc.setCellRenderer(new PlainTableRowRenderer());
        }
    }
	
	public static void setColumnSize(TableColumnModel cm, int i, int width) {
		TableColumn   column  = cm.getColumn(i);//得到第i个列对象   
		column.setPreferredWidth(width);//将此列的首选宽度设置为 preferredWidth。
		//如果 preferredWidth 超出最小或最大宽度，则将其调整为合适的界限值。 
	}
}
