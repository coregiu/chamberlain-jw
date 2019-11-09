package com.eagle.ui.file;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * �ļ�ѡ�񣬽ڵ����
 * 
 * @author zhangsai
 * @time 20091102
 */
public class FileNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;

	private boolean explored = false;

	public FileNode(File file) {
		setUserObject(file);
	}

	public boolean getAllowsChildren() {
		return isDirectory();
	}

	public boolean isLeaf() {
		return !isDirectory();
	}

	public File getFile() {
		return (File) getUserObject();
	}

	public boolean isExplored() {
		return explored;
	}

	public boolean isDirectory() {
		File file = getFile();
		return file.isDirectory();
	}

	public String toString() {
		File file = (File) getUserObject();
		String filename = file.toString();
		int index = filename.lastIndexOf(File.separator);
		return (index != -1 && index != filename.length() - 1) ? filename.substring(index + 1) : filename;
	}

	public File explore() {
		File file = getFile();
		if (!isDirectory())
			return file;
		if (!isExplored()) {
			File[] children = file.listFiles();
			for (int i = 0; i < children.length; i++) {
				if (children[i].isDirectory()) {
					add(new FileNode(children[i]));
				}
			}
			for (int i = 0; i < children.length; i++) {
				if (!children[i].isDirectory()) {
					add(new FileNode(children[i]));
				}
			}
			explored = true;
		}
		return null;
	}

	public void reexplore() {
		this.removeAllChildren();
		explored = false;
		explore();
	}
}
