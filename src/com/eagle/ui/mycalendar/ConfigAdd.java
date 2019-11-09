package com.eagle.ui.mycalendar;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.apache.commons.logging.*;

import com.eagle.action.CalendarAction;
import com.eagle.data.ImageData;
import com.eagle.data.MmrDayData;
import com.eagle.prop.*;
import com.eagle.util.StringUtil;

public class ConfigAdd extends JDialog implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private CalendarAction mmrAction = new CalendarAction();
	
	private JPanel clkPanle ;
	private JLabel label_0=new JLabel("添加纪念日配置",JLabel.CENTER);
    private JLabel label_1=new JLabel("时间：",JLabel.CENTER);
    private JLabel label_10=new JLabel("年",JLabel.CENTER);
    private JLabel label_11=new JLabel("月",JLabel.CENTER);
    private JLabel label_12=new JLabel("日",JLabel.CENTER);
    private JLabel label_2=new JLabel("内容：",JLabel.CENTER);
    private JLabel label_3=new JLabel("历法：",JLabel.CENTER);
    private JLabel label_4=new JLabel("是否有效：",JLabel.CENTER);
    private JComboBox txt_10=new JComboBox();
    private JComboBox txt_11=new JComboBox();
    private JComboBox txt_12=new JComboBox();
    private JTextField txt_2=new JTextField(15);
    private JComboBox txt_3=new JComboBox();
    private JComboBox txt_4=new JComboBox();
    private JButton button_1=new JButton("确定");
    private JButton button_2=new JButton("取消");
    private Log log = LogFactory.getLog(ConfigAdd.class);
    
	public ConfigAdd(JFrame _f){
		super(_f, "添加纪念日配置", true);
		clkPanle = (JPanel)this.getContentPane();
		initBody();
		UIProp.setUIScreen(this, new Dimension(300,250));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void initBody(){
		initComb();
		clkPanle.setLayout(null);
		
		FontProp.setFont_b_18_h(label_0);
		button_1.addActionListener(this);
        button_1.addKeyListener(this);
        button_1.setActionCommand("add");
        button_1.setIcon(ImageData.commit);
        button_2.addActionListener(this);
        button_2.addKeyListener(this);
        button_2.setActionCommand("cancel");
        button_2.setIcon(ImageData.delete);
        
        JPanel panel_0 = new JPanel();
        JPanel panel_1=new JPanel();
        JPanel panel_2=new JPanel();
        JPanel panel_3=new JPanel();
        JPanel panel_40=new JPanel();
        JPanel panel_6=new JPanel();
        
        panel_0.add(label_0);
        panel_0.setBounds(10, 10, 280, 30);
        this.clkPanle.add(panel_0);
        
        panel_1.add(label_1);
        panel_1.add(txt_10);
        panel_1.add(label_10);
        panel_1.add(txt_11);
        panel_1.add(label_11);
        panel_1.add(txt_12);
        panel_1.add(label_12);
        panel_1.setBounds(10, 50, 280, 30);
        this.clkPanle.add(panel_1);
        
        panel_2.add(label_2);
        panel_2.add(txt_2);
        panel_2.setBounds(10, 80, 280, 30);
        this.clkPanle.add(panel_2);
        
        panel_3.add(label_3);
        panel_3.add(txt_3);
        panel_3.setBounds(10, 110, 280, 30);
        this.clkPanle.add(panel_3);
        
        panel_40.add(label_4);
        panel_40.add(txt_4);
        panel_40.setBounds(10, 140, 280, 30);
        this.clkPanle.add(panel_40);
        
        
        panel_6.add(button_1);
        panel_6.add(button_2);
        panel_6.setBounds(10, 180, 280, 30);
        this.clkPanle.add(panel_6);
	}
	
	public void actionPerformed(ActionEvent event){
		try{
			if("add".equals(event.getActionCommand())){
				if(StringUtil.isEmpty(txt_2.getText())){
					JOptionPane.showMessageDialog(this, "请输入提示内容！");
					return;
				}
				
				MmrDayData data = new MmrDayData();
				data.setContent(txt_2.getText());
				data.setType(txt_3.getSelectedItem().toString());
				data.setValid(txt_4.getSelectedItem().toString());
				String year = txt_10.getSelectedItem().toString();
				String month = txt_11.getSelectedItem().toString();
				String day = txt_12.getSelectedItem().toString();
				if(StringUtil.isEmpty(year)){
					data.setId(month+"-"+day);
				}else{
					data.setId(year+"-"+month+"-"+day);
				}
				if(mmrAction.addMmyDay(data)){
					this.setVisible(false);
					this.dispose();
				}else{
					JOptionPane.showMessageDialog(this, "添加失败，可能是纪念日时间已设置！");
				}
			}else{
				this.setVisible(false);
				this.dispose();
			}
		}catch(Exception e){
			log.error(e);
		}
	}
	
	public void keyReleased(KeyEvent event){}
	
	public void keyTyped(KeyEvent event){
		if(event.getKeyChar()=='\n'){
			ActionEvent ok=new ActionEvent("",0,"add");
			actionPerformed(ok);
    	}else if(event.getKeyChar()==0x1b){//0x1b表示esc键
    		ActionEvent ok=new ActionEvent("",1,"exit");
    		actionPerformed(ok);
    	}
	}
	public void keyPressed(KeyEvent event){}
	
	
	public void initComb(){
		txt_10.addItem("");
		for (int y = 1900; y < 2200; y++) {
			txt_10.addItem(String.valueOf(y));
		}
		
		txt_11.addItem("01");
		txt_11.addItem("02");
		txt_11.addItem("03");
		txt_11.addItem("04");
		txt_11.addItem("05");
		txt_11.addItem("06");
		txt_11.addItem("07");
		txt_11.addItem("08");
		txt_11.addItem("09");
		txt_11.addItem("10");
		txt_11.addItem("11");
		txt_11.addItem("12");
		
		txt_12.addItem("01");
		txt_12.addItem("02");
		txt_12.addItem("03");
		txt_12.addItem("04");
		txt_12.addItem("05");
		txt_12.addItem("06");
		txt_12.addItem("07");
		txt_12.addItem("08");
		txt_12.addItem("09");
		txt_12.addItem("10");
		txt_12.addItem("11");
		txt_12.addItem("12");
		txt_12.addItem("13");
		txt_12.addItem("14");
		txt_12.addItem("15");
		txt_12.addItem("16");
		txt_12.addItem("17");
		txt_12.addItem("18");
		txt_12.addItem("19");
		txt_12.addItem("20");
		txt_12.addItem("21");
		txt_12.addItem("22");
		txt_12.addItem("23");
		txt_12.addItem("24");
		txt_12.addItem("25");
		txt_12.addItem("26");
		txt_12.addItem("27");
		txt_12.addItem("28");
		txt_12.addItem("29");
		txt_12.addItem("30");
		txt_12.addItem("31");
		
		txt_3.addItem("阳历");
		txt_3.addItem("阴历");
		
		txt_4.addItem("是");
		txt_4.addItem("否");
	}
}
