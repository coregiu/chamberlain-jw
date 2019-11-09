package com.eagle.ui.user;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.*;
import com.eagle.action.UserAction;
import com.eagle.data.ConfBean;
import com.eagle.data.ImageData;
import com.eagle.data.UserBean;
import com.eagle.prop.FontProp;
import com.eagle.prop.UIProp;
import com.eagle.xmlparse.XMLParser;
import com.eagle.ui.MainFrame;

public class DeleteUserDialog extends JDialog implements ActionListener, KeyListener{
	private static final long serialVersionUID = 1L;
	private UserAction userAction = new UserAction();
	
	private JLabel label_0=new JLabel("删除用户",JLabel.CENTER);
    private JLabel label_1=new JLabel("用户ID：",JLabel.CENTER);
    private JComboBox box_1=new JComboBox();
    private JButton button_1=new JButton("删除");
    private JButton button_2=new JButton("查看密码");
    private JButton button_3=new JButton("取消");
	public DeleteUserDialog(MainFrame _f){
		super(_f, "删除用户", true);
		this.add(body());
		UIProp.setUIScreen(this, new Dimension(300,130));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private JComponent body(){
		FontProp.setFont_b_18_h(label_0);
        button_1.addActionListener(this);
        button_1.addKeyListener(this);
        button_1.setActionCommand("delete");
        button_2.addActionListener(this);
        button_2.addKeyListener(this);
        button_2.setActionCommand("pass");
        button_3.addActionListener(this);
        button_3.addKeyListener(this);
        button_3.setActionCommand("cancel");
        
        JPanel panelAdd=new JPanel(new GridLayout(3,2,1,1));
        JPanel panel_1=new JPanel(new GridLayout(1,2,1,1));
        JPanel panel_2=new JPanel();
        panelAdd.add(label_0);
        panel_1.add(label_1);
        panel_1.add(box_1);
        panel_2.add(button_1);
        //panel_2.add(button_2);
        panel_2.add(button_3);

        button_1.setIcon(ImageData.completed);
        button_3.setIcon(ImageData.delete);
        panelAdd.add(panel_1);
        panelAdd.add(panel_2);
        
        setBox_1();
        
        return panelAdd;
	}
	
	public void actionPerformed(ActionEvent event){
		if("delete".equals(event.getActionCommand())){
			UserBean userBean = new UserBean();
			userBean.setUsername((String)box_1.getSelectedItem());
			if("zhangsai".equals(userBean.getUsername())||"hong".equals(userBean.getUsername())||"administrator".equals(userBean.getUsername())){
				JOptionPane.showMessageDialog(this, "此用户不能删除！");
				return;
			}
			String currentUser = ConfBean.loginUserInfo.getUsername();
			if((!("zhangsai".equals(currentUser)||"hong".equals(currentUser)||"administrator".equals(currentUser))) && (!currentUser.equals(userBean.getUsername()))){
				JOptionPane.showMessageDialog(this, "无权删除此用户！");
				return;
			}
			if(JOptionPane.showConfirmDialog(this, "确定要删除"+userBean.getUsername()+"吗？","删除提示",0)==0){
				userAction.deleteUser(userBean);
				if(userBean.getUsername().equals(ConfBean.loginUserInfo.getUsername())){
					JOptionPane.showMessageDialog(this, "当前用户已被删除，系统将关闭");
					System.exit(0);
				}else{
					this.setVisible(false);
					this.dispose();
				}
			}else{
				return;
			}
		}else if("pass".equals(event.getActionCommand())){
			if(!"administrator".equals(ConfBean.loginUserInfo.getUsername())){
				JOptionPane.showMessageDialog(this, "当前用户无权查看！");
				return;
			}
			JOptionPane.showMessageDialog(this, "密码是："+userAction.viewPass((String)box_1.getSelectedItem()));
		}else if("cancel".equals(event.getActionCommand())){
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
		Hashtable<String, UserBean> userInfoList = XMLParser.getUserInfoList();
		Enumeration en = userInfoList.keys();
		while(en.hasMoreElements()){
			String aUsername = (String)en.nextElement();
			box_1.addItem(aUsername);
			if(aUsername.equals(ConfBean.lastUser)){
				box_1.setSelectedItem(aUsername);
			}
		}		
	}
}

