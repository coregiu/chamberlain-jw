package com.eagle.ui.ssq;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

import com.eagle.data.ImageData;
import com.eagle.lottery.SSQLottery;
import com.eagle.prop.NameProp;
import com.eagle.prop.UIProp;
import com.eagle.ui.MainFrame;

/**
 * 双色球
 * 
 * @author eagle
 *
 */
public class SSQCheckUI extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
    private JLabel label_1=new JLabel("机选注数：",JLabel.CENTER);
    private JTextField txt_1=new JTextField(10);
    private JTextArea txt_2=new JTextArea();
    private JButton button_1=new JButton("机选");
    private JButton button_2=new JButton("取消");
	public SSQCheckUI(MainFrame _f){
		super(_f, "双色球机选", true);
		this.add(body());
		UIProp.setUIScreen(this, new Dimension(400,400));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public JPanel body(){
        button_1.addActionListener(this);
        button_1.setActionCommand("check");
        button_1.setIcon(ImageData.commit);
        button_2.addActionListener(this);
        button_2.setActionCommand("cancel");
        button_2.setIcon(ImageData.delete);
        
        txt_2.setLineWrap(true);
        txt_2.setAutoscrolls(true);
        
        JPanel panelAdd=new JPanel();
        JPanel panel_1=new JPanel();
        JPanel panel_2=new JPanel(new GridLayout(1,2,1,1));
        JScrollPane sPanel = new JScrollPane(txt_2);
        sPanel.setPreferredSize(new Dimension(390,300));
        panel_1.add(label_1);
        panel_1.add(txt_1);
        panel_1.add(button_1);
        panel_1.add(button_2);
        panel_2.add(sPanel);
        panelAdd.add(panel_1);
        panelAdd.add(panel_2);
        return panelAdd;
	}
	
	public void actionPerformed(ActionEvent event){
		if("check".equals(event.getActionCommand())){
			StringBuffer buffer = new StringBuffer();
			SSQLottery ssq;
			int num = 1;
			try{
				num = Integer.parseInt(txt_1.getText().trim());
				if(num>NameProp.LOTTERY_MAX_NUM){
					JOptionPane.showMessageDialog(this, "别整那么多，小赌才怡情!");
					return;
				}
			}catch(Exception e){
				
			}
			for(int i=0;i<num;i++){
				ssq = new SSQLottery();
				buffer.append(ssq.getValuesAsStrFmt());
				buffer.append("\n");
			}
			
			txt_2.setText(buffer.toString());
		}else if("cancel".equals(event.getActionCommand())){
			this.dispose();
		}
	}
}
