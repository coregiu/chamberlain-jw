package com.eagle.ui.user;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import com.eagle.action.*;
import com.eagle.data.*;
import com.eagle.prop.*;
import com.eagle.ui.MainFrame;
import com.eagle.xmlparse.XMLParser;

/**
 * 登录窗体
 * */
public class LoginDialog extends JDialog implements ActionListener, KeyListener{
	private static final long serialVersionUID = 7951991789530817522L;
	private UserAction userAction = new UserAction();
	JPanel pnlMain ;
	
	JLabel label_1=new JLabel("系统登录");
    JLabel label_2=new JLabel("用    户：");
    JLabel label_3=new JLabel("密    码：");
    JTextField box_1=new JTextField(15);
    JPasswordField passwordfield_1=new JPasswordField(15);
    JButton button_1=new JButton("登录(Ent)");
    JButton button_2=new JButton("退出(Esc)");
    private MainFrame f;
	public LoginDialog(MainFrame f){
		super(f, "系统登录", true);
		this.f = f;
		pnlMain = (JPanel) this.getContentPane();
		body();
		passwordfield_1.requestFocus();
		UIProp.setUIScreen(this, new Dimension(300,200));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void body(){
		this.pnlMain.setLayout(null);
		setBox_1();
		
        FontProp.setFont_b_18_h(label_1);
        label_1.setBounds(100, 10, 100, 30);
        this.pnlMain.add(label_1, Container.LEFT_ALIGNMENT);

        JPanel panel_2=new JPanel();
        panel_2.add(label_2);
        panel_2.add(box_1);
        label_2.setBounds(10, 45, 100, 30);
        box_1.setBounds(110, 45, 150, 30);
        panel_2.setBounds(30, 45, 250, 30);
        this.pnlMain.add(panel_2, Container.LEFT_ALIGNMENT);

        JPanel panel_3=new JPanel();
        panel_3.add(label_3);
        panel_3.add(passwordfield_1);
        label_3.setBounds(10, 80, 100, 30);
        passwordfield_1.setBounds(110, 80, 150, 30);
        panel_3.setBounds(30, 80, 250, 30);
        this.pnlMain.add(panel_3, Container.LEFT_ALIGNMENT);
        
        
        JPanel panel_6=new JPanel();
        button_1.addActionListener(this);
        button_1.setActionCommand("login");
        button_2.addActionListener(this);
        button_2.setActionCommand("exit");
        button_1.addKeyListener(this);
        button_2.addKeyListener(this);
        passwordfield_1.addKeyListener(this);
        panel_6.add(button_1);
        panel_6.add(button_2);       
        button_1.setBounds(10, 150, 100, 30);
        button_2.setBounds(10, 150, 150, 30);
        button_1.setIcon(ImageData.login);
        button_2.setIcon(ImageData.delete);
        panel_6.setBounds(30, 120, 250, 30);
        this.pnlMain.add(panel_6, Container.LEFT_ALIGNMENT);
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event){
		if("login".equals(event.getActionCommand())){
			String username = (String)box_1.getText();
			String password = passwordfield_1.getText();
			char check = userAction.login(username, password);
			switch(check){
				case 'u' :JOptionPane.showMessageDialog(this, "登录用户信息不存在，请检查配置文件！");break;
				case 'p' :JOptionPane.showMessageDialog(this, "密码错误，请重新输入");break;
				default:{
					ConfBean.loadInfo();
					f.initContentPanel();
					this.setVisible(false);
					this.dispose();
				}
			}
		}else{
			this.setVisible(false);
			this.dispose();
		}
	}
	
	public void keyReleased(KeyEvent event){}
	
	public void keyTyped(KeyEvent event){
		if(event.getKeyChar()=='\n'){
			ActionEvent ok=new ActionEvent("",0,"login");
			actionPerformed(ok);
    	}else if(event.getKeyChar()==0x1b){//0x1b表示esc键
    		ActionEvent ok=new ActionEvent("",1,"exit");
    		actionPerformed(ok);
    	}
	}
	
	public void keyPressed(KeyEvent event){}
	
	private void setBox_1(){
		Hashtable<String, UserBean> userInfoList = XMLParser.getUserInfoList();
		Enumeration en = userInfoList.keys();
		while(en.hasMoreElements()){
			String aUsername = (String)en.nextElement();
			//box_1.addItem(aUsername);
			if(aUsername.equals(ConfBean.lastUser)){
				//box_1.setSelectedItem(aUsername);
				box_1.setText(aUsername);
			}
		}		
	}
	
	public static void main(String[] args) {
		new LoginDialog(null);
	}
}
