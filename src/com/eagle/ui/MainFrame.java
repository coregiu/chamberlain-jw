package com.eagle.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

import com.eagle.action.*;
import com.eagle.data.*;
import com.eagle.prop.*;
import com.eagle.ui.calculator.Calculator;
import com.eagle.ui.clock.ClockPanel;
import com.eagle.ui.diary.DiaryPanel;
import com.eagle.ui.help.*;
import com.eagle.ui.io.*;
import com.eagle.ui.mycalendar.ConfigMmrDayPanel;
import com.eagle.ui.mycalendar.MyCalendar;
import com.eagle.ui.plain.PlainPanel;
import com.eagle.ui.report.Report;
import com.eagle.ui.report.ViewCondition;
import com.eagle.ui.ssq.SSQCheckUI;
import com.eagle.ui.user.*;
import com.eagle.util.DateUtil;
import com.eagle.util.FileUtil;
/**
 * 系统主窗体
 * */
public class MainFrame 
		extends JFrame 
		implements ActionListener, WindowListener{
	
	private static final long serialVersionUID = 3461273568210670064L;
	private Container container;
	private String title = NameProp.TITLE+" -- "+ConfBean.VERSION;
	private TrayIcon trayIcon = null; // 托盘图标
	private SystemTray tray = null; // 本操作系统托盘的实例
	private ClockAction clkAction;
	/**
	 * 系统主界面
	 * 
	 * */
	public MainFrame(){
		
	}
	
	public void loadMainFrame(){
		container = this.getContentPane();
		this.setTitle(title);
		this.setJMenuBar(getJMenu());
		UIProp.setUIScreen(this, new Dimension(700,500));
		initContentPanel();
		this.setIconImage(ImageData.logo);
		this.addWindowListener(this);
		this.setResizable(false);
		// 最小化到托盘
		if (SystemTray.isSupported()) { // 如果操作系统支持托盘
			this.tray();
		}
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// 闹钟加载
		clkAction = new ClockAction();
	}
	
	/**
	 * 设置系统菜单
	 * @return
	 */
	private JMenuBar getJMenu(){
		JMenuBar menuBar = new JMenuBar();
		
		// 个人信息
		JMenu user = new JMenu("个人信息");
		JMenuItem newUser = new JMenuItem("新建用户", ImageData.newUser);
		newUser.addActionListener(this);
		newUser.setActionCommand("newUser");
		newUser.setAccelerator(KeyStroke.getKeyStroke('N',Event.SHIFT_MASK,false));
		
		JMenuItem login = new JMenuItem("登录", ImageData.login);
		login.addActionListener(this);
		login.setActionCommand("login");
		login.setAccelerator(KeyStroke.getKeyStroke('L',Event.SHIFT_MASK,false));
		
		updatePass.addActionListener(this);
		updatePass.setActionCommand("updatePass");
		updatePass.setAccelerator(KeyStroke.getKeyStroke('U',Event.SHIFT_MASK,false));
		
		deleteUser.addActionListener(this);
		deleteUser.setActionCommand("deleteUser");
		deleteUser.setAccelerator(KeyStroke.getKeyStroke('D',Event.SHIFT_MASK,false));
		
		bak.addActionListener(this);
		bak.setActionCommand("bak");
		bak.setAccelerator(KeyStroke.getKeyStroke('X',Event.SHIFT_MASK,false));
		
		restore.addActionListener(this);
		restore.setActionCommand("restore");
		restore.setAccelerator(KeyStroke.getKeyStroke('Y',Event.SHIFT_MASK,false));
		
		JMenuItem importData = new JMenuItem("导入", ImageData.importData);
		importData.addActionListener(this);
		importData.setActionCommand("importData");
		importData.setAccelerator(KeyStroke.getKeyStroke('V',Event.SHIFT_MASK,false));
		
		exportData.addActionListener(this);
		exportData.setActionCommand("exportData");
		exportData.setAccelerator(KeyStroke.getKeyStroke('W',Event.SHIFT_MASK,false));
		
		JMenuItem exit = new JMenuItem("退出", ImageData.exit);
		exit.addActionListener(this);
		exit.setActionCommand("exit");
		exit.setAccelerator(KeyStroke.getKeyStroke('E',Event.SHIFT_MASK,false));
		
		user.add(newUser);
		user.add(login);
		user.addSeparator();
		user.add(updatePass);
		user.add(deleteUser);
		user.addSeparator();
		user.add(bak);
		user.add(restore);
		user.add(importData);
		user.add(exportData);
		user.addSeparator();
		user.add(exit);
		
		// 收支管理
		JMenuItem iConf = new JMenuItem("收入管理", ImageData.iConf);
		iConf.addActionListener(this);
		iConf.setActionCommand("iConf");
		iConf.setAccelerator(KeyStroke.getKeyStroke('I',Event.SHIFT_MASK,false));
		
		JMenuItem oConf = new JMenuItem("支出管理", ImageData.oConf);
		oConf.addActionListener(this);
		oConf.setActionCommand("oConf");
		oConf.setAccelerator(KeyStroke.getKeyStroke('O',Event.SHIFT_MASK,false));
		
		JMenuItem iReport = new JMenuItem("收入图表分析", ImageData.iReport);
		iReport.addActionListener(this);
		iReport.setActionCommand("iReport");
		iReport.setAccelerator(KeyStroke.getKeyStroke('R',Event.SHIFT_MASK,false));
		
		JMenuItem oReport = new JMenuItem("支出图表分析", ImageData.oReport);
		oReport.addActionListener(this);
		oReport.setActionCommand("oReport");
		oReport.setAccelerator(KeyStroke.getKeyStroke('S',Event.SHIFT_MASK,false));
		
		JMenuItem ioCReport = new JMenuItem("收支对比图表分析", ImageData.ioReport);
		ioCReport.addActionListener(this);
		ioCReport.setActionCommand("ioCReport");
		ioCReport.setAccelerator(KeyStroke.getKeyStroke('T',Event.SHIFT_MASK,false));
		
		ioConf.add(iConf);
		ioConf.add(oConf);
		ioConf.addSeparator();
		ioConf.add(iReport);
		ioConf.add(oReport);
		ioConf.add(ioCReport);
		
		// 日常管理
		JMenuItem dailyBk = new JMenuItem("日记&档案", ImageData.diary);
		dailyBk.addActionListener(this);
		dailyBk.setActionCommand("dailyBk");
		dailyBk.setAccelerator(KeyStroke.getKeyStroke('A',Event.SHIFT_MASK,false));
		
		JMenuItem workBk = new JMenuItem("工作学习计划", ImageData.plain);
		workBk.addActionListener(this);
		workBk.setActionCommand("workBk");
		workBk.setAccelerator(KeyStroke.getKeyStroke('B',Event.SHIFT_MASK,false));
		
		JMenuItem clock = new JMenuItem("闹钟配置", ImageData.clock);
		clock.addActionListener(this);
		clock.setActionCommand("clock");
		clock.setAccelerator(KeyStroke.getKeyStroke('C',Event.SHIFT_MASK,false));
		
		JMenuItem mmrDate = new JMenuItem("纪念日配置", ImageData.mmrdate);
		mmrDate.addActionListener(this);
		mmrDate.setActionCommand("mmrDate");
		mmrDate.setAccelerator(KeyStroke.getKeyStroke('F',Event.SHIFT_MASK,false));
		
		JMenuItem calendar = new JMenuItem("万年历", ImageData.calendar);
		calendar.addActionListener(this);
		calendar.setActionCommand("calendar");
		calendar.setAccelerator(KeyStroke.getKeyStroke('J',Event.SHIFT_MASK,false));
		
		JMenuItem calc = new JMenuItem("计算器", ImageData.calc);
		calc.addActionListener(this);
		calc.setActionCommand("calc");
		calc.setAccelerator(KeyStroke.getKeyStroke('K',Event.SHIFT_MASK,false));
		
		
		txtBk.add(dailyBk);
		txtBk.add(workBk);
		txtBk.addSeparator();
		txtBk.add(clock);
		txtBk.add(mmrDate);
		txtBk.add(calendar);
		txtBk.add(calc);
		
		// 帮助
		JMenu help = new JMenu("关于管家");
		JMenuItem config = new JMenuItem("管家配置", ImageData.config);
		config.addActionListener(this);
		config.setActionCommand("config");
		config.setAccelerator(KeyStroke.getKeyStroke('G',Event.SHIFT_MASK,false));
		
		JMenuItem about = new JMenuItem("关于我的管家", ImageData.about);
		about.addActionListener(this);
		about.setActionCommand("about");
		about.setAccelerator(KeyStroke.getKeyStroke('P',Event.SHIFT_MASK,false));
		
		JMenuItem helper = new JMenuItem("帮助", ImageData.help);
		helper.addActionListener(this);
		helper.setActionCommand("helper");
		helper.setAccelerator(KeyStroke.getKeyStroke('H',Event.SHIFT_MASK,false));
		
		help.add(config);
		help.addSeparator();
		help.add(about);
		help.add(helper);
		
		// 休闲娱乐
		JMenu qilei = new JMenu("棋类游戏");
		qilei.setIcon(ImageData.ntmt);
		
		JMenuItem zgxq = new JMenuItem("中国象棋");
		zgxq.addActionListener(this);
		zgxq.setActionCommand("zgxq");
		
		JMenuItem wzq = new JMenuItem("五子棋");
		wzq.addActionListener(this);
		wzq.setActionCommand("wzq");
		
		JMenuItem dphxb = new JMenuItem("大炮轰小兵");
		dphxb.addActionListener(this);
		dphxb.setActionCommand("dphxb");
		
		JMenuItem cfcl = new JMenuItem("成方成龙");
		cfcl.addActionListener(this);
		cfcl.setActionCommand("cfcl");
		
		qilei.add(zgxq);
		qilei.add(wzq);
		qilei.add(dphxb);
		qilei.add(cfcl);
		
		JMenu lettry = new JMenu("彩票");
		lettry.setIcon(ImageData.lettery);
		
		JMenuItem ssqBk = new JMenuItem("双色球");
		ssqBk.addActionListener(this);
		ssqBk.setActionCommand("ssq");
		
		lettry.add(ssqBk);
		
		//ntmtBk.add(qilei);
		ntmtBk.add(lettry);
		
		setMenuEnable(false);
		
		menuBar.add(user);
		menuBar.add(ioConf);
		menuBar.add(txtBk);
		menuBar.add(ntmtBk);
		menuBar.add(help);
		menuBar.add(opName);
		opName.setEnabled(false);
		return menuBar;
	}
	
	/**
	 * 添加图片
	 * */
	private JPanel getContent(){
		return new MainPanel();
	}
	
	/**
	 * 事件监控
	 * */
	public void actionPerformed (ActionEvent event){
		if("exit".equals(event.getActionCommand())){
			exit();
		}else if("config".equals(event.getActionCommand())){
			new ConfigDialog(this);
			clkAction.restart();
		}else if("about".equals(event.getActionCommand())){
			new Help(this);
		}else if("helper".equals(event.getActionCommand())){
			File file = new File(ConfBean.HELP);
			if(!file.exists()){
				JOptionPane.showMessageDialog(this, "系统找不到相关帮助文档!");
				return;
			}
			FileUtil.editFile(ConfBean.HELP);
		}else if("login".equals(event.getActionCommand())){
			new LoginDialog(this);
			if(ConfBean.loginUserInfo!=null){
				setMenuEnable(true);
				startClockProcess();
			}
		}else if("newUser".equals(event.getActionCommand())){
			new AddUserDialog(this);
		}else if("updatePass".equals(event.getActionCommand())){
			new UpdateUserDialog(this);
		}else if("deleteUser".equals(event.getActionCommand())){
			new DeleteUserDialog(this);
		}else if("importData".equals(event.getActionCommand())){
			ImportUI importUI = new ImportUI(this);
			if(importUI.isFlag()){
				setMenuEnable(true);
				setWelcome();
				initContentPanel();
			}
		}else if("exportData".equals(event.getActionCommand())){
			new ExportUI(this);
		}else if("iConf".equals(event.getActionCommand())){
			IPanel iPanel = new IPanel(this);
			container.removeAll();
            container.add(iPanel.getIConfPanel(), BorderLayout.CENTER);
            this.setContentPane(container);
            setOpName("收入管理");
            this.invalidate();
		}else if("oConf".equals(event.getActionCommand())){
			OPanel oPanel = new OPanel(this);
			container.removeAll();
            container.add(oPanel.getOConfPanel(), BorderLayout.CENTER);
            this.setContentPane(container);
            setOpName("支出管理");
            this.invalidate();
		}else if("bak".equals(event.getActionCommand())){
			if(JOptionPane.showConfirmDialog(this, "确定要备份收支数据码？", "备份提示" ,0) == 0){
				File backDir = new File(ConfBean.backupPath);
				if(!backDir.isDirectory()){
					JOptionPane.showMessageDialog(this, "备份目录不存在，请重新配置！");
					return;
				}
				DataBakAction bakAction = new DataBakAction();
				if(bakAction.bakData()){
					ConfBean.loadInfo();
					initContentPanel();
					JOptionPane.showMessageDialog(this, "备份成功！");
				}else{
					JOptionPane.showMessageDialog(this, "备份失败！");
				}
			}
		}else if("restore".equals(event.getActionCommand())){
			if(JOptionPane.showConfirmDialog(this, "确定要恢复收支数据码？", "恢复提示" ,0) == 0){
				File backDir = new File(ConfBean.backupPath);
				if(!backDir.isDirectory()){
					JOptionPane.showMessageDialog(this, "找不到备份目录，请重新指定！");
					return;
				}
				DataBakAction bakAction = new DataBakAction();
				if(bakAction.restoreData()){
					ConfBean.loadInfo();
					initContentPanel();
					JOptionPane.showMessageDialog(this, "恢复成功！");
				}else{
					JOptionPane.showMessageDialog(this, "恢复失败！");
				}
			}
		}else if("iReport".equals(event.getActionCommand())){
			ViewTimeBean vd = new ViewTimeBean();
			new ViewCondition(this, vd, 'i');
			if(vd==null||vd.getBgTime()==null){return;}
			setOpName("收入图表分析");
			Report reportPanel = new Report(this, vd, 'i');
			container.removeAll();
            container.add(reportPanel.getAnalysisPanel(vd.getType()), BorderLayout.CENTER);
            this.setContentPane(container);
            this.invalidate();
		}else if("oReport".equals(event.getActionCommand())){
			ViewTimeBean vd = new ViewTimeBean();
			new ViewCondition(this, vd, 'o');
			if(vd==null||vd.getBgTime()==null){return;}
			setOpName("支出图表分析");
			Report reportPanel = new Report(this, vd, 'o');
			container.removeAll();
            container.add(reportPanel.getAnalysisPanel(vd.getType()), BorderLayout.CENTER);
            this.setContentPane(container);
            this.invalidate();
		}else if("ioCReport".equals(event.getActionCommand())){
			ViewTimeBean vd = new ViewTimeBean();
			new ViewCondition(this, vd, 'a');
			if(vd==null||vd.getBgTime()==null){return;}
			setOpName("收支图表分析");
			Report reportPanel = new Report(this, vd, 'a');
			container.removeAll();
            container.add(reportPanel.getAnalysisPanel(vd.getType()), BorderLayout.CENTER);
            this.setContentPane(container);
            this.invalidate();
		}else if("ssq".equals(event.getActionCommand())){
			new SSQCheckUI(this);
		}else if("calc".equals(event.getActionCommand())){
			/*try{
				Runtime.getRuntime().exec("calc");
			}catch(Exception e){
				JOptionPane.showMessageDialog(this, "本地未安装计算器！");
			}*/
			new Calculator(this);
		}else if("calendar".equals(event.getActionCommand())){
			new MyCalendar(this);
		}else if("dailyBk".equals(event.getActionCommand())){
			setOpName("日记&档案");
			DiaryPanel diaryPanel = new DiaryPanel(this);
			container.removeAll();
            container.add(diaryPanel.getDiaryPanel(0), BorderLayout.CENTER);
            this.setContentPane(container);
            this.invalidate();
		}else if("workBk".equals(event.getActionCommand())){
			setOpName("工作学习计划");
			PlainPanel plainPanel = new PlainPanel(this);
			container.removeAll();
            container.add(plainPanel.getPlainPanel(), BorderLayout.CENTER);
            this.setContentPane(container);
            this.invalidate();
		}else if("clock".equals(event.getActionCommand())){
			setOpName("闹钟配置");
			ClockPanel clkPanel = new ClockPanel(this, clkAction);
			container.removeAll();
            container.add(clkPanel.getClkPanel(), BorderLayout.CENTER);
            this.setContentPane(container);
            this.invalidate();
		}else if("mmrDate".equals(event.getActionCommand())){
			setOpName("纪念日配置");
			ConfigMmrDayPanel mmrPanel = new ConfigMmrDayPanel(this);
			container.removeAll();
            container.add(mmrPanel.getMmrDayPanel(), BorderLayout.CENTER);
            this.setContentPane(container);
            this.invalidate();
		}	
	}
	
	private void exit(){
		if(JOptionPane.showConfirmDialog(this, "确定要退出吗？", "系统提示", JOptionPane.OK_CANCEL_OPTION)==0){
			ExitAction exitAction = new ExitAction();
			exitAction.regist();
			System.exit(0);
		}
	}
	
	/**
	 * 设置菜单可用
	 * */
	public void setMenuEnable(boolean flag){
		updatePass.setEnabled(flag);
		deleteUser.setEnabled(flag);
		bak.setEnabled(flag);
		restore.setEnabled(flag);
		ioConf.setEnabled(flag);
		txtBk.setEnabled(flag);
		exportData.setEnabled(flag);
		ntmtBk.setEnabled(flag);
	}
	
	/**
	 * 设置初始界面
	 * */
	public void initContentPanel(){
		container.removeAll();
		container.add(getContent());
		opName.setText("");
		setWelcome();
		this.setContentPane(container);
		this.validate();
	}
	
	/**
	 * 设置当前操作功能
	 * */
	public void setOpName(String name){
		opName.setText("    当前操作： "+name);
	}
	
	/**
	 * 设置问候语
	 * */
	public void setWelcome(){
		if(ConfBean.loginUserInfo != null){
			int hour = DateUtil.getHourOfTime(new Date());
			String welcomeStr = "";
			if(hour>=18||hour<=6){
				welcomeStr = "晚上";
			}else if(hour>=6 && hour <12){
				welcomeStr = "早上";
			}else if(hour>=12 && hour <14){
				welcomeStr = "中午";
			}else if(hour>=14 && hour < 18){
				welcomeStr = "下午";
			}
			String hello = "";
			if("".equals(ConfBean.loginUserInfo.getRelation())){
				hello = ConfBean.loginUserInfo.getRealname();
			}else{
				hello = ConfBean.loginUserInfo.getRelation();
			}
			String today = new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
			this.setTitle(title+"      "+hello+" "+welcomeStr+"好！  今天是　"+today);
		}
	}
	
	/**
	 * 最小化到托盘
	 *
	 */
	private void tray() {

		tray = SystemTray.getSystemTray(); // 获得本操作系统托盘的实例

		PopupMenu pop = new PopupMenu(); // 构造一个右键弹出式菜单
		MenuItem show = new MenuItem("打开");
		MenuItem exit = new MenuItem("退出");
		pop.add(show);
		pop.add(exit);
		trayIcon = new TrayIcon(ImageData.logo, title, pop);

		/**
		 * 添加鼠标监听器，当鼠标在托盘图标上双击时，默认显示窗口
		 */
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) { // 鼠标双击
					tray.remove(trayIcon); // 从系统的托盘实例中移除托盘图标
					setExtendedState(JFrame.NORMAL);
					setVisible(true); // 显示窗口
					toFront();
				}
			}
		});
		show.addActionListener(new ActionListener() { // 点击“显示窗口”菜单后将窗口显示出来
					public void actionPerformed(ActionEvent e) {
						tray.remove(trayIcon); // 从系统的托盘实例中移除托盘图标
						setExtendedState(JFrame.NORMAL);
						setVisible(true); // 显示窗口
						toFront();
					}
				});
		exit.addActionListener(new ActionListener() { // 点击“退出演示”菜单后退出程序
					public void actionPerformed(ActionEvent e) {
						exit(); // 退出程序
						dispose();
					}
				});

	}
	
	public void startClockProcess(){
		clkAction.stop();
		clkAction.init();
		clkAction.execute();
	}
	
	private JMenuItem updatePass = new JMenuItem("修改个人信息", ImageData.updatePass);
	private JMenuItem deleteUser = new JMenuItem("删除个人信息", ImageData.deleteUser);
	JMenuItem bak = new JMenuItem("数据备份", ImageData.bak);
	JMenuItem restore = new JMenuItem("数据恢复", ImageData.restore);
	private JMenuItem exportData = new JMenuItem("导出", ImageData.exportData);
	private JMenu ioConf = new JMenu("收支管理分析");
	private JMenu txtBk = new JMenu("日常管理及工具");
	private JMenu ntmtBk = new JMenu("休闲娱乐");
	private JMenu opName = new JMenu("");

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		exit();
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		try {
			tray.add(trayIcon); // 将托盘图标添加到系统的托盘实例中
			if("0".equals(ConfBean.autologin)){
				setMenuEnable(false);
				initContentPanel();
				ConfBean.loginUserInfo = null;
			}
			// setVisible(false); // 使窗口不可视
			dispose();
		} catch (AWTException ex) {
			ex.printStackTrace();
		}
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
