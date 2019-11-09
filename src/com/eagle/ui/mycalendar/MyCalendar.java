package com.eagle.ui.mycalendar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.*;
import org.apache.log4j.Logger;

import com.eagle.action.CalendarAction;
import com.eagle.data.*;
import com.eagle.prop.UIProp;
import com.eagle.ui.MainFrame;
import com.eagle.util.StringUtil;

/**
 * 万年历
 * @author eagle
 *
 */
public class MyCalendar extends JDialog implements MouseListener,
		ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(MyCalendar.class);
	// 快捷菜单
	private JPopupMenu popup;
	// 日历字体大小
	int fontsize = 16;
	/*
	public static void main(String args[]) {
		try {
			MyCalendar frame = new MyCalendar(null);
			frame.setVisible(true);
		} catch (Exception e) {
			log.error(e);
		}
	}*/

	// 当前时间
	private Date date = new Date();

	private GregorianCalendar gregorianCalendar = new GregorianCalendar();

	private String[] stringWeek = new String[] { "SUN", "MON", "TUE", "WED",
			"THU", "FRI", "SAT" };

	private String[] stringWeekCn = new String[] { "星期日", "星期一", "星期二", "星期三",
			"星期四", "星期五", "星期六" };

	private String[] stringMonth = new String[] { "Jan", "Feb", "Mar", "Apr",
			"May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec" };

	private String[] strSysTime = new String[6];

	private String[] strSysNowTime = new String[6];

	private JLabel[] buttonDay = new JLabel[42];

	private JLabel[] buttonWeek = new JLabel[7];

	private JLabel labelMonth = new JLabel();

	private JButton buttonToday = new JButton();

	private JButton buttonLastMonth = new JButton();

	private JButton buttonNextMonth = new JButton();

	//private JButton buttonConfig = new JButton("配置");

	private JComboBox comboYear = new JComboBox();

	private JComboBox comboMonth = new JComboBox();
	
	private CalendarAction action = new CalendarAction();
	
	private JPanel pnlMain;
	
	private MainFrame pf;

	/**
	 * 构造成年历
	 * @param pf
	 */
	public MyCalendar(MainFrame pf) {

		super(pf, "万年历");
		super.setModal(true);
		this.pf = pf;
		action.setMyFeast();
		pnlMain = (JPanel)getContentPane();
		pnlMain.setLayout(new GridLayout(9, 7, 3, 5));
		// setBounds(300, 200, 620, 400);
		UIProp.setUIScreen(this, new Dimension(550, 430));
		// comboYear.setForeground(new Color(200, 50, 255));
		comboYear.setFont(new Font("", Font.PLAIN, 18));
		for (int y = 1900; y < 2200; y++) {
			comboYear.addItem("  " + new Integer(y).toString());
		}
		pnlMain.add(comboYear);
		comboYear.addItemListener(this);

		final JLabel labelYear = new JLabel();
		labelYear.setForeground(Color.BLUE);
		labelYear.setFont(new Font(" ", Font.BOLD, 18));
		pnlMain.add(labelYear);
		labelYear.setText("   年");

		comboMonth.setForeground(new Color(150, 20, 255));
		comboMonth.setFont(new Font(" ", Font.BOLD, 18));
		for (int m = 1; m <= 12; m++) {
			comboMonth.addItem(" " + new Integer(m).toString());
		}
		pnlMain.add(comboMonth);
		comboMonth.addItemListener(this);

		pnlMain.add(labelMonth);
		labelMonth.setForeground(Color.BLUE);
		labelMonth.setFont(new Font(" ", Font.BOLD, 18));
		labelMonth.setText("   月");

		pnlMain.add(buttonLastMonth);
		buttonLastMonth.setForeground(Color.BLUE);
		buttonLastMonth.setFont(new Font(" ", Font.BOLD, 16));
		buttonLastMonth.setText("上月");
		buttonLastMonth.addActionListener(this);

		pnlMain.add(buttonToday);
		buttonToday.setForeground(Color.BLUE);
		buttonToday.setFont(new Font(" ", Font.BOLD, 16));
		buttonToday.setText("今日");
		buttonToday.addActionListener(this);

		pnlMain.add(buttonNextMonth);
		buttonNextMonth.setForeground(Color.BLUE);
		buttonNextMonth.setFont(new Font(" ", Font.BOLD, 16));
		buttonNextMonth.setText("下月");
		buttonNextMonth.addActionListener(this);

		for (int i = 0; i < 7; i++) {
			buttonWeek[i] = new JLabel("", JLabel.CENTER);
			buttonWeek[i].setBackground(null);
			buttonWeek[i].addMouseListener(this);
			if (i == 0 || i == 6) {
				buttonWeek[i].setForeground(Color.RED);

			} else {
				buttonWeek[i].setForeground(Color.BLUE);
			}
			buttonWeek[i].setFont(new Font(" ", Font.BOLD, 16));
			buttonWeek[i].setText(stringWeekCn[i]);
			// pnlMain.setBackground(Color.decode("#3a3737"));
			pnlMain.add(buttonWeek[i]);
		}

		for (int i = 0; i < 42; i++) {
			buttonDay[i] = new JLabel("", JLabel.CENTER);
			buttonDay[i].setBackground(null);
			buttonDay[i].setOpaque(true);
			buttonDay[i].addMouseListener(this);
			buttonDay[i].setText(" ");
			buttonDay[i].setFont(new Font(" ", Font.BOLD, fontsize));
			pnlMain.add(buttonDay[i]);
		}
		this.setResizable(false);
		getSysNowTimeInfo();
		setNowDate();
		// 添加时间
		final JLabel time = new JLabel();
		time.add(new TIME(this));
		pnlMain.add(time);
		//pnlMain.add(buttonConfig);
		pnlMain.addMouseListener(this);

		popup = this.createPopupMenu();

		setVisible(true);
	}

	public void setSysDate(int year, int month) {
		gregorianCalendar.set(year, month, 1);
	}

	private String toolTipText = "";

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == buttonToday) {
			setNowDate();
		} else if (ae.getSource() == buttonLastMonth) {
			setDate(-1);
		} else if (ae.getSource() == buttonNextMonth) {
			setDate(1);
		} else if ("config".equals(ae.getActionCommand())) {
			try {
				/*String cmd = "rundll32 url.dll FileProtocolHandler file://"
						+ ConfBean.calendarPath;
				Runtime.getRuntime().exec(cmd);
	
				File file = new File(ConfBean.calendarPath);
				if(!file.exists()){
					file.createNewFile();
				}
				Desktop.getDesktop().edit(file);*/
				this.dispose();
				this.pf.actionPerformed(new ActionEvent("", 0, "mmrDate"));
			} catch (Exception e) {
				log.error(e);
			}
		} else if ("view".equals(ae.getActionCommand())) {
			if (StringUtil.isNotEmpty(toolTipText)) {
				JOptionPane.showMessageDialog(this, toolTipText, "查看详细",
						JOptionPane.INFORMATION_MESSAGE);
				toolTipText="";
			}
		}
	}

	public void itemStateChanged(ItemEvent arg0) {
		setDate(0);
	}

	public void getSysNowTimeInfo() {

		date = gregorianCalendar.getTime();
		strSysNowTime = (date + " ").split(" ");
	}

	public void getSysTimeInfo() {

		date = gregorianCalendar.getTime();
		strSysTime = (date + " ").split(" ");
	}

	public int getNowMonth() {
		int month = 0;
		for (int i = 0; i < 12; i++) {
			if (strSysNowTime[1].equalsIgnoreCase(stringMonth[i])) {
				month = i;
				break;
			}
		}
		return month;
	}

	public int weekStrat(String strWeek) {
		int strat = 0;
		for (int i = 0; i < 7; i++) {
			if (strWeek.equalsIgnoreCase(stringWeek[i])) {
				strat = i;
				break;
			}
		}
		return strat;
	}

	/**
	 * 设置时间
	 * 
	 */
	public void setNowDate() {
		setSysTime(getNowYear(), getNowMonth());
		getSysTimeInfo();
		setDayNull();
		getDay(getMonthDays(getNowYear(), getNowMonth() - 1), getMonthDays(
				getNowYear(), getNowMonth()), weekStrat(strSysTime[0]),
				getNowDay());
		comboYear.setSelectedIndex(getNowYear() - 1900);
		comboMonth.setSelectedIndex(getNowMonth());
	}

	/**
	 * 设置日期（上月、下月按钮）
	 * 
	 * @param move
	 */
	public void setDate(int move) {
		setSysTime(getYear(), getMonth() + move);
		getSysTimeInfo();
		setDayNull();
		getDay(getMonthDays(getYear(), getMonth() + move - 1), getMonthDays(
				getYear(), getMonth() + move), weekStrat(strSysTime[0]), -1);
		if (move != 0) {
			if (getMonth() == 0 && move < 0) {
				move = 11;
				comboYear.setSelectedIndex(getYear() - 1901);
			} else if (getMonth() == 11 && move > 0) {
				move = -11;
				comboYear.setSelectedIndex(getYear() - 1899);
			} else {
				comboYear.setSelectedIndex(getYear() - 1900);
			}
			comboMonth.setSelectedIndex(getMonth() + move);
		}
	}

	public void setSysTime(int year, int month) {
		gregorianCalendar.set(year, month, 1);
	}

	public int getNowYear() {
		return Integer.parseInt(strSysNowTime[5]);
	}

	public int getNowDay() {
		return Integer.parseInt(strSysNowTime[2]);
	}

	public int getYear() {
		return comboYear.getSelectedIndex() + 1900;
	}

	public int getMonth() {
		return comboMonth.getSelectedIndex();
	}

	public void setDayNull() {
		for (int d = 0; d < 42; d++) {
			buttonDay[d].setText(" ");
		}
	}

	private Calendar pCal = Calendar.getInstance();

	private int yearIt;

	private int monthIt;

	private String dayStr;

	private int dayIt;

	/**
	 * 添加日期
	 * 
	 * @param lastMonDays
	 * @param monthDays
	 * @param startWeek
	 * @param day
	 */
	public void getDay(int lastMonDays, int monthDays, int startWeek, int day) {
		yearIt = Integer.parseInt(comboYear.getSelectedItem().toString().trim());
		monthIt = Integer.parseInt(comboMonth.getSelectedItem().toString().trim());
		// 添加上个月日历
		if (monthIt == 0) {
			monthIt = 11;
			yearIt = Integer.parseInt(comboYear.getSelectedItem().toString().trim()) - 1;
		} else {
			monthIt = Integer.parseInt(comboMonth.getSelectedItem().toString().trim()) - 1;
			yearIt = Integer.parseInt(comboYear.getSelectedItem().toString().trim());
		}
		for (int d = 0; d < startWeek + 1; d++) {
			buttonDay[d].setForeground(Color.GRAY);
			dayStr = (lastMonDays - startWeek) + d + 1 + "";
			dayIt = Integer.parseInt(dayStr);
			buttonDay[d].setText(dayStr);
			pCal.set(yearIt, monthIt - 1, dayIt, 00 , 00 , 00);
			setToolTip(buttonDay[d], pCal);
		}

		// 添加本月日历
		yearIt = Integer.parseInt(comboYear.getSelectedItem().toString().trim());
		monthIt = Integer.parseInt(comboMonth.getSelectedItem().toString().trim());
		for (int d = startWeek; d < startWeek + monthDays; d++) {
			if ((d - startWeek + 1) == day) {
				buttonDay[d].setForeground(Color.blue);
				buttonDay[d].setBackground(Color.GREEN);
				buttonDay[d].setFont(new Font(" ", Font.BOLD, fontsize));
			} else if (d % 7 == 0 || d % 7 == 6) {
				buttonDay[d].setForeground(Color.RED);
			} else {
				buttonDay[d].setForeground(Color.BLACK);
			}
			dayStr = d - startWeek + 1 + " ";
			dayIt = Integer.parseInt(dayStr.trim());
			buttonDay[d].setText(dayStr);
			pCal.set(yearIt, monthIt - 1, dayIt, 00 , 00 , 00);
			setToolTip(buttonDay[d], pCal);
		}

		// 添加下个月日历
		if (monthIt == 11) {
			monthIt = 0;
			yearIt = Integer.parseInt(comboYear.getSelectedItem().toString().trim()) + 1;
		} else {
			monthIt = Integer.parseInt(comboMonth.getSelectedItem().toString().trim()) + 1;
			yearIt = Integer.parseInt(comboYear.getSelectedItem().toString().trim());
		}
		for (int d = monthDays + startWeek; d < 42; d++) {
			buttonDay[d].setForeground(Color.GRAY);
			dayStr = d - (monthDays + startWeek) + 1 + " ";
			dayIt = Integer.parseInt(dayStr.trim());
			buttonDay[d].setText(dayStr);
			pCal.set(yearIt, monthIt - 1, dayIt, 00 , 00 , 00);
			setToolTip(buttonDay[d], pCal);
		}
	}

	/**
	 * 设置显示内容
	 * 
	 * @param cp
	 *            日期单元格
	 * @param cal
	 *            当前日期
	 */
	private void setToolTip(JLabel cp, Calendar cal) {
		try {
			String tipArr[] = action.getCalendarText(cal);
			cp.setToolTipText(tipArr[0]);

			// 设置显示
			if(StringUtil.isNotEmpty(tipArr[1])){
				cp.setText(tipArr[1]);
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	
	/**
	 * 获取某年某月有多少天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public int getMonthDays(int year, int month) {
		switch (month) {
		case 3:
		case 5:
		case 8:
		case 10:
			return 30;
		case 1:
			if (gregorianCalendar.isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		default:
			return 31;
		}
	}

	/**
	 * 构造右键
	 * 
	 * @param couldRefresh
	 * @param couldNew
	 * @param couldDelete
	 * @return
	 */
	protected JPopupMenu createPopupMenu() {

		JPopupMenu configMenu = new JPopupMenu("Chart:");

		JMenuItem refreshItem = new JMenuItem("配置我的纪念日");
		refreshItem.setActionCommand("config");
		refreshItem.addActionListener(this);
		refreshItem.setIcon(ImageData.config);
		// refreshItem.setAccelerator(KeyStroke.getKeyStroke('R',Event.CTRL_MASK,false));

		JMenuItem viewItem = new JMenuItem("查看详细");
		viewItem.setActionCommand("view");
		viewItem.addActionListener(this);
		viewItem.setIcon(ImageData.search);

		configMenu.add(viewItem);
		configMenu.add(refreshItem);

		return configMenu;
	}

	/**
	 * 展现快捷菜单
	 * 
	 * @param tree
	 * @param x
	 * @param y
	 */
	protected void displayPopupMenu(JComponent tree, int x, int y) {
		if (this.popup != null) {
			this.popup.show(tree, x, y);
		}
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

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (e.getSource() instanceof JLabel) {
				toolTipText = ((JLabel) e.getSource()).getToolTipText();
			}
			displayPopupMenu((JComponent) e.getSource(), e.getX(), e.getY());
		}
	}
}
