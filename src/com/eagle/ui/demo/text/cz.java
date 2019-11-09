package com.eagle.ui.demo.text;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class cz extends JFrame implements ActionListener {

	private static final long serialVersionUID = 865059732091510690L;

	JTextField jtf = new JTextField(15);

	JButton jb = new JButton("查找");

	Test fj;

	public cz(Test fj) {
		super("查找");
		this.setLayout(new FlowLayout());
		this.add(new JLabel("查找内容"));
		this.add(jtf);
		this.add(jb);
		this.pack();
		this.setVisible(true);
		// 获得父窗体的引用
		this.fj = fj;

		jb.addActionListener(this);
	}

	int ci = 0;

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(jb)) {
			// np.jta.append(jtf.getText());
			String scontent = fj.jt.getText();
			String sfind = jtf.getText();
			int x = scontent.indexOf(sfind, ci);
			System.out.println("ci=" + ci);
			System.out.println("x=" + x);
			if (x == -1) {
				JOptionPane.showMessageDialog(null, "没有找到！");
			} else {
				fj.jt.setSelectionStart(x);
				fj.jt.setSelectionEnd(x + sfind.length());
				ci = x;
				ci += 1;
			}
		}
	}

}
