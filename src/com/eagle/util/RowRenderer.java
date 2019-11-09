package com.eagle.util;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
/**
 * 实现表格奇偶行间不同颜色区分
 */
public class RowRenderer extends  DefaultTableCellRenderer {
     public   Component   getTableCellRendererComponent   (JTable   t,   Object   value,   boolean   isSelected,   boolean   hasFocus,   int   row,   int   column)   {
	     if   (row%2==0)
	         setBackground(Color.decode("#F3F6F5"));//D3F0F9
	     else
	         setBackground(Color.decode("#EDFEFA"));//D9FFED
	     return   super.getTableCellRendererComponent(t,   value,   isSelected,   hasFocus,   row,   column);
     }
}
 