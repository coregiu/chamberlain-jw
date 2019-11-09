package com.eagle.ui.clock;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.Date;

import javax.swing.*;

import org.apache.commons.logging.*;

import com.eagle.action.ClockAction;
import com.eagle.data.ClockData;
import com.eagle.data.ImageData;
import com.eagle.prop.*;
import com.eagle.ui.datechooser.DateChooser;
import com.eagle.util.StringUtil;

public class ClockUpdateDialog extends JDialog implements ActionListener, KeyListener, MouseListener{
	private static final long serialVersionUID = 1L;
	private ClockAction clkAction = new ClockAction();
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private ClockData clkData;
	
	private JPanel clkPanle ;
	private JLabel label_0=new JLabel("修改闹钟配置",JLabel.CENTER);
    private JLabel label_1=new JLabel("闹钟时间：",JLabel.CENTER);
    private JLabel label_10=new JLabel("时",JLabel.CENTER);
    private JLabel label_11=new JLabel("分",JLabel.CENTER);
    private JLabel label_2=new JLabel("提醒方式：",JLabel.CENTER);
    private JLabel label_3=new JLabel("闹钟类型：",JLabel.CENTER);
    private JLabel label_4=new JLabel("提醒内容：",JLabel.CENTER);
    private JLabel label_5=new JLabel("是否有效：",JLabel.CENTER);
    private JComboBox txt_10=new JComboBox();
    private JComboBox txt_11=new JComboBox();
    private JComboBox txt_2=new JComboBox();
    private JComboBox txt_3=new JComboBox();
    private JTextArea txt_4=new JTextArea();
    private JComboBox txt_5=new JComboBox();
    private JTextField txt_8=new JTextField(12);
    private JButton button_1=new JButton("确定");
    private JButton button_2=new JButton("取消");
    private DateChooser dateChooser;
    
    private Log log = LogFactory.getLog(ClockAddDialog.class);
    
	public ClockUpdateDialog(JFrame _f, ClockData clkData){
		super(_f, "修改闹钟配置", true);
		this.clkData = clkData;
		clkPanle = (JPanel)this.getContentPane();
		dateChooser=new DateChooser(this);
		initBody();
		UIProp.setUIScreen(this, new Dimension(300,360));
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
        JPanel panel_41=new JPanel();
        JPanel panel_5=new JPanel();
        JPanel panel_6=new JPanel();
        
        panel_0.add(label_0);
        panel_0.setBounds(10, 10, 280, 30);
        this.clkPanle.add(panel_0);
        
        panel_1.add(label_1);
        panel_1.add(txt_10);
        panel_1.add(label_10);
        panel_1.add(txt_11);
        panel_1.add(label_11);
        panel_1.setBounds(10, 50, 280, 30);
        this.clkPanle.add(panel_1);
        
        panel_2.add(label_2);
        panel_2.add(txt_2);
        panel_2.setBounds(10, 80, 280, 30);
        this.clkPanle.add(panel_2);
        
        panel_3.add(label_3);
        panel_3.add(txt_3);
        panel_3.add(txt_8);
        txt_8.addMouseListener(this);
        txt_8.setEditable(false);
        panel_3.setBounds(10, 110, 280, 30);
        this.clkPanle.add(panel_3);
        
        panel_40.add(label_4);
        panel_40.setBounds(10, 140, 280, 30);
        this.clkPanle.add(panel_40);
        
        txt_4.setLineWrap(true);
        JScrollPane sPanel = new JScrollPane(txt_4);
        sPanel.setPreferredSize(new Dimension(280, 100));
        panel_41.add(sPanel);
        panel_41.setBounds(10, 170, 280, 100);
        this.clkPanle.add(panel_41);
        
        panel_5.add(label_5);
        panel_5.add(txt_5);
        panel_5.setBounds(10, 270, 280, 30);
        this.clkPanle.add(panel_5);
        
        panel_6.add(button_1);
        panel_6.add(button_2);
        panel_6.setBounds(10, 300, 280, 30);
        this.clkPanle.add(panel_6);
	}
	
