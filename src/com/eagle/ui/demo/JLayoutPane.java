package com.eagle.ui.demo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JLayoutPane extends JFrame{
	private static final long serialVersionUID = 8055687594683762724L;
	@SuppressWarnings("deprecation")
	public JLayoutPane(){
        super("JLayeredPane");
        
        JLabel label1=new JLabel("左Label",JLabel.CENTER);
        label1.setVerticalAlignment(JLabel.TOP);
        label1.setBackground(Color.red);
        label1.setForeground(Color.black);
        label1.setOpaque(true);
        label1.setBounds(20,20,150,150);
        
        JLabel label2=new JLabel("右sLabe2",JLabel.CENTER);
        label2.setVerticalAlignment(JLabel.TOP);
        label2.setBackground(Color.red);
        label2.setForeground(Color.black);
        label2.setOpaque(true);
        label2.setBounds(50,50,150,150);
        
        JLayeredPane layeredPane=getLayeredPane();
        layeredPane.add(label1,new Integer(10),1);
        layeredPane.add(label2,new Integer(10),0);
    
      	 setSize(new Dimension(280,280));
      	 show();
      	 addWindowListener(
      	 	new WindowAdapter(){
      	 	   public void windowClosing(WindowEvent e){
      	 	      System.exit(0);
      	 	   }
      	    }
         );
    }	
    public static void main(String[] args){
         new JLayoutPane();
    }
}
