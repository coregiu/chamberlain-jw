package com.eagle.ui.mycalendar;

import java.awt.Canvas;

import java.awt.Color;

import java.awt.Font;

import java.awt.Graphics;

import java.text.SimpleDateFormat;

import java.util.Calendar;

/**
 * 电子时钟
 * @author eagle
 *
 */
class TIME extends Canvas implements Runnable {
	private static final long serialVersionUID = 3660124045489727166L;

	MyCalendar mf;
	Thread t;
	String time;
	public TIME(MyCalendar mf) {
		this.mf = mf;
		setSize(620, 60);
		setBackground(null);
		t = new Thread(this);
		t.start();
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			}catch (InterruptedException e) {
				System.out.println("error");
			}
			this.repaint(100);
		}
	}

	public void paint(Graphics g) {
		Font f = new Font(" ", Font.BOLD, 23);
		SimpleDateFormat SDF = new SimpleDateFormat("'公元 'yyyy'年'MM'月'dd'日 'HH'时'mm'分'ss'秒'");
		Calendar now = Calendar.getInstance();
		time = SDF.format(now.getTime());
		g.setFont(f);
		g.setColor(Color.BLACK);
		g.drawString(time, 80, 25);
	}
}