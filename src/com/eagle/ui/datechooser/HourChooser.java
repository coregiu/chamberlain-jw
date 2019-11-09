package com.eagle.ui.datechooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("serial")
/**
 * 加载时间
 * */
public class HourChooser extends JPanel implements ActionListener{

	private TablePanel tablePane;
	
	HourChooser(TablePanel tablePane) {        
        makeFace();
        this.tablePane = tablePane;
    }
    private void makeFace() {
        
        Font txtFont = new Font("楷体", Font.PLAIN, 12);
        this.setBorder(null);
        this.setBackground(Pallet.configLineColor);
        this.setLayout(new FlowLayout(1, 7, 1));
        this.setPreferredSize(new Dimension(50, 19));
        //txtYear.setForeground(Pallet.cfgTextColor);
        //yearBox.setForeground(Pallet.cfgTextColor);
        yearBox.setBackground(Pallet.configLineColor);
        txtYear.setPreferredSize(new Dimension(14, 14));
        txtYear.setFont(txtFont);
        //txtMonth.setForeground(Pallet.cfgTextColor);
        //monthBox.setForeground(Pallet.cfgTextColor);
        monthBox.setBackground(Pallet.configLineColor);
        txtMonth.setPreferredSize(new Dimension(14, 14));
        txtMonth.setFont(txtFont);
        //monthBox.setSize(width, height);
        for(int i=0;i<24;i++) {
        	if(i<10)   	yearBox.addItem("0"+i);
        	else		yearBox.addItem(i);
        }
        for(int i=0;i<60;i++) {
        	if(i<10)   	monthBox.addItem("0"+i);
        	else		monthBox.addItem(i);
        }
        SimpleDateFormat f =new SimpleDateFormat("HH-mm");
        String timeNow[]=f.format(new Date()).split("-");
        yearBox.setSelectedIndex(Integer.parseInt(timeNow[0]));
        monthBox.setSelectedIndex(Integer.parseInt(timeNow[1]));
        JButton button = new JButton("确定");
        button.setBackground(Pallet.configLineColor);
        button.setActionCommand("submit");
        button.addActionListener(this);
        
        JButton today = new JButton("今日");
        today.setBackground(Pallet.configLineColor);
        today.setActionCommand("today");
        today.addActionListener(this);
        
        add(yearBox);
        add(txtYear);
        add(monthBox);
        add(txtMonth);   
        add(today);
        add(button);
    }
    //Timer timer = new Timer(true);
    public JComboBox yearBox = new JComboBox();
    public JComboBox monthBox = new JComboBox();
    private JLabel txtYear = new JLabel("时");
    private JLabel txtMonth = new JLabel("分");
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if("submit".equals(e.getActionCommand())){
			tablePane.checkTime(false);
		}else if("today".equals(e.getActionCommand())){
			tablePane.checkTime(true);
		}
	}
}

