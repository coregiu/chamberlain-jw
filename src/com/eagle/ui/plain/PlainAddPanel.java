package com.eagle.ui.plain;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.eagle.action.PlainAction;
import com.eagle.data.ImageData;
import com.eagle.data.PlainData;
import com.eagle.prop.FontProp;
import com.eagle.prop.UIProp;

public class PlainAddPanel extends JDialog implements ActionListener, KeyListener, MouseListener{
	private static final long serialVersionUID = 1L;
	private PlainAction plainAction = new PlainAction();
	
	private JLabel label_0=new JLabel("添加工作学习计划",JLabel.CENTER);
    private JLabel label_1=new JLabel("计划编号：",JLabel.CENTER);
    private JLabel label_2=new JLabel("计划标题：",JLabel.CENTER);
    private JLabel label_3=new JLabel("计划内容：",JLabel.CENTER);
    private JTextField txt_1=new JTextField(25);
    private JTextField txt_2=new JTextField(25);
    private JTextArea txt_3=new JTextArea();
    private JButton button_1=new JButton("确定");
    private JButton button_2=new JButton("取消");
    private PlainData plainData;
    private JPanel plnMain;
    
    private Logger log = Logger.getLogger(PlainAddPanel.class);
    
	public PlainAddPanel(JFrame _f, PlainData plainData){
		super(_f, "添加工作学习计划", true);
		this.plainData = plainData;
		plnMain = (JPanel)this.getContentPane();
		body();
		UIProp.setUIScreen(this, new Dimension(400,500));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void body(){
		plnMain.setLayout(null);
		
		FontProp.setFont_b_18_h(label_0);
        button_1.addActionListener(this);
        button_1.addKeyListener(this);
        button_1.setActionCommand("add");
        button_1.setIcon(ImageData.commit);
        button_2.addActionListener(this);
        button_2.addKeyListener(this);
        button_2.setActionCommand("cancel");
        button_2.setIcon(ImageData.delete);
        
        JPanel panel_1=new JPanel();
        JPanel panel_2=new JPanel();
        JPanel panel_3=new JPanel();
        JPanel panel_4=new JPanel();
        
        JPanel panel_8=new JPanel();
        label_0.setBounds(10,10,380,30);
        plnMain.add(label_0);
        panel_1.add(label_1);
        panel_1.add(txt_1);
        panel_2.add(label_2);
        panel_2.add(txt_2);
        panel_3.add(label_3);
        
        txt_3.setLineWrap(true);
        JScrollPane sPanel = new JScrollPane(txt_3);
        sPanel.setPreferredSize(new Dimension(380, 270));
        panel_4.add(sPanel);
        
        txt_1.setEditable(false);
        panel_8.add(button_1);
        panel_8.add(button_2);
        
        panel_1.setBounds(10,40,380,30);
        panel_2.setBounds(10,80,380,30);
        panel_3.setBounds(10,120,380,30);
        panel_4.setBounds(10,150,380,270);
        panel_8.setBounds(10,430,380,30);
        plnMain.add(panel_1);
        plnMain.add(panel_2);
        plnMain.add(panel_3);
        plnMain.add(panel_4);
        plnMain.add(panel_8);
        
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeNow = format.format(new Date());
        
        txt_1.setText(timeNow);
	}
	
	public void actionPerformed(ActionEvent event){
		if("add".equals(event.getActionCommand())){
			if("".equals(txt_2.getText()) || "".equals(txt_3.getText())){
				JOptionPane.showMessageDialog(this, "请输入工作计划！");
				return;
			}
			PlainData plainData = new PlainData();
			plainData.setDate(this.plainData.getDate());
			plainData.setId(txt_1.getText());
			plainData.setTitle(txt_2.getText());
			plainData.setContent(txt_3.getText());
			plainData.setCompleted("否");
			this.plainData.setCurrentPlainData(plainData);
			
			if(plainAction.newPlain(plainData)){
				log.info("添加成功");
				this.setVisible(false);
				this.dispose();
			}else{
				JOptionPane.showMessageDialog(this, "添加失败！");
			}
		}else{
			this.setVisible(false);
			this.dispose();
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
		
	}
	public void mouseReleased(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
}

