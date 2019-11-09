package com.eagle.action;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;

import jxl.format.*;

public class ExcelAction {
	/**
	 * 将表格数据导入EXCEL
	 * @param file
	 * @param tHead
	 * @param tData
	 * @return
	 */
	public static boolean grt2Excel(String title, String file, Vector<String> tHead, Vector<Vector> tData){
		Logger log = Logger.getLogger(ExcelAction.class);
		try{
			OutputStream os = new FileOutputStream(file);
			jxl.write.WritableWorkbook wwb = jxl.Workbook.createWorkbook(os); // 创建Excel工作表
			jxl.write.WritableSheet ws = wwb.createSheet(title, 0);

			jxl.write.WritableCellFormat wc = new jxl.write.WritableCellFormat();

			// 设置居中
			wc.setAlignment(Alignment.CENTRE);
			// 设置边框线
			wc.setBorder(Border.ALL, BorderLineStyle.THIN);
			// 设置单元格的背景颜色
			wc.setBackground(jxl.format.Colour.GREY_40_PERCENT);
			// wc.s

			jxl.write.WritableCellFormat wc1 = new jxl.write.WritableCellFormat();
			wc1.setBorder(Border.ALL, BorderLineStyle.THIN);

			jxl.write.WritableCellFormat wc2 = new jxl.write.WritableCellFormat();
			wc2.setBorder(Border.ALL, BorderLineStyle.THIN);
			wc2.setAlignment(Alignment.RIGHT);

			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#");

			jxl.write.WritableCellFormat wcf = new jxl.write.WritableCellFormat(
					nf);
			wcf.setBorder(Border.ALL, BorderLineStyle.THIN);

			jxl.write.Label label_head;

			int i=0;
			// 表头
			for (String aHeadStr: tHead) {
				label_head = new jxl.write.Label(i++, 0, aHeadStr, wc);
				ws.addCell(label_head);
			}

			// 结果集中的数据添加到excel中
			i=1;
			int j=0;
			for (Vector<String> aVc:tData) {
				j=0;
				for(Object aData: aVc){
					ws.addCell(new jxl.write.Label(j++, i, aData.toString(), wc1));
				}
				i++;
			}

			wwb.write();
			wwb.close();
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
}
