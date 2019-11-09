package com.eagle.util;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
/**
 * 实现表格奇偶行间不同颜色区分
 */
public class PlainTableRowRenderer extends  DefaultTableCellRenderer {
     public   Component   getTableCellRendererComponent   (JTable   table,   Object   value,   boolean   isSelected,   boolean   hasFocus,   int   row,   int   column)   {
	     if("否".equals(table.getValueAt(row,3).toString().trim())){
	    	 setBackground(Color.decode("#ff7777"));//D3F0F9
	     }else if(row%2==0){
	         setBackground(Color.decode("#F3F6F5"));//D3F0F9
	     }else{
	         setBackground(Color.decode("#EDFEFA"));//D9FFED
	     }
	     return   super.getTableCellRendererComponent(table,   value,   isSelected,   hasFocus,   row,   column);
     }
}
 