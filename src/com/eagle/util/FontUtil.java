package com.eagle.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComponent;

public class FontUtil {
	public static void setFont(Component cmp){
		cmp.setFont(new Font("宋体",Font.BOLD,20));
	}
	
	public static void setToolButtonSize(JComponent comp){
		comp.setPreferredSize(new Dimension(20, 20));
	}
}
