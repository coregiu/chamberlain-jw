package com.eagle.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.eagle.data.*;
import com.eagle.prop.UIProp;
/**
 * 系统启动前的闪屏效果
 * */
public class SplashWindow extends JWindow {
	private static final long serialVersionUID = 6092446459138714784L;

	public SplashWindow(MainFrame f) {
		super(f);
		this.toFront();
		this.setVisible(true);
		JLabel label = new JLabel(ImageData.splashEagle);
		getContentPane().add(label, BorderLayout.CENTER);
		pack();
		UIProp.setUIScreen(this, label.getPreferredSize());
		
		addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					setVisible(false);
					dispose();
				}
			});
		
		
		final int pause = Integer.parseInt(ConfBean.splashTime);//设置闪屏时间
		final Runnable closerRunner = new Runnable() {
			public void run() {
				setVisible(false);
				dispose();
			}
		};
		
		Runnable loadData = new Runnable(){
			public void run(){
				try {
					Thread.sleep(pause);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		Runnable waitRunner = new Runnable() {
			public void run() {
				try {
					SwingUtilities.invokeAndWait(closerRunner);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		Thread splashThread = new Thread(waitRunner, "SplashThread");
		Thread loadDataThread = new Thread(loadData,"LoadData");
		try{
			loadDataThread.start();
			while(loadDataThread.isAlive()){
				continue;
			}
			splashThread.start();
			f.loadMainFrame();
			f.setVisible(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}