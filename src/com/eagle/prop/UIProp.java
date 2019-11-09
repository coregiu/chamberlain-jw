package com.eagle.prop;

import java.awt.Dimension;
import java.awt.Toolkit;

public class UIProp {
	
	/**
	 * 设置窗体大小和位置
	 * */
	public static void setUIScreen(java.awt.Window window, Dimension bodySize){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(screenSize.width / 2 - (bodySize.width / 2),
				screenSize.height / 2 - (bodySize.height / 2));
		window.setSize(bodySize);
	}
	
	/**
	 * 设置窗体位置
	 * */
	public static void setUIScreen(java.awt.Window window){
		Dimension bodySize = window.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(screenSize.width / 2 - (bodySize.width / 2),
				screenSize.height / 2 - (bodySize.height / 2));
	}
}
