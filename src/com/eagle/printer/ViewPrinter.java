package com.eagle.printer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;

import javax.swing.JDialog;

public class ViewPrinter extends JDialog {
	private static final long serialVersionUID = -1767914513164189380L;

	/* 打印预览 将待打印内容按比例绘制到屏幕 */
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2 = (Graphics2D) g;
		PageFormat pf = PrinterJob.getPrinterJob().defaultPage(); // 获取页面格式
		double xoff; // 在屏幕上页面初始位置的水平偏移
		double yoff; // 在屏幕上页面初始位置的垂直偏移
		double scale; // 在屏幕上适合页面的比例
		double px = pf.getWidth(); // 页面宽度
		double py = pf.getHeight(); // 页面高度
		double sx = getWidth() - 1;
		double sy = getHeight() - 1;
		if (px / py < sx / sy) {
			scale = sy / py; // 计算比例
			xoff = 0.5 * (sx - scale * px); // 水平偏移量
			yoff = 0;
		} else {
			scale = sx / px; // 计算比例
			xoff = 0;
			yoff = 0.5 * (sy - scale * py); // 垂直偏移量
		}
		g2.translate((float) xoff, (float) yoff); // 转换坐标
		g2.scale((float) scale, (float) scale);
		Rectangle2D page = new Rectangle2D.Double(0, 0, px, py); // 绘制页面矩形
		g2.setPaint(Color.white); // 设置页面背景为白色
		g2.fill(page);
		g2.setPaint(Color.black);// 设置页面文字为黑色
		g2.draw(page);
		try {
			//preview.print(g2, pf, currentPage); // 显示指定的预览页面
		} catch (Exception pe) {
			g2.draw(new Line2D.Double(0, 0, px, py));
			g2.draw(new Line2D.Double(0, px, 0, py));
		}
	}

	/* 预览指定的页面 */
	public void viewPage(int pos) {
		/*int newPage = currentPage + pos;
		// 指定页面在实际的范围内
		if (0 <= newPage && newPage < preview.getPagesCount(printStr)) {
			currentPage = newPage; // 将指定页面赋值为当前页
			repaint();
		}*/
	}
}
