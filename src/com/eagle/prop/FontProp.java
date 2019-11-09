package com.eagle.prop;

import java.awt.Font;

public class FontProp {
	public static void setFont_b_18_h(java.awt.Component comp){
		Font f=new Font("华文行楷",Font.BOLD,18);
		comp.setFont(f);
	}
	
	public static void setFont_b_20_s(java.awt.Component comp){
		Font f=new Font("宋体",Font.BOLD,20);
		comp.setFont(f);
	}
	
	public static void setFont_b_10_s(java.awt.Component comp){
		Font f=new Font("宋体",Font.CENTER_BASELINE,10);
		comp.setFont(f);
	}
}
