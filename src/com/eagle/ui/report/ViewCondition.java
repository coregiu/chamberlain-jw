package com.eagle.ui.report;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import com.eagle.data.ImageData;
import com.eagle.data.ViewTimeBean;
import com.eagle.prop.UIProp;
import com.eagle.ui.datechooser.DateChooser;
import com.eagle.util.*;

/**
 * 构造报表查询条件页面
 * @author zhangsai
 * @time 20091031
 * */
public class ViewCondition extends JDialog
		implements ActionListener,MouseListener{
	private static final long serialVersionUID = 1L;
	private DateChooser dateChooser;
	private JTextField bgTimeField = new JTextField(20);
	private JTextField edTimeField = new JTextField(20);
	private JRadioButton iType1 = new JRadioButton("详情");
	private JRadioButton iType2 = new JRadioButton("统计",true);
	private JComboBox dtBox = new JComboBox();
	private JComboBox dpBox = new JComboBox();
	private ViewTimeBean vd;

	public ViewCondition(JFrame f, ViewTimeBean vd, char type){
		super(f, "分析选项", true);
		this.vd = vd;
		this.add(inPanel());
		dateChooser=new DateChooser(this);
		UIProp.setUIScreen(this, new Dimension(400,300));
		setItemValid(type);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void setItemValid(char type){
		if(type=='a'){
			iType1.setEnabled(false);
			dtBox.setEnabled(false);
		}
	}
	
	/**
	 * 收入报表查询页面
	 * */
	private JPanel inPanel(){
		JPanel pane = new JPanel();
		
		JLabel title = new JLabel("分析选项",JLabel.CENTER);
		FontUtil.setFont(title);
		JLabel typeLabel = new JLabel("统计类型：   ");
		
		iType1.addActionListener(this);
		iType1.setActionCommand("iType1");
		iType2.addActionListener(this);
		iType2.setActionCommand("iType2");
		ButtonGroup iType = new ButtonGroup();
		iType.add(iType1);
		iType.add(iType2);
		
		JLabel bgTimeLabel = new JLabel("统计起始时间：");
		JLabel edTimeLabel = new JLabel("统计结束时间：");
		JLabel dtLabel = new JLabel("统计详细类型：       ");
		JLabel dpLabel = new JLabel("统计时段：           ");
		
		DateFormat df = new SimpleDateFormat("yyyy-01-01 00:00:00");
		String bgTime = df.format(new Date());
		bgTimeField.setText(bgTime);
		edTimeField.setText(DateUtil.addTime(bgTime, 12));
		bgTimeField.addMouseListener(this);
		edTimeField.addMouseListener(this);
		
		dtBox.addItem("时间");
		dtBox.addItem("类型");
		dtBox.addItem("时间和类型");
		
		dpBox.addItem("月");
		dpBox.addItem("年");
		dpBox.addItem("周");
		dpBox.addItem("日");
		
		JButton sbt = new JButton("确定");
		JButton cl = new JButton("取消");
		sbt.setIcon(ImageData.commit);
		cl.setIcon(ImageData.delete);
		sbt.addActionListener(this);
		sbt.setActionCommand("sbt");
		cl.addActionListener(this);
		cl.setActionCommand("cl");
		
		iType1.addActionListener(this);
		iType1.setActionCommand("iType1");
		iType2.addActionListener(this);
		iType2.setActionCommand("iType2");
		
		pane.setLayout(new GridLayout(7,2,1,1));
		pane.add(title);

		JPanel pane1 = new JPanel();
		pane1.add(typeLabel);
		pane1.add(iType1);
		pane1.add(iType2);

		JPanel pane2 = new JPanel();
		pane2.add(bgTimeLabel);
		pane2.add(bgTimeField);

		JPanel pane3 = new JPanel();
		pane3.add(edTimeLabel);
		pane3.add(edTimeField);
		
		JPanel pane4 = new JPanel();
		pane4.add(dtLabel);
		pane4.add(dtBox);
		
		JPanel pane5 = new JPanel();
		pane5.add(dpLabel);
		pane5.add(dpBox);
		
		JPanel pane6 = new JPanel();
		pane6.add(sbt);
		pane6.add(cl);
		
		pane.add(pane1);
		pane.add(pane2);
		pane.add(pane3);
		pane.add(pane4);
		pane.add(pane5);
		pane.add(pane6);
		return pane;
	}
	
	public void actionPerformed(ActionEvent event){
		String action = event.getActionCommand();
		if("cl".equals(action)){
			vd = null;
			this.setVisible(false);
			this.dispose();
		}else if("sbt".equals(action)){
			vd.setBgTime(bgTimeField.getText());
			vd.setEdTime(edTimeField.getText());
			if(iType1.isSelected()){
				vd.setType('0');
			}else{
				vd.setType('1');
			}
			vd.setDtType((String)dtBox.getSelectedItem());
			vd.setDpType((String)dpBox.getSelectedItem());
			this.setVisible(false);
			this.dispose();
		}else if("iType1".equals(action)){
			setDtBox();
		}else if("iType2".equals(action)){
			setDtBox();
		}
	}
	private void setDtBox(){
		if(iType1.isSelected()){
			dtBox.setSelectedItem("时间");
			dtBox.setEnabled(false);
		}else{
			dtBox.setEnabled(true);
		}
	}
	public void mouseClicked(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){
		if(event.getSource().equals(bgTimeField)){
			dateChooser.showChooser(bgTimeField, event.getX() - DateChooser.width, event.getY());
			if(dateChooser.getDate() != null)
			bgTimeField.setText(new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(dateChooser.getDate()));
		}else if(event.getSource().equals(edTimeField)){
			dateChooser.showChooser(edTimeField, event.getX() - DateChooser.width, event.getY());
			if(dateChooser.getDate() != null)
				edTimeField.setText(new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(dateChooser.getDate()));
		}
	}
	public void mouseReleased(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
}
