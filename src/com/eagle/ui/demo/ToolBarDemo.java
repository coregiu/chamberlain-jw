package com.eagle.ui.demo;

import javax.swing.JFrame;   
import javax.swing.JToolBar;   
import javax.swing.JScrollPane;   
import javax.swing.JButton;   
import javax.swing.ImageIcon;   
import com.eagle.util.FileUtil;
import java.awt.Container;   
import java.awt.BorderLayout;   
import java.awt.TextArea;   
import java.awt.event.ActionListener;   
import java.awt.event.ActionEvent;   
import java.net.URL;   
  
public class ToolBarDemo extends JFrame implements ActionListener {   
    private static final long serialVersionUID = 1L;   
    TextArea textarea;   
  
    public static void main(String arg[]) {   
        new ToolBarDemo();   
    }   
  
    public ToolBarDemo() {   
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        buildFrame();   
        setLocation(250, 150);   
        setSize(300, 200);   
        setVisible(true);   
    }   
  
    private void buildFrame() {   
        Container pane = getContentPane();   
        pane.setLayout(new BorderLayout());   
  
        textarea = new TextArea(4, 20);   
        textarea.setEditable(false);   
        JScrollPane scroll = new JScrollPane(textarea);   
        pane.add(scroll, BorderLayout.CENTER);   
  
        JToolBar toolbar = new JToolBar();// 建立工具栏   
        toolbar.add(makeToolButton(null, "anchor"));   
        toolbar.add(makeToolButton(null, "hammer"));   
        toolbar.add(makeToolButton(null, "file"));   
        toolbar.add(makeToolButton(null, "spinner"));  
        //toolbar.setRollover(true);   
        pane.add(toolbar, BorderLayout.NORTH);   
  
        pack();   
    }   
  
    private JButton makeToolButton(String filename, String command) {// 创建按钮   
        ImageIcon imageicon = getImageIcon("F:\\09-spaceprivate\\IOANALYSIS\\src\\com\\eagle\\ui\\logo\\diary\\save.png");  
        JButton button = new JButton(imageicon);   
        button.setActionCommand(command);   
        button.addActionListener(this);   
        return (button);   
    }   
  
    /** 获取图片图标 */  
    public static final ImageIcon getImageIcon(String url) {   
    	byte[] logoByte = FileUtil.getFileAsByte(com.eagle.ui.MainFrame.class,"logo/menu/123.jpg");
		return new ImageIcon(logoByte);
    }   
  
    /** 获得文件的绝对地址 */  
    public static final URL getURL(String path) {   
        return com.eagle.ui.demo.TrayDemo.class.getResource(path);   
    }   
  
    public void actionPerformed(ActionEvent e) {   
        String selection = e.getActionCommand();   
        textarea.append("\n" + selection);   
        textarea.repaint();   
    }   
}