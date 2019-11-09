package com.eagle.data;

import java.awt.*;
import javax.swing.*;

import com.eagle.util.*;
/**
 * 图片数据
 * */
public class ImageData {
	public static Image logo;
	
	public static ImageIcon logoIcon;
	
	public static ImageIcon bodyPic;
	
	public static ImageIcon splashEagle;
	
	public static ImageIcon newUser;
	
	public static ImageIcon login;

	public static ImageIcon updatePass;

	public static ImageIcon deleteUser;
	
	public static ImageIcon importData;

	public static ImageIcon exportData;
	
	public static ImageIcon exit;

	public static ImageIcon iConf;

	public static ImageIcon oConf;
	
	public static ImageIcon bak;

	public static ImageIcon restore;
	
	public static ImageIcon calendar;
	
	public static ImageIcon plain;
	
	public static ImageIcon clock;
	
	public static ImageIcon calc;
	
	public static ImageIcon diary;
	
	public static ImageIcon lettery;

	public static ImageIcon iReport;

	public static ImageIcon oReport;

	public static ImageIcon ioReport;
	
	public static ImageIcon config;
	
	public static ImageIcon mmrdate;
	
	public static ImageIcon about;
	
	public static ImageIcon help;
	
	public static ImageIcon eagle;
	
	public static ImageIcon completed;
	
	public static ImageIcon unplain;
	
	public static ImageIcon clockBg;
	
	public static ImageIcon add;
	
	public static ImageIcon update;
	
	public static ImageIcon commit;
	
	public static ImageIcon ntmt;
	
	// 日记本图标
	public static ImageIcon save;
	
	public static ImageIcon search;
	
	public static ImageIcon printer;
	
	public static ImageIcon undo;
	
	public static ImageIcon redo;
	
	public static ImageIcon cut;
	
	public static ImageIcon copy;
	
	public static ImageIcon paste;

	public static ImageIcon bolder;

	public static ImageIcon italic;

	public static ImageIcon underline;
	
	public static ImageIcon color;
	
	public static ImageIcon font;
	
	public static ImageIcon newDiary;
	
	public static ImageIcon refresh;
	
	public static ImageIcon delete;
	
	public static ImageIcon rename;
	
	static{
		try{
			String logoStr;
			String splashEagleStr;
			String bodyPicStr;
			
			
			// 设置闪屏和图标的图片
			logoStr = "logo/logo.png";
			if("1".equals(ConfBean.type)){
				splashEagleStr = "logo/splashuu.jpg";
				int radomNo = (int)((3)*Math.random());
				// 设置窗体图片
				bodyPicStr = "logo/hong"+String.valueOf(radomNo)+".jpg";
			}else{
				splashEagleStr = "logo/splasheagle.jpg";
				int radomNo = (int)((3)*Math.random());
				// 设置窗体图片
				if("1".equals(ConfBean.type)){
					bodyPicStr = "logo/hong"+String.valueOf(radomNo)+".jpg";
				}else{
					bodyPicStr = "logo/scenery"+String.valueOf(radomNo)+".jpg";
				}
			}
			
			
			byte[] logoByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,logoStr);
			Toolkit tk=Toolkit.getDefaultToolkit();
			logo=tk.createImage(logoByte); 
			
			logoIcon = new ImageIcon(logoByte);
			
			byte[] bodyPicByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,bodyPicStr);
			bodyPic = new ImageIcon(bodyPicByte);
			
			byte[] newUserByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/newUser.png");
			newUser = new ImageIcon(newUserByte);
			
