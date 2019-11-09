package com.eagle.ui.demo.clock;

import java.awt.*;
import javax.swing.*;
import java.util.*;

class ClockPaint extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	int x, y, r;

	int h, m, s;// 小时，分钟，秒

	double rad = Math.PI / 180;

	public ClockPaint(int x, int y, int r) {
		this.x = x;
		this.y = y;
		this.r = r;
		Calendar now = new GregorianCalendar();
		s = now.get(Calendar.SECOND) * 6;// 获得秒转换成度数
		m = now.get(Calendar.MINUTE) * 6;// 获得分钟
		h = (now.get(Calendar.HOUR_OF_DAY) - 12) * 30
				+ now.get(Calendar.MINUTE) / 12 * 6;// 获得小时

		Thread t = new Thread(this);
		t.start();
	}

	public void paint(Graphics g) {
		// 清屏
		super.paint(g);
		//g.setColor(null);
		Image img = new ImageIcon("c:/2.jpg").getImage();
		g.drawImage(img, 0, 0, null);
		// g.fillRect(0, 0, r * 3, r * 3);
		// 画圆
		g.setColor(Color.WHITE);
		g.drawOval(x, y, r * 2, r * 2);
		// 秒针
		g.setColor(Color.RED);
		int x1 = (int) ((r - 10) * Math.sin(rad * s));
		int y1 = (int) ((r - 10) * Math.cos(rad * s));
		g.drawLine(x + r, y + r, x + r + x1, y + r - y1);
		// 分针
		g.setColor(Color.BLUE);
		x1 = (int) ((r - r / 2.5) * Math.sin(rad * m));
		y1 = (int) ((r - r / 2.5) * Math.cos(rad * m));
		g.drawLine(x + r, y + r, x + r + x1, y + r - y1);
		// 时针
		g.setColor(Color.CYAN);
		x1 = (int) ((r - r / 1.5) * Math.sin(rad * h));
		y1 = (int) ((r - r / 1.5) * Math.cos(rad * h));
		g.drawLine(x + r, y + r, x + r + x1, y + r - y1);
		// 数字
		g.setColor(Color.YELLOW);
		int d = 29;
		for (int i = 1; i <= 12; i++) {
			x1 = (int) ((r - 10) * Math.sin(rad * d));
			y1 = (int) ((r - 10) * Math.cos(rad * d));
			g.drawString(i + "", x + r + x1 - 4, x + r - y1 + 5);
			d += 30;
		}
		// 小点
		d = 0;
		for (int i = 0; i < 60; i++) {
			x1 = (int) ((r - 2) * Math.sin(rad * d));
			y1 = (int) ((r - 2) * Math.cos(rad * d));
			g.drawString(".", x + r + x1 - 1, x + r - y1 + 1);
			d += 6;
		}
		// 显示时间
		Calendar now1 = new GregorianCalendar();
		g.drawString(now1.get(Calendar.HOUR_OF_DAY) + "："
				+ now1.get(Calendar.MINUTE) + "：" + now1.get(Calendar.SECOND),
				0, 10);
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
			}
			s += 6;
			if (s >= 360) {
				s = 0;
				m += 6;
				if (m == 72 || m == 144 || m == 216 || m == 288) {
					h += 6;
				}
				if (m >= 360) {
					m = 0;
					h += 6;
				}
				if (h >= 360) {
					h = 0;
				}
			}
			this.repaint();
		}
	}
}