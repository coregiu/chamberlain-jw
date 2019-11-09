package com.eagle.printer;

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;

import com.eagle.ui.diary.MyBook;

public class MyPrinter implements Printable {

	private MyBook editor;

	private int PAGES = 1;

	private int ROWSAPAGE = 54;

	public MyPrinter(MyBook editor, JFrame f) {
		this.editor = editor;
		PAGES = getPagesCount(editor.getText());
	}

	/**
	 * Graphic指明打印的图形环境；PageFormat指明打印页格式（页面大小以点为计量单位，
	 * 1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×842点）；page指明页号
	 */
	public int print(Graphics g, PageFormat pf, int page)
			throws PrinterException {
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.black); // 设置打印颜色为黑色
		if (page >= PAGES) {// 当打印页号大于需要打印的总页数时，打印工作结束
			return Printable.NO_SUCH_PAGE;
		}
		g2.translate(pf.getImageableX(), pf.getImageableY());// 转换坐标，确定打印边界
		drawCurrentPageText(g2, pf, page); // 打印当前页文本
		return Printable.PAGE_EXISTS; // 存在打印页时，继续打印工作
	}

	/* 打印指定页号的具体文本内容 */
	private void drawCurrentPageText(Graphics2D g2, PageFormat pf, int page) {
		String s = getDrawText(editor.getText())[page];// 获取当前页的待打印文本内容
		// 获取默认字体及相应的尺寸
		Font f = editor.getFont();
		String drawText;
		float ascent = 16; // 给定字符点阵
		int k, i = f.getSize(), lines = 0;
		while (s.length() > 0 && lines < ROWSAPAGE) // 每页限定在ROWSAPAGE行以内
		{
			k = s.indexOf('\n'); // 获取每一个回车符的位置
			if (k != -1) // 存在回车符
			{
				lines += 1; // 计算行数
				drawText = s.substring(0, k); // 获取每一行文本
				g2.drawString(drawText, 0, ascent); // 具体打印每一行文本，同时走纸移位
				if (s.substring(k + 1).length() > 0) {
					s = s.substring(k + 1); // 截取尚未打印的文本
					ascent += i;
				}
			} else // 不存在回车符
			{
				lines += 1; // 计算行数
				drawText = s; // 获取每一行文本
				g2.drawString(drawText, 0, ascent); // 具体打印每一行文本，同时走纸移位
				s = ""; // 文本已结束
			}
		}
	}

	/* 将打印目标文本按页存放为字符串数组 */
	public String[] getDrawText(String s) {
		String[] drawText = new String[PAGES];// 根据页数初始化数组
		for (int i = 0; i < PAGES; i++)
			drawText[i] = ""; // 数组元素初始化为空字符串

		int k, suffix = 0, lines = 0;
		while (s.length() > 0) {
			if (lines < ROWSAPAGE) // 不够一页时
			{
				k = s.indexOf('\n');
				if (k != -1) // 存在回车符
				{
					lines += 1; // 行数累加
					// 计算该页的具体文本内容，存放到相应下标的数组元素
					drawText[suffix] = drawText[suffix] + s.substring(0, k + 1);
					if (s.substring(k + 1).length() > 0)
						s = s.substring(k + 1);
				} else {
					lines += 1; // 行数累加
					// 将文本内容存放到相应的数组元素
					drawText[suffix] = drawText[suffix] + s;
					s = "";
				}
			} else // 已满一页时
			{
				lines = 0; // 行数统计清零
				suffix++; // 数组下标加1
			}
		}
		return drawText;
	}

	/**
	 * 2、计算需要打印的总页数
	 * 
	 * @param curStr
	 * @return
	 */
	public int getPagesCount(String curStr) {
		int page = 0;
		int position, count = 0;
		String str = curStr;
		while (str.length() > 0) {// 文本尚未计算完毕
			position = str.indexOf('\n'); // 计算回车符的位置
			count += 1; // 统计行数
			if (position != -1)
				str = str.substring(position + 1); // 截取尚未计算的文本
			else
				str = ""; // 文本已计算完毕
		}
		if (count > 0)
			page = count / ROWSAPAGE + 1; // 以总行数除以ROWSAPAGE获取总页数
		return page; // 返回需打印的总页数
	
		/*editor.get
		g2.setColor(Color.black);
		int fontHeight = g2.getFontMetrics().getHeight();
		int fontDesent = g2.getFontMetrics().getDescent();

		// leave room for page number
		double pageHeight = pageFormat.getImageableHeight() - fontHeight;
		double pageWidth = pageFormat.getImageableWidth();
		double tableWidth = (double) editor.getWidth();
		double scale = 1;
		if (tableWidth >= pageWidth) {
			scale = pageWidth / tableWidth;
		}

		double headerHeightOnPage = editor.getHeight() * scale;
		double tableWidthOnPage = tableWidth * scale;

		double oneRowHeight = (tableView.getRowHeight() + tableView.getRowMargin())* scale;
		int numRowsOnAPage = (int) ((pageHeight - headerHeightOnPage) / oneRowHeight);
		double pageHeightForTable = oneRowHeight * numRowsOnAPage;
		int totalNumPages = (int) Math.ceil(((double) tableView.getRowCount())/numRowsOnAPage);*/
	}
	
	/*

	public void printTextAction() {
		printStr = editor.getText().trim(); // 获取需要打印的目标文本
		if (printStr != null && printStr.length() > 0) // 当打印内容不为空时
		{
			PAGES = getPagesCount(printStr); // 获取打印总页数
			PrinterJob myPrtJob = PrinterJob.getPrinterJob(); // 获取默认打印作业
			PageFormat pageFormat = myPrtJob.defaultPage(); // 获取默认打印页面格式
			myPrtJob.setPrintable(this, pageFormat); // 设置打印工作
			if (myPrtJob.printDialog()) // 显示打印对话框
			{
				try {
					myPrtJob.print(); // 进行每一页的具体打印操作
				} catch (PrinterException pe) {
					pe.printStackTrace();
				}
			}
		} else {
			// 如果打印内容为空时，提示用户打印将取消
			JOptionPane.showConfirmDialog(null,
					"Sorry, Printer Job is Empty, Print Cancelled!", "Empty",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
		}
	}

	*//**
	 * 打印
	 * 
	 *//*
	public void printText2Action() {
		printStr = editor.getText().trim();// 获取需要打印的目标文本
		if (printStr != null && printStr.length() > 0) // 当打印内容不为空时
		{
			PAGES = getPagesCount(printStr); // 获取打印总页数
			// 指定打印输出格式
			DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
			// 定位默认的打印服务
			PrintService printService = PrintServiceLookup
					.lookupDefaultPrintService();
			// 创建打印作业
			DocPrintJob job = printService.createPrintJob();
			// 设置打印属性
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			DocAttributeSet das = new HashDocAttributeSet();
			// 指定打印内容
			Doc doc = new SimpleDoc(this, flavor, das);
			// 不显示打印对话框，直接进行打印工作
			try {
				job.print(doc, pras); // 进行每一页的具体打印操作
			} catch (PrintException pe) {
				pe.printStackTrace();
			}
		} else {
			// 如果打印内容为空时，提示用户打印将取消
			JOptionPane.showConfirmDialog(null,
					"Sorry, Printer Job is Empty, Print Cancelled!", "Empty",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
		}
	}

	 打印指定的窗体及其包含的组件 
	public void printFrameAction() {
		Toolkit kit = Toolkit.getDefaultToolkit(); // 获取工具箱
		Properties props = new Properties();
		props.put("awt.print.printer", "durango");// 设置打印属性
		props.put("awt.print.numCopies", "2");
		if (kit != null) {
			// 获取工具箱自带的打印对象
			PrintJob printJob = kit.getPrintJob(parentFrame, "Print Frame",
					props);
			if (printJob != null) {
				Graphics pg = printJob.getGraphics();// 获取打印对象的图形环境
				if (pg != null) {
					try {
						this.printAll(pg);// 打印该窗体及其所有的组件
					} finally {
						pg.dispose();// 注销图形环境
					}
				}
				printJob.end();// 结束打印作业
			}
		}
	}

	 打印指定的文件 
	public void printFileAction() {
		// 构造一个文件选择器，默认为当前目录
		JFileChooser fileChooser = new JFileChooser();
		int state = fileChooser.showOpenDialog(parentFrame);// 弹出文件选择对话框
		if (state == JFileChooser.APPROVE_OPTION)// 如果用户选定了文件
		{
			File file = fileChooser.getSelectedFile();// 获取选择的文件
			// 构建打印请求属性集
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			// 设置打印格式，因为未确定文件类型，这里选择AUTOSENSE
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			// 查找所有的可用打印服务
			PrintService printService[] = PrintServiceLookup
					.lookupPrintServices(flavor, pras);
			// 定位默认的打印服务
			PrintService defaultService = PrintServiceLookup
					.lookupDefaultPrintService();
			// 显示打印对话框
			PrintService service = ServiceUI.printDialog(null, 200, 200,
					printService, defaultService, flavor, pras);
			if (service != null) {
				try {
					DocPrintJob job = service.createPrintJob();// 创建打印作业
					FileInputStream fis = new FileInputStream(file);// 构造待打印的文件流
					DocAttributeSet das = new HashDocAttributeSet();
					Doc doc = new SimpleDoc(fis, flavor, das);// 建立打印文件格式
					job.print(doc, pras);// 进行文件的打印
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void printAll(Graphics pg) {

	}*/
}
