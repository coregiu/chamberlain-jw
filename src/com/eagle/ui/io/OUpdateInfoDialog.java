package com.eagle.ui.io;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;

import org.apache.commons.logging.*;
import com.eagle.action.IOConfAction;
import com.eagle.data.ConfBean;
import com.eagle.data.IODataBean;
import com.eagle.data.ImageData;
import com.eagle.prop.*;
import com.eagle.ui.datechooser.DateChooser;
import com.eagle.util.*;
import com.eagle.xmlparse.XMLParser;

public class OUpdateInfoDialog extends JDialog implements ActionListener, KeyListener, MouseListener{
	private static final long serialVersionUID = 1L;
	private IOConfAction ioConfAction = new IOConfAction();
	
	private JLabel label_0=new JLabel("修改支出信息",JLabel.CENTER);
    private JLabel label_1=new JLabel("支出编号：",JLabel.CENTER);
    private JLabel label_2=new JLabel("支出名称：",JLabel.CENTER);
    private JLabel label_3=new JLabel("支出金额：",JLabel.CENTER);
    private JLabel label_5=new JLabel("支出类型：",JLabel.CENTER);
    private JLabel label_6=new JLabel("备注：",JLabel.CENTER);
    private JLabel label_7=new JLabel("时间：",JLabel.CENTER);
    private JTextField txt_1=new JTextField();
    private JTextField txt_2=new JTextField();
    private JTextField txt_3=new JTextField();
    private JComboBox txt_5=new JComboBox();
    private JTextArea txt_6=new JTextArea(10,10);
    private JTextField txt_7=new JTextField();
    private JTextField txt_8=new JTextField("其它类型");
    private JButton button_1=new JButton("确定");
    private JButton button_2=new JButton("取消");
    private DateChooser dateChooser;
    private Log log = LogFactory.getLog(IAddInfoDialog.class);
    
	public OUpdateInfoDialog(JFrame _f, IODataBean _ioDataBean){
		super(_f, "修改支出信息", true);
		dateChooser=new DateChooser(this);
		this.add(body(_ioDataBean));
		UIProp.setUIScreen(this, new Dimension(300,320));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private JComponent body(IODataBean ioDataBean){
		FontProp.setFont_b_18_h(label_0);
		button_1.addActionListener(this);
        button_1.addKeyListener(this);
        button_1.setActionCommand("add");
        button_1.setIcon(ImageData.commit);
        button_2.addActionListener(this);
        button_2.addKeyListener(this);
        button_2.setActionCommand("cancel");
        button_2.setIcon(ImageData.delete);
        
        JPanel panelAdd=new JPanel(new GridLayout(10,2,1,1));
        JPanel panel_1=new JPanel(new GridLayout(1,2,1,1));
        JPanel panel_2=new JPanel(new GridLayout(1,2,1,1));
        JPanel panel_3=new JPanel(new GridLayout(1,2,1,1));
        JPanel panel_5=new JPanel(new GridLayout(1,3,1,1));
        JPanel panel_6=new JPanel(new GridLayout(1,2,1,1));
        JPanel panel_7=new JPanel(new GridLayout(1,2,1,1));
        JPanel panel_8=new JPanel(new GridLayout(1,2,1,1));
        JPanel panel_9=new JPanel();
        panelAdd.add(label_0);
        panel_1.add(label_1);
        panel_1.add(txt_1);
        panel_2.add(label_2);
        panel_2.add(txt_2);
        panel_3.add(label_3);
        panel_3.add(txt_3);
        panel_5.add(label_5);
        panel_5.add(txt_5);
        panel_5.add(txt_8);
        panel_6.add(label_6);
        JScrollPane sPanel = new JScrollPane(txt_6);
        sPanel.setPreferredSize(new Dimension(270, 60));
        panel_7.add(sPanel);
        panel_8.add(label_7);
        panel_8.add(txt_7);
        panel_9.add(button_1);
        panel_9.add(button_2);
        panelAdd.add(panel_1);
        panelAdd.add(panel_2);
        panelAdd.add(panel_3);
        panelAdd.add(panel_5);
        panelAdd.add(panel_6);
        panelAdd.add(panel_7);
        panelAdd.add(panel_8);
        panelAdd.add(panel_9);
        
        DataUtil.setOType(txt_5);
        txt_6.setLineWrap(true);
        txt_7.addMouseListener(this);
        
        txt_1.setText(ioDataBean.getOId());
        txt_1.setEditable(false);
        txt_2.setText(ioDataBean.getOName());
        txt_3.setText(ioDataBean.getOMoney());
        txt_5.setSelectedItem(ioDataBean.getOType());
        txt_6.setText(ioDataBean.getOResume());
        txt_7.setText(ioDataBean.getODate());
        txt_5.addItem("其它类型");
        
        return panelAdd;
	}
	
	public void actionPerformed(ActionEvent event){
		if("add".equals(event.getActionCommand())){
			if("".equals(txt_2.getText()) || "".equals(txt_3.getText())){
				JOptionPane.showMessageDialog(this, "收入名称和收入总金额不能为空！");
				return;
			}
			try{
				Double.parseDouble(txt_3.getText());
			}catch(Exception e){
				JOptionPane.showMessageDialog(this, "金额项必须填写正确的数字！");
				return;
			}
			IODataBean ioDataBean = new IODataBean();
			ioDataBean.setOId(txt_1.getText());
			ioDataBean.setOName(txt_2.getText());
			ioDataBean.setOMoney(txt_3.getText());
			ioDataBean.setOType((String)((txt_5.getSelectedItem()==null||"其它类型".equals(txt_5.getSelectedItem()))?("".equals(txt_8.getText())?"其它类型":txt_8.getText()):txt_5.getSelectedItem()));
			ioDataBean.setOResume(txt_6.getText());
			ioDataBean.setODate(txt_7.getText());
			
			if(ioConfAction.updateOInfo(ioDataBean)){
				ConfBean.loadIOInfo();
				log.info(XMLParser.getIOText());
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
		if(event.getSource().equals(txt_7)){
			dateChooser.showChooser(txt_7, event.getX() - DateChooser.width, event.getY());
			if(dateChooser.getDate() != null)
			txt_7.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateChooser.getDate()));
		}
	}
	public void mouseReleased(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
}