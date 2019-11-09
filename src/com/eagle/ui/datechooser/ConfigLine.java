package com.eagle.ui.datechooser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.util.*;
import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
class ConfigLine extends JPanel {
    ConfigLine(TablePanel tablePanel, Calendar showMonth,int startYear, int lastYear) {
    	this.tablePanel = tablePanel;
        nowYear = Integer.valueOf(new SimpleDateFormat("yyyy")
                                    .format(showMonth.getTime())).intValue();
        nowMonth = Integer.valueOf(new SimpleDateFormat("M")
                                    .format(showMonth.getTime())).intValue();
        yearBox = new RoundBox(nowYear, startYear, lastYear);
        monthBox = new RoundBox(nowMonth, 1, 12);
        makeFace();
        addListener();
    }
    private void makeFace() {
        
        Font txtFont = new Font("楷体", Font.PLAIN, 12);
        this.setBorder(null);
        this.setBackground(Pallet.configLineColor);
        this.setLayout(new FlowLayout(1, 7, 1));
        this.setPreferredSize(new Dimension(50, 19));
        txtYear.setForeground(Pallet.cfgTextColor);
        txtYear.setPreferredSize(new Dimension(14, 14));
        txtYear.setFont(txtFont);
        txtMonth.setForeground(Pallet.cfgTextColor);
        txtMonth.setPreferredSize(new Dimension(14, 14));
        txtMonth.setFont(txtFont);
        monthBox.setShowWidth(17);
        add(yearBox);
        add(txtYear);
        add(monthBox);
        add(txtMonth);
    }
    private void addListener() {
        yearBox.bt_UP.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                btPressed(yearBox, 1);
            }
            public void mouseReleased(MouseEvent e) {
                btReleased(yearBox, 1);
                nowYear = yearBox.showNow;
                if(tablePanel!=null){
                	tablePanel.setMonth(nowYear, nowMonth);
                }
            }
        });        
        yearBox.bt_DOWN.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                btPressed(yearBox, 2);
            }
            public void mouseReleased(MouseEvent e) {
                btReleased(yearBox, 2);
                nowYear = yearBox.showNow;
                if(tablePanel!=null){
                	tablePanel.setMonth(nowYear, nowMonth);
                }
            }
        });
        monthBox.bt_UP.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                btPressed(monthBox, 1);
            }

            public void mouseReleased(MouseEvent e) {
                btReleased(monthBox, 1);
                nowMonth = monthBox.showNow;
                if(tablePanel!=null){
                	tablePanel.setMonth(nowYear, nowMonth);
                }
            }
        });
        monthBox.bt_DOWN.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                btPressed(monthBox, 2);
            }
            public void mouseReleased(MouseEvent e) {
                btReleased(monthBox, 2);
                nowMonth = monthBox.showNow;
                if(tablePanel!=null){
                	tablePanel.setMonth(nowYear, nowMonth);
                }
            }
        });
    }

    private void btPressed(RoundBox box, int theBT) {
        final RoundBox theBox = box;
        
        if(theBT == 1) {
            timer = new Timer(true);
            timer.schedule(new TimerTask() {
                public void run() {
                    if(theBox.showNow < theBox.showMax) {
                        theBox.showing.setText("" + (theBox.showNow+1));
                        
                        theBox.showNow++;
                    }
                }
            }, 500, 100);
        }
        else if(theBT == 2) {
            timer = new Timer(true);
            timer.schedule(new TimerTask() {
                public void run() {
                    if(theBox.showNow > theBox.showMin) {
                        theBox.showing.setText("" + (theBox.showNow-1));
                        theBox.showNow--;
                    }
                }
            }, 500, 100);
        }
    }

    private void btReleased(RoundBox box, int theBT) {
        final RoundBox theBox = box;
        timer.cancel();
        if(theBT == 1) {
            if(theBox.showNow < theBox.showMax) {
                theBox.showing.setText("" + (theBox.showNow+1));
                theBox.showNow++;
            }
        }
        else if(theBT == 2) {
            if(theBox.showNow > theBox.showMin) {
                theBox.showing.setText("" + (theBox.showNow-1));
                theBox.showNow--;
            }
        }
    }
    private TablePanel tablePanel = null;
    private int nowYear = 0;
    private int nowMonth = 0;
    Timer timer = new Timer(true);
    private RoundBox yearBox = null;
    private RoundBox monthBox = null;
    private JLabel txtYear = new JLabel("年");
    private JLabel txtMonth = new JLabel("月");
    
    public RoundBox getYearBox(){
    	return yearBox;
    }
    
    public RoundBox getMonthBox(){
    	return monthBox;
    }
    
    public void setTablePanel(TablePanel tablePanel){
    	this.tablePanel = tablePanel;
    }
}
