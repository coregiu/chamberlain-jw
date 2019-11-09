package com.eagle.ui.datechooser;
import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.eagle.prop.UIProp;

import java.util.Calendar; 
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Date;

/**
 * 时间选择界面，加载日期
 * */
public class DateChooser extends JDialog {
	private static final long serialVersionUID = 9180526871430687449L;
	private static boolean isShow = false; 
    private static Calendar showMonth = new GregorianCalendar();
    private int startYear = 1900;
    private int lastYear = 3050;
    JPanel rootPanel = new JPanel(new BorderLayout(), true);
    private TablePanel tablePanel = null;
    private ConfigLine configLine = null;
    private HourChooser hourChooser = null;
    public static final int width = 230;
    public static final int height = 190;
    
    public DateChooser() {
        makeFace();
    }

    public DateChooser(Frame owner) {
        super(owner, "日期选择", true);
        makeFace();
    }
    
    public DateChooser(JDialog owner) {
        super(owner, "日期选择", true);
        this.setResizable(false);
        makeFace();
    }

    @SuppressWarnings("static-access")
	public DateChooser(Frame owner, Calendar showMonth, int startYear,int lastYear) {
        super(owner, "日期选择", true);
        this.showMonth = showMonth;
        this.startYear = startYear;
        this.lastYear = lastYear;
        this.setResizable(false);
        makeFace();
    }
    /*@SuppressWarnings("static-access")
	public DateChooser(Calendar showMonth, int startYear, int lastYear) {
        super((Frame)null, "日期选择", true);
        this.showMonth = showMonth;
        this.startYear = startYear;
        this.lastYear = lastYear;
        makeFace(); 
    }*/
    private void makeFace() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        configLine = new ConfigLine(tablePanel, showMonth, startYear, lastYear);
        tablePanel = new TablePanel(this, showMonth, configLine);
        configLine.setTablePanel(tablePanel);
        hourChooser=new HourChooser(tablePanel);
        UIProp.setUIScreen(this, new Dimension(width, height));
        rootPanel.setBorder(new LineBorder(Pallet.backGroundColor, 2));
        rootPanel.setBackground(Pallet.backGroundColor);
        rootPanel.add(tablePanel, BorderLayout.CENTER);
        rootPanel.add(configLine, BorderLayout.NORTH);
        rootPanel.add(hourChooser, BorderLayout.SOUTH);
        getContentPane().add(rootPanel, BorderLayout.CENTER);
    }
    public Date showChooser(JComponent invoker, int x, int y) {
        Point invokerOrigin;
        if (invoker != null) {
            if(isShow == true)
                setVisible(false);
            invokerOrigin = invoker.getLocationOnScreen();
            setLocation(invokerOrigin.x + x, invokerOrigin.y + y);
        } else {
            if(isShow == true)
                setVisible(false);
            setLocation(x, y);
        }
        setVisible(true);
        isShow = true;
        return tablePanel.getDate();
    }
    
    public void hideChooser() {
        setVisible(false);
    }
    
	public Date getDate() {
    	try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            java.util.Date du  = new java.util.Date();
    		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
    		if(tablePanel.getDate()==null) return null;
    		String timeSelect=f.format(tablePanel.getDate())+" "+hourChooser.yearBox.getSelectedItem()+":"+hourChooser.monthBox.getSelectedItem()+":00";
    		//System.out.println(timeSelect);
    		du = df.parse(timeSelect);
    		return du;
    	}catch(Exception e){
    		e.printStackTrace();
    		System.out.println("time ERROR!");    		
    		return null;
    	}        
    }
}