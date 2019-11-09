package com.eagle.ui.demo.text;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.*;

public class Test extends JFrame implements ActionListener {
	private static final long serialVersionUID = 2387354062687588164L;

	// 创建菜单
	JMenuBar jm = new JMenuBar();

	JMenu w = new JMenu("文件(F)");

	JMenuItem w1 = new JMenuItem("新建(N)");

	JMenuItem w2 = new JMenuItem("打开(O)");

	JMenuItem w3 = new JMenuItem("保存(S)");

	JMenuItem w4 = new JMenuItem("另存为(A)");

	JMenuItem w5 = new JMenuItem("页面设置(U)");

	JMenuItem w6 = new JMenuItem("打印(P)");

	JMenuItem w7 = new JMenuItem("退出(X)");

	JMenu b = new JMenu("编辑(E)");

	JMenuItem b1 = new JMenuItem("撤销(Z)");

	JMenuItem b2 = new JMenuItem("剪切(X)");

	JMenuItem b3 = new JMenuItem("复制(C)");

	JMenuItem b4 = new JMenuItem("粘贴(V)");

	JMenuItem b5 = new JMenuItem("删除(Del)");

	JMenuItem b6 = new JMenuItem("查找(F)");

	JMenuItem b7 = new JMenuItem("查找下一个(F3)");

	JMenuItem b8 = new JMenuItem("替换(H)");

	JMenuItem b9 = new JMenuItem("转到(G)");

	JMenuItem b10 = new JMenuItem("全选(A)");

	JMenuItem b11 = new JMenuItem("时间/日期(F5)");

	JMenu g = new JMenu("格式(O)");

	JMenuItem g1 = new JMenuItem("自动换行(W)");

	JMenuItem g2 = new JMenuItem("字体(F)...");

	JMenu c = new JMenu("查看(v)");

	JMenuItem c1 = new JMenuItem("状态栏(s)");

	JMenu z = new JMenu("帮助(H)");

	JMenuItem z1 = new JMenuItem("帮助主题(H)");

	JMenuItem z2 = new JMenuItem("关于记事本(A)");

	JTextArea jt = new JTextArea(); // 创建文本区

	JScrollPane js = new JScrollPane(jt); // 创建滚动条

	public Test() {
		super("记事本");
		this.setSize(400, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTabbedPane lb = new JTabbedPane();
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false,lb,js);
		sp.setDividerLocation(135);
		this.add(sp);
		w.add(w1);
		w.add(w2);
		w.add(w3);
		w.add(w4);
		w.add(w5);
		w.add(w6);
		w.add(w7);
		b.add(b1);
		b.add(b2);
		b.add(b3);
		b.add(b4);
		b.add(b5);
		b.add(b6);
		b.add(b7);
		b.add(b8);
		b.add(b9);
		b.add(b10);
		b.add(b11);
		g.add(g1);
		g.add(g2);
		z.add(z1);
		z.add(z2);

		jm.add(w);
		jm.add(b);
		jm.add(g);
		jm.add(z);
		jt.setLineWrap(true);
		this.setJMenuBar(jm);
		this.setVisible(true);
		// 注册监听器

		w2.addActionListener(this);// 打开
		w3.addActionListener(this);// 保存
		w7.addActionListener(this);// 退出

		b3.addActionListener(this);// 复制
		b2.addActionListener(this);// 剪切
		b4.addActionListener(this);// 粘贴

		b6.addActionListener(this);// 查找
		b8.addActionListener(this);// 替换

		g2.addActionListener(this);// 字体
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// 打开
		if (e.getSource().equals(w2)) {
			openFile();
		}
		// 保存
		if (e.getSource().equals(w3)) {
			saveFile();
		}
		// 保存
		if (e.getSource().equals(w7)) {
			int x = JOptionPane.showConfirmDialog(null, "确定要退出吗？", "询问",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			// --按钮个数
			// JOptionPane.YES_NO_OPTION --两个按钮
			// JOptionPane.YES_OPTION --一个按钮
			// JOptionPane.YES_NO_CANCEL_OPTION --三个按钮
			// --图标
			// JOptionPane.QUESTION_MESSAGE --问号
			// JOptionPane.INFORMATION_MESSAGE --叹号
			// JOptionPane.ERROR_MESSAGE --叉号
			// JOptionPane.WARNING_MESSAGE --叹号
			// JOptionPane.PLAIN_MESSAGE --无图标
			if (x == 0) {
				System.exit(0);
			}
		}
		// 复制
		if (e.getSource().equals(b3)) {
			jt.copy();
		}
		// 剪切
		if (e.getSource().equals(b2)) {
			jt.cut();
		}
		// 粘贴
		if (e.getSource().equals(b4)) {
			jt.paste();
		}
		// 查找
		if (e.getSource().equals(b6)) {
			new cz(this);
		}
		// 替换
		if (e.getSource().equals(b8)) {
			new th(this);
		}

	}

	// 打开
	private void openFile() {
		try {
			// --自动弹出窗体
			JFileChooser jfc = new JFileChooser();
			int c = jfc.showOpenDialog(null);
			// --打开文件并读取数据
			if (c == 0) {
				File f1 = jfc.getSelectedFile();

				FileReader fr = new FileReader(f1);

				while (true) {
					int x = fr.read();
					if (x == -1) {
						break;
					}
					jt.append(String.valueOf((char) x));
				}
				fr.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

	}

	// 保存
	public void saveFile() {
		try {
			JFileChooser jfc = new JFileChooser();
			int c = jfc.showSaveDialog(null);
			// --打开文件并读取数据
			if (c == 0) {
				File f1 = jfc.getSelectedFile();

				FileWriter fw = new FileWriter(f1);

				String str = jt.getText();
				fw.write(str);

				fw.close();
				JOptionPane.showMessageDialog(null, "写入成功");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new Test();
		System.out.println(Color.RED.hashCode());//-65536
		System.out.println(Color.BLACK.hashCode());//-16777216
		System.out.println(Color.getColor("SM", -65536));
	}

}