package com.eagle.common;

import java.io.*;
import javax.swing.*;

/**
 * 系统路径管理
 * */
public class URLManager{
	private static String filePathStr;// 系统路径
	
	private static String urlStr;// 系统URL
	
	static {
		try{
			File workDir=new File("");
			String workDirPath=workDir.getAbsolutePath();
			filePathStr = workDirPath+"/";
			urlStr= "file:\\"+filePathStr;
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "不能正确读取文件工作路径，请检查系统安装文件！");
			System.exit(0);
		}
	}
	/**
	 * 获取系统工作目录
	 * */
	public static String getWorkDirPath(){
		return filePathStr;
	}
	
	/**
	 * 获取系统工作URL
	 * */
	public static String getWorDirURL(){
		return urlStr;
	}
}