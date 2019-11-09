package com.eagle.ui.demo.text;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class th extends JFrame implements ActionListener {

	private static final long serialVersionUID = 5671468520299327574L;

	JTextField jtfold = new JTextField(15);

	JTextField jtfnew = new JTextField(15);

	JButton jb1 = new JButton("替        换");

	JButton jb2 = new JButton("替换所有");

	Test fj;

	public th(Test fj) {
		super("替换");
		this.setLayout(new GridLayout(2, 1));
		JPanel jp1 = new JPanel();
		jp1.add(new JLabel("旧内容"));
		jp1.add(jtfold);
		jp1.add(jb1);
		JPanel jp2 = new JPanel();
		jp2.add(new JLabel("新内容"));
		jp2.add(jtfnew);
		jp2.add(jb2);
		this.add(jp1);
		this.add(jp2);
		this.pack();
		this.setVisible(true);

		this.fj = fj;

		jb1.addActionListener(this);
		jb2.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String scontent = fj.jt.getText();
		String sold = jtfold.getText();
		String snew = jtfnew.getText();
		if (e.getSource().equals(jb1)) {
			// 替换
			String s2 = scontent.replaceFirst(sold, snew);//
			fj.jt.setText(s2);
		}
		if (e.getSource().equals(jb2)) {
			// 替换所有
			String s1 = scontent.replaceAll(sold, snew);
			fj.jt.setText(s1);
		}
	}
}
