package com.eagle.ui.file;

import java.awt.*;

import javax.swing.*;

import com.eagle.prop.UIProp;

/**
 * 文件路径选择窗体����
 * 
 * @author zhangsai
 * @time 20091102
 */
public class SelectFilePath  extends JDialog{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数，加载文件树
	 * @param JFrame 父窗体
	 * @param JTextField 文件选择后的输出目标框
	 * */
	public SelectFilePath(Dialog f){
		super(f, "选择文件路径", true);
		FileList fileList = new FileList(this);
		UIProp.setUIScreen(this, new Dimension(400,500));
		Container content = this.getContentPane();
		content.setLayout(new FlowLayout());
		content.add(fileList.createTreePanel());
		this.setContentPane(content);
		this.setResizable(false);
		this.setVisible(true);
		this.setFilePath(fileList.getPath());
	}
	
	/**
	 * 构造函数，加载文件树
	 * @param JFrame 父窗体
	 * @param JTextField 文件选择后的输出目标框
	 * */
	public SelectFilePath(Frame f) {
		super(f, "选择文件路径", true);
		FileList fileList = new FileList(this);
		UIProp.setUIScreen(this, new Dimension(400,500));
		Container content = this.getContentPane();
		content.setLayout(new FlowLayout());
		content.add(fileList.createTreePanel());
		this.setContentPane(content);
		this.setVisible(true);
		this.setFilePath(fileList.getPath());
	}
	
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