			byte[] splashEagleByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,splashEagleStr);
			splashEagle = new ImageIcon(splashEagleByte);
			
			byte[] loginByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/login.png");
			login = new ImageIcon(loginByte);
			
			byte[] updatePassByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/updatePass.png");
			updatePass = new ImageIcon(updatePassByte);
			
			byte[] deleteUserByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/deleteUser.png");
			deleteUser = new ImageIcon(deleteUserByte);
			
			byte[] importDataByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/importData.png");
			importData = new ImageIcon(importDataByte);
			
			byte[] exportDataByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/exportData.png");
			exportData = new ImageIcon(exportDataByte);
			
			byte[] exitByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/exit.png");
			exit = new ImageIcon(exitByte);
			
			byte[] iConfByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/iConf.png");
			iConf = new ImageIcon(iConfByte);
			
			byte[] oConfByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/oConf.png");
			oConf = new ImageIcon(oConfByte);
			
			byte[] bakByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/bak.png");
			bak = new ImageIcon(bakByte);
			
			byte[] plainByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/plain.png");
			plain = new ImageIcon(plainByte);
			
			byte[] completedByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/completed.png");
			completed = new ImageIcon(completedByte);
			
			byte[] unplainByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/unplain.png");
			unplain = new ImageIcon(unplainByte);
			
			byte[] restoreByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/restore.png");
			restore = new ImageIcon(restoreByte);

			byte[] calendarByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/calendar.png");
			calendar = new ImageIcon(calendarByte);
			
			byte[] clockByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/clock.png");
			clock = new ImageIcon(clockByte);
			
			byte[] clockBgByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/clockbg.jpg");
			clockBg = new ImageIcon(clockBgByte);
			
			//byte[] calendarBgByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/calendar.png");
			//calendarBg = new ImageIcon(calendarBgByte).getImage(); 
			
			byte[] calcByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/calc.png");
			calc = new ImageIcon(calcByte);
			
			byte[] diaryByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/diary.png");
			diary = new ImageIcon(diaryByte);
			
			byte[] letteryByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/lettery.png");
			lettery = new ImageIcon(letteryByte);
			
			byte[] iReportByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/iReport.png");
			iReport = new ImageIcon(iReportByte);
			
			byte[] oReportByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/oReport.png");
			oReport = new ImageIcon(oReportByte);
			
			byte[] ioReportByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/ioCReport.png");
			ioReport = new ImageIcon(ioReportByte);
			
			byte[] configByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/config.png");
			config = new ImageIcon(configByte);
			
			byte[] aboutByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/about.png");
			about = new ImageIcon(aboutByte);
			
			byte[] helpByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/help.png");
			help = new ImageIcon(helpByte);
			
			byte[] eagleByte = null;
			if("1".equals(ConfBean.type)){
				eagleByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/we.jpg");
			}else{
				eagleByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/eagle.jpg");
			}
			eagle = new ImageIcon(eagleByte);
			
			// 日记本部分
			byte[] saveByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/save.png");
			save = new ImageIcon(saveByte);
			
			byte[] searchByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/search.png");
			search = new ImageIcon(searchByte);
			
			byte[] printerByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/printer.png");
			printer = new ImageIcon(printerByte);
			
			byte[] undoByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/back.png");
			undo = new ImageIcon(undoByte);
			
			byte[] redoByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/redo.png");
			redo = new ImageIcon(redoByte);
			
			byte[] cutByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/cut.png");
			cut = new ImageIcon(cutByte);
			
			byte[] copyByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/copy.png");
			copy = new ImageIcon(copyByte);
			
			byte[] pasteByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/paste.png");
			paste = new ImageIcon(pasteByte);
			
			byte[] bolderByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/bolder.png");
			bolder = new ImageIcon(bolderByte);
			
			byte[] italicByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/italic.png");
			italic = new ImageIcon(italicByte);
			
			byte[] underlineByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/underline.png");
			underline = new ImageIcon(underlineByte);
			
			byte[] colorByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/color.png");
			color = new ImageIcon(colorByte);
			
			byte[] fontByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/font.png");
			font = new ImageIcon(fontByte);
			
			byte[] newByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/new.png");
			newDiary = new ImageIcon(newByte);
			
			byte[] refreshByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/refresh.png");
			refresh = new ImageIcon(refreshByte);
			
			byte[] deleteByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/delete.png");
			delete = new ImageIcon(deleteByte);
			
			byte[] renameByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/diary/rename.png");
			rename = new ImageIcon(renameByte);
			
			byte[] addByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/add.png");
			add = new ImageIcon(addByte);
			
			byte[] updateByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/update.png");
			update = new ImageIcon(updateByte);
			
			byte[] commitByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/commit.png");
			commit = new ImageIcon(commitByte);
			
			byte[] mmrdateByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/mmrdate.png");
			mmrdate = new ImageIcon(mmrdateByte);
			
			byte[] ntmtByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/ntmt.png");
			ntmt = new ImageIcon(ntmtByte);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
