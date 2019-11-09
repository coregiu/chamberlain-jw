package com.eagle.ui.diary;

import javax.swing.*;

@SuppressWarnings("serial")
public class TestJframe extends JFrame {
	public TestJframe(){
		/*MyBook editor= new MyBook();
		editor.setText("12334563456");
		editor.select(0, 6);*/
		DiaryPanel diaryPanel = new DiaryPanel(this);
		this.getContentPane().add(diaryPanel.getDiaryPanel(0));
		this.pack();
		this.setVisible(true);
	}
	public static void main(String[] args) {
		new TestJframe();
	}
}
