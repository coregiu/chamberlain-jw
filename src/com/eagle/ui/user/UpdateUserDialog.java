package com.eagle.ui.user;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.eagle.action.UserAction;
import com.eagle.data.ConfBean;
import com.eagle.data.ImageData;
import com.eagle.data.UserBean;
import com.eagle.prop.FontProp;
import com.eagle.prop.UIProp;
import com.eagle.ui.MainFrame;

public class UpdateUserDialog extends JDialog implements ActionListener, KeyListener{
	private static final long serialVersionUID = 1L;

	private UserAction userAction = new UserAction();
	
	private JLabel label_0=new JLabel("修改用户",JLabel.CENTER);
    private JLabel label_1=new JLabel("用户ID：",JLabel.CENTER);
    private JLabel label_2=new JLabel("用户姓名：",JLabel.CENTER);
    private JLabel label_3=new JLabel("用户密码：",JLabel.CENTER);
    private JTextField txt_1=new JTextField();
    private JTextField txt_2=new JTextField();
    private JPasswordField txt_3=new JPasswordField("",10);
    private JButton button_1=new JButton("确定");
    private JButton button_2=new JButton("取消");
    private MainFrame f;
	public UpdateUserDialog(MainFrame _f){
		super(_f, "修改用户", true);
		f = _f;
		this.add(body());
		UIProp.setUIScreen(this, new Dimension(300,200));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private JComponent body(){
		FontProp.setFont_b_18_h(label_0);
		
		txt_1.setText(ConfBean.loginUserInfo.getUsername());
		txt_1.setEnabled(false);
		txt_2.setText(ConfBean.loginUserInfo.getRealname());
		txt_3.setText(ConfBean.loginUserInfo.getPassword());
		
        button_1.addActionListener(this);
        button_1.addKeyListener(this);
        button_1.setActionCommand("update");
        button_2.addActionListener(this);
        button_2.addKeyListener(this);
        button_2.setActionCommand("cancel");
        
        JPanel panelAdd=new JPanel(new GridLayout(5,2,1,1));
        JPanel panel_1=new JPanel(new GridLayout(1,2,1,1));
        JPanel panel_2=new JPanel(new GridLayout(1,2,1,1));
        JPanel panel_3=new JPanel(new GridLayout(1,2,1,1));
        JPanel panel_4=new JPanel();
        panelAdd.add(label_0);
        panel_1.add(label_1);
        panel_1.add(txt_1);
        panel_2.add(label_2);
        panel_2.add(txt_2);
        panel_3.add(label_3);
        panel_3.add(txt_3);
        panel_4.add(button_1);
        panel_4.add(button_2);
        button_1.setIcon(ImageData.updatePass);
        button_2.setIcon(ImageData.delete);
        panelAdd.add(panel_1);
        panelAdd.add(panel_2);
        panelAdd.add(panel_3);
        panelAdd.add(panel_4);
        return panelAdd;
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event){
		if("update".equals(event.getActionCommand())){
			UserBean userBean = new UserBean();
			userBean.setUsername(txt_1.getText());
			userBean.setRealname(txt_2.getText());
			userBean.setPassword(txt_3.getText());
			userBean.setFilename(txt_1.getText());
			userBean.setRelation("");
			if("".equals(userBean.getUsername().trim())||"".equals(userBean.getPassword().trim())){
				JOptionPane.showMessageDialog(this, "用户名和密码不能为空！");
				return;
			}
			if(userAction.updateUser(userBean)){
				ConfBean.loginUserInfo = userBean;
				ConfBean.reLoad();
				f.initContentPanel();
				this.setVisible(false);
				this.dispose();
			}else{
				JOptionPane.showMessageDialog(this, "修改失败，检查数据！");
			}
		}else{
			this.setVisible(false);
			this.dispose();
		}
	}
	
	public void keyReleased(KeyEvent event){}
	
	public void keyTyped(KeyEvent event){
		if(event.getKeyChar()=='\n'){
			ActionEvent ok=new ActionEvent("",0,"update");
			actionPerformed(ok);
    	}else if(event.getKeyChar()==0x1b){//0x1b表示esc键
    		ActionEvent ok=new ActionEvent("",1,"exit");
    		actionPerformed(ok);
    	}
	}
	
	public void keyPressed(KeyEvent event){}
}

