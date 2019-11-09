package com.eagle.ui.user;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.jfree.ui.ExtensionFileFilter;
import com.eagle.action.DataBakAction;
import com.eagle.data.ImageData;
import com.eagle.prop.FontProp;
import com.eagle.prop.UIProp;
import com.eagle.ui.MainFrame;
import com.eagle.util.StringUtil;

public class ExportUI extends JDialog implements ActionListener, KeyListener{
	private static final long serialVersionUID = -8692636903611041108L;
	private JLabel label_0=new JLabel("导出数据",JLabel.CENTER);
    private JLabel label_1=new JLabel("文件路径：",JLabel.CENTER);
    private JLabel label_2=new JLabel("导出密码设置：",JLabel.CENTER);
    private JLabel label_3=new JLabel("再次输入密码：",JLabel.CENTER);
    private JTextField txt_1=new JTextField(25);
    private JPasswordField txt_2=new JPasswordField("",20);
    private JPasswordField txt_3=new JPasswordField("",20);
    private JButton button_1=new JButton("确定");
    private JButton button_2=new JButton("取消");
    private JButton button_3=new JButton("浏览");
    private boolean flag;
	public ExportUI(MainFrame _f){
		super(_f, "导出数据", true);
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
        
        JPanel panelAdd=new JPanel(new GridLayout(5,2,1,1));
        JPanel panel_1=new JPanel();
        JPanel panel_2=new JPanel();
        JPanel panel_3=new JPanel();
        JPanel panel_4=new JPanel();
        panelAdd.add(label_0);
        panel_1.add(label_1);
        panel_1.add(txt_1);
        panel_1.add(button_3);
        panel_2.add(label_2);
        panel_2.add(txt_2);
        panel_3.add(label_3);
        panel_3.add(txt_3);
        panel_4.add(button_1);
        panel_4.add(button_2);
        button_1.setIcon(ImageData.exportData);
        button_2.setIcon(ImageData.delete);
        panelAdd.add(panel_1);
        panelAdd.add(panel_2);
        panelAdd.add(panel_3);
        panelAdd.add(panel_4);
        
        txt_1.setEditable(false);
        return panelAdd;
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event){
		if("add".equals(event.getActionCommand())){
			String filepath = txt_1.getText();
			String password = txt_2.getText();
			String passconf = txt_3.getText();
			if(StringUtil.isEmpty(password)){
				JOptionPane.showMessageDialog(this, "为了安全起见，请输入导出密码！");
				return;
			}
			if(!password.equals(passconf)){
				JOptionPane.showMessageDialog(this, "两次输入的密码不一致，请重新输入！");
				return;
			}
			if(StringUtil.isEmpty(filepath)){
				JOptionPane.showMessageDialog(this, "请确认输出文件路径！");
				return;
			}
			DataBakAction bakAction = new DataBakAction();
			boolean result = bakAction.exportData(filepath, password);
			if(result){
				JOptionPane.showMessageDialog(this, "导出成功！");
				this.setVisible(false);
				this.dispose();
			}else{
				JOptionPane.showMessageDialog(this, "导出失败！");
			}
		}else if("file".equals(event.getActionCommand())){
			JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setCurrentDirectory(null);
	        ExtensionFileFilter filter = new ExtensionFileFilter("*.io", ".io");
	        fileChooser.addChoosableFileFilter(filter);

	        int option = fileChooser.showSaveDialog(this);
	        if (option == JFileChooser.APPROVE_OPTION) {
	            String filename = fileChooser.getSelectedFile().getPath();
                if (!filename.endsWith(".io")) {
                    filename = filename + ".io";
                }
                txt_1.setText(filename);
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

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}

