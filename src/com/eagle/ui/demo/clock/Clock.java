package com.eagle.ui.demo.clock;

import java.awt.BorderLayout;

import javax.swing.*;

public class Clock extends JFrame {
	private static final long serialVersionUID = 3513485648301906948L;

	public Clock() {
		ClockPaint cp = new ClockPaint(20, 20, 70);

		// JButton bn = new JButton("xxxxxxxxx");
		// JPanel pan = new JPanel();
		// pan.add(bn);
		// this.add(BorderLayout.NORTH, bn);
		JLabel lab = new JLabel("时钟");
		// lab.add(bn);
		// lab.setOpaque(false);
		//lab.setBackground(null);
		this.add(BorderLayout.NORTH, lab);

		this.add(cp);
		this.setSize(300, 320);
		this.setResizable(false);
		this.setLocation(260, 120);
		this.setTitle("小时钟");
		this.setVisible(true);
	}

	public static void main(String[] s) {
		new Clock();
	}
}
