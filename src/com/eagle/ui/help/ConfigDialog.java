package com.eagle.ui.help;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.eagle.action.DataBakAction;
import com.eagle.data.ConfBean;
import com.eagle.data.ConfigData;
import com.eagle.data.ImageData;
import com.eagle.prop.FontProp;
import com.eagle.prop.UIProp;

public class ConfigDialog extends JDialog implements ActionListener, KeyListener{
	private static final long serialVersionUID = 1L;
	private JComboBox box_1=new JComboBox();
    private JComboBox box_2=new JComboBox();
    private JComboBox box_3=new JComboBox();
    private JButton button_1=new JButton("确定");
    private JButton button_2=new JButton("取消");
    
	public ConfigDialog(JFrame f){		
		super(f, "", true);
		this.setTitle("管家配置");
		this.add(body());
		UIProp.setUIScreen(this, new Dimension(280,300));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private JComponent body(){
		JLabel label_0=new JLabel("管家配置",JLabel.CENTER);
	    JLabel label_1=new JLabel("退出系统自动备份数据：",JLabel.CENTER);
	    JLabel label_2=new JLabel("托盘打开系统重新登录：",JLabel.CENTER);
	    JLabel label_3=new JLabel("自动提醒当天计划完成情况：",JLabel.CENTER);
		FontProp.setFont_b_18_h(label_0);
        button_1.addActionListener(this);
        button_1.addKeyListener(this);
        button_1.setActionCommand("config");
        button_1.setIcon(ImageData.config);
        button_2.addActionListener(this);
        button_2.addKeyListener(this);
        button_2.setActionCommand("cancel");
        button_2.setIcon(ImageData.delete);
        
        JPanel panelAdd=new JPanel(new GridLayout(5,2,1,1));
        JPanel panel_1=new JPanel();
        panel_1.setBorder(new TitledBorder("数据备份配置"));
        JPanel panel_2=new JPanel();
        panel_2.setBorder(new TitledBorder("托盘配置"));
        JPanel panel_3=new JPanel();
        panel_3.setBorder(new TitledBorder("自动提醒配置"));
        JPanel panel_4=new JPanel();
        panelAdd.add(label_0);
        panel_1.add(label_1);
        panel_1.add(box_1);
        panel_2.add(label_2);
        panel_2.add(box_2);
        panel_3.add(label_3);
        panel_3.add(box_3);
        panel_4.add(button_1);
        panel_4.add(button_2);
        panelAdd.add(panel_1);
        panelAdd.add(panel_2);
        panelAdd.add(panel_3);
        panelAdd.add(panel_4);
        
        setBox_1();
        setBox_2();
        setBox_3();
        return panelAdd;
	}
	public void actionPerformed(ActionEvent event){
		if("config".equals(event.getActionCommand())){
			String autobakup = "0";
			if("否".equals(box_1.getSelectedItem())){
				autobakup = "1";
			}
			String autoLogin = "0";
			if("否".equals(box_2.getSelectedItem())){
				autoLogin = "1";
			}
			
			String autoRemind = "0";
			if("否".equals(box_3.getSelectedItem())){
				autoRemind = "1";
			}
			DataBakAction bakAction = new DataBakAction();
			ConfigData beanB = new ConfigData();
			beanB.setName("autobakup");
			beanB.setValue(autobakup);
			ConfigData beanL = new ConfigData();
			beanL.setName("autologin");
			beanL.setValue(autoLogin);
			ConfigData beanR = new ConfigData();
			beanR.setName("autoRemind");
			beanR.setValue(autoRemind);
			if(bakAction.systemConfig(beanB)
						&&bakAction.systemConfig(beanL)
						&&bakAction.systemConfig(beanR)){
				JOptionPane.showMessageDialog(this, "修改成功！");
				ConfBean.autobakup = autobakup;
				ConfBean.autologin = autoLogin;
				ConfBean.autoRemind = autoRemind;
				//ConfBean.backupPath = backupField.getText();
				this.setVisible(false);
				this.dispose();
			}else{
				JOptionPane.showMessageDialog(this, "修改失败！");
			}
		}else{
			this.setVisible(false);
			this.dispose();
		}
	}
	
	public void keyReleased(KeyEvent event){}
	
	public void keyTyped(KeyEvent event){
		if(event.getKeyChar()=='\n'){
			ActionEvent ok=new ActionEvent("",0,"delete");
			actionPerformed(ok);
    	}else if(event.getKeyChar()==0x1b){//0x1b表示esc键
    		ActionEvent ok=new ActionEvent("",1,"exit");
    		actionPerformed(ok);
    	}
	}
	
	public void keyPressed(KeyEvent event){}
	
	private void setBox_1(){
		box_1.addItem("否");
		box_1.addItem("是");
		if("0".equals(ConfBean.autobakup)){
			box_1.setSelectedItem("是");
		}
	}
	
	private void setBox_2(){
		box_2.addItem("否");
		box_2.addItem("是");
		if("0".equals(ConfBean.autologin)){
			box_2.setSelectedItem("是");
		}
	}
	
	private void setBox_3(){
		box_3.addItem("否");
		box_3.addItem("是");
		if("0".equals(ConfBean.autoRemind)){
			box_3.setSelectedItem("是");
		}
	}
}