	public void actionPerformed(ActionEvent event){
		try{
			if("add".equals(event.getActionCommand())){
				if(StringUtil.isEmpty(txt_4.getText())){
					JOptionPane.showMessageDialog(this, "请输入提示内容！");
					return;
				}
				
				if("具体的某天".equals((String)txt_3.getSelectedItem())&& StringUtil.isEmpty(txt_8.getText())){
					JOptionPane.showMessageDialog(this, "请单击“具体的某天”后的文本框，选择日期！");
					return;
				}
				
				ClockData clkData = new ClockData();
				clkData.setRemindDate(df.format(new Date()));
				clkData.setRemindTime((String)txt_10.getSelectedItem()+txt_11.getSelectedItem());
				clkData.setRemindType((String)txt_2.getSelectedItem());
				String isRepeat = (String)txt_3.getSelectedItem();
				if("具体的某天".equals(isRepeat)){
					isRepeat = txt_8.getText();
				}
				clkData.setIsRepeat(isRepeat);
				clkData.setRemindContent(txt_4.getText());
				clkData.setIsValid((String)txt_5.getSelectedItem());
				
				if(clkAction.update(clkData)){
					this.setVisible(false);
					this.dispose();
				}else{
					JOptionPane.showMessageDialog(this, "添加失败，可能是闹钟时间已设置！");
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
	
	public void mouseClicked(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){
		if(event.getSource().equals(txt_8)){
			dateChooser.showChooser(txt_8, event.getX() - DateChooser.width, event.getY());
			if(dateChooser.getDate() != null)
			txt_8.setText(df.format(dateChooser.getDate()));
		}
	}
	public void mouseReleased(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
	
	public void initComb(){
		txt_10.addItem("00");
		txt_10.addItem("01");
		txt_10.addItem("02");
		txt_10.addItem("03");
		txt_10.addItem("04");
		txt_10.addItem("05");
		txt_10.addItem("06");
		txt_10.addItem("07");
		txt_10.addItem("08");
		txt_10.addItem("09");
		txt_10.addItem("10");
		txt_10.addItem("11");
		txt_10.addItem("12");
		txt_10.addItem("13");
		txt_10.addItem("14");
		txt_10.addItem("15");
		txt_10.addItem("16");
		txt_10.addItem("17");
		txt_10.addItem("18");
		txt_10.addItem("19");
		txt_10.addItem("20");
		txt_10.addItem("21");
		txt_10.addItem("22");
		txt_10.addItem("23");
		
		txt_11.addItem("00");
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
		txt_11.addItem("13");
		txt_11.addItem("14");
		txt_11.addItem("15");
		txt_11.addItem("16");
		txt_11.addItem("17");
		txt_11.addItem("18");
		txt_11.addItem("19");
		txt_11.addItem("20");
		txt_11.addItem("21");
		txt_11.addItem("22");
		txt_11.addItem("23");
		txt_11.addItem("24");
		txt_11.addItem("25");
		txt_11.addItem("26");
		txt_11.addItem("27");
		txt_11.addItem("28");
		txt_11.addItem("29");
		txt_11.addItem("30");
		txt_11.addItem("31");
		txt_11.addItem("32");
		txt_11.addItem("33");
		txt_11.addItem("34");
		txt_11.addItem("35");
		txt_11.addItem("36");
		txt_11.addItem("37");
		txt_11.addItem("38");
		txt_11.addItem("39");
		txt_11.addItem("40");
		txt_11.addItem("41");
		txt_11.addItem("42");
		txt_11.addItem("43");
		txt_11.addItem("44");
		txt_11.addItem("45");
		txt_11.addItem("46");
		txt_11.addItem("47");
		txt_11.addItem("48");
		txt_11.addItem("49");
		txt_11.addItem("50");
		txt_11.addItem("51");
		txt_11.addItem("52");
		txt_11.addItem("53");
		txt_11.addItem("54");
		txt_11.addItem("55");
		txt_11.addItem("56");
		txt_11.addItem("57");
		txt_11.addItem("58");
		txt_11.addItem("59");

		txt_2.addItem("闹钟式");
		txt_2.addItem("便签式");
		
		txt_3.addItem("每天都提醒");
		txt_3.addItem("工作日提醒");
		txt_3.addItem("非工作日提醒");
		txt_3.addItem("具体的某天");
		
		txt_5.addItem("是");
		txt_5.addItem("否");
		
		txt_10.setSelectedItem(clkData.getRemindTime().substring(0,2));
		txt_11.setSelectedItem(clkData.getRemindTime().substring(2,4));
		txt_2.setSelectedItem(clkData.getRemindType());
		String isRepeat = clkData.getIsRepeat();
		if(isRepeat.indexOf("2")==0||isRepeat.indexOf("3")==0){
			isRepeat = "具体的某天";
			txt_8.setText(clkData.getIsRepeat());
		}
		txt_3.setSelectedItem(isRepeat);
		txt_4.setText(clkData.getRemindContent());
		txt_5.setSelectedItem(clkData.getIsValid());
		txt_10.setEnabled(false);
		txt_11.setEnabled(false);
	}
}
