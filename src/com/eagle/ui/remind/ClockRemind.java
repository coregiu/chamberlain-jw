package com.eagle.ui.remind;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.eagle.action.MediaAction;
import com.eagle.data.ImageData;
import com.eagle.prop.UIProp;

/**
 * 
 * 闹钟提醒
 * 
 * @author szhang
 */
public class ClockRemind extends JFrame implements Runnable, MouseListener, ActionListener {

	private static final long serialVersionUID = -3564453685861233338L;

	private Logger log = Logger.getLogger(ClockRemind.class);
	private Thread clkThread;
	private JButton bt = new JButton("停止");
	MediaAction mediaAction = new MediaAction();
	public ClockRemind(String clockStr) {
		ClockPaint cp = new ClockPaint(25, 30, 120, clockStr);

		this.add(cp);
		this.add(BorderLayout.SOUTH, bt);
		bt.setActionCommand("stop");
		bt.addActionListener(this);
		this.setResizable(false);
		UIProp.setUIScreen(this, new Dimension(300, 450));
		this.setTitle("闹钟提醒");
		this.setIconImage(ImageData.logo);
		this.setVisible(true);
		clkThread = new Thread(this);
		clkThread.start();
		mediaAction.play("");
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		stop();
	}
	
	public void stop(){
		this.setVisible(false);
		this.dispose();
		mediaAction.stop();
	}
	
	public void start(){
		this.setVisible(true);
		mediaAction.play("");
	}
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if("stop".equals(e.getActionCommand())){
			stop();
		}
	}

	public static void main(String args[]) {
		new ClockRemind("baby come on ");
	}
}