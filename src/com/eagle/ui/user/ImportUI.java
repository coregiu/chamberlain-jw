package com.eagle.ui.user;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import com.eagle.action.DataBakAction;
import com.eagle.data.ImageData;
import com.eagle.prop.FontProp;
import com.eagle.prop.UIProp;
import com.eagle.ui.MainFrame;
import com.eagle.ui.file.SelectFilePath;

public class ImportUI extends JDialog implements ActionListener, KeyListener{
	private static final long serialVersionUID = 1L;
	private JLabel label_0=new JLabel("导入数据",JLabel.CENTER);
    private JLabel label_1=new JLabel("文件路径：",JLabel.CENTER);
    private JLabel label_2=new JLabel("导入密码验证：",JLabel.CENTER);
    private JTextField txt_1=new JTextField(25);
    private JPasswordField txt_2=new JPasswordField("",20);
    private JButton button_1=new JButton("确定");
    private JButton button_2=new JButton("取消");
    private JButton button_3=new JButton("浏览");
    private boolean flag;
	public ImportUI(MainFrame _f){
		super(_f, "导入数据", true);
		this.add(body());
		UIProp.setUIScreen(this, new Dimension(400,200));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private JComponent body(){
		FontProp.setFont_b_18_h(label_0);
        button_1.addActionListener(this);
        button_1.addKeyListener(this);
        button_1.setActionCommand("add");
        button_2.addActionListener(this);
        button_2.addKeyListener(this);
        button_2.setActionCommand("cancel");
        button_3.addActionListener(this);
        button_3.setActionCommand("file");
        
        JPanel panelAdd=new JPanel(new GridLayout(4,2,1,1));
        JPanel panel_1=new JPanel();
        JPanel panel_2=new JPanel();
        JPanel panel_3=new JPanel();
        panelAdd.add(label_0);
        panel_1.add(label_1);
        panel_1.add(txt_1);
        panel_1.add(button_3);
        panel_2.add(label_2);
        panel_2.add(txt_2);
        panel_3.add(button_1);
        panel_3.add(button_2);
        button_1.setIcon(ImageData.importData);
        button_2.setIcon(ImageData.delete);
        panelAdd.add(panel_1);
        panelAdd.add(panel_2);
        panelAdd.add(panel_3);
        txt_1.setEditable(false);
        return panelAdd;
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event){
		if("add".equals(event.getActionCommand())){
			DataBakAction bakAction = new DataBakAction();
			File file = new File(txt_1.getText());
			if(!file.isFile()){
				JOptionPane.showMessageDialog(this, "请选择导入文件！");
				return;
			}
			if(!file.exists()){
				JOptionPane.showMessageDialog(this, "所填文件不存在！");
				return;
			}
			if(JOptionPane.showConfirmDialog(this, "导入数据会覆盖现有系统数据，确定要导入吗？","覆盖提示",JOptionPane.OK_CANCEL_OPTION)==2){
				return;
			}
			String result = bakAction.importData(txt_1.getText(), txt_2.getText().trim(), true);
			if(result.indexOf("成功")!=-1){
				JOptionPane.showMessageDialog(this, "导入成功！");
				setFlag(true);
				this.setVisible(false);
				this.dispose();
			}else{
				setFlag(false);
				JOptionPane.showMessageDialog(this, result + " 导入失败！");
			}
		}else if("file".equals(event.getActionCommand())){
			SelectFilePath path = new SelectFilePath(this);
			txt_1.setText(path.getFilePath());
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

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}

