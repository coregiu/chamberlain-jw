package com.eagle.ui.calculator;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.eagle.action.MediaAction;
import com.eagle.data.ImageData;
import com.eagle.prop.UIProp;

/**
 * 计算器
 * 
 * @author szhang
 * 
 */
public class Calculator extends JDialog implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	// 声明三个面板的布局
	GridLayout gl1, gl2, gl3;

	JPanel p0, p1, p2, p3;

	JTextField tf1;

	JTextField tf2;

	JButton b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14,
			b15, b16, b17, b18, b19, b20, b21, b22, b23, b24, b25, b26;

	StringBuffer str;// 显示屏所显示的字符串

	double x, y;// x和y都是运算数

	int z;// Z表示单击了那一个运算符.0表示"+",1表示"-",2表示"*",3表示"/"

	static double m;// 记忆的数字
	
	private MediaAction mdaAction = new MediaAction();

	public Calculator(JFrame f) {
		super(f, "计算器", true);
		
		gl1 = new GridLayout(1, 4, 10, 0);// 实例化三个面板的布局
		gl2 = new GridLayout(4, 1, 0, 15);
		gl3 = new GridLayout(4, 5, 10, 15);
		tf1 = new JTextField(26);// 显示屏
		tf1.setBackground(Color.BLACK);
		tf1.setHorizontalAlignment(JTextField.RIGHT);
		tf1.setEnabled(false);
		tf1.setText("0");
		tf2 = new JTextField(10);// 显示记忆的索引值
		tf2.setEditable(false);
		// 实例化所有按钮、设置其前景色并注册监听器
		b0 = new JButton("Backspace");
		b0.setForeground(Color.red);
		b0.addActionListener(this);
		b1 = new JButton("CE");
		b1.setForeground(Color.red);
		b1.addActionListener(this);
		b2 = new JButton("C");
		b2.setForeground(Color.red);
		b2.addActionListener(this);
		b3 = new JButton("MC");
		b3.setForeground(Color.red);
		b3.addActionListener(this);
		b4 = new JButton("MR");
		b4.setForeground(Color.red);
		b4.addActionListener(this);
		b5 = new JButton("MS");
		b5.setForeground(Color.red);
		b5.addActionListener(this);
		b6 = new JButton("M+");
		b6.setForeground(Color.red);
		b6.addActionListener(this);
		b7 = new JButton("7");
		b7.setForeground(Color.blue);
		b7.addActionListener(this);
		b8 = new JButton("8");
		b8.setForeground(Color.blue);
		b8.addActionListener(this);
		b9 = new JButton("9");
		b9.setForeground(Color.blue);
		b9.addActionListener(this);
		b10 = new JButton("/");
		b10.setForeground(Color.red);
		b10.addActionListener(this);
		b11 = new JButton("sqrt");
		b11.setForeground(Color.blue);
		b11.addActionListener(this);
		b12 = new JButton("4");
		b12.setForeground(Color.blue);
		b12.addActionListener(this);
		b13 = new JButton("5");
		b13.setForeground(Color.blue);
		b13.addActionListener(this);
		b14 = new JButton("6");
		b14.setForeground(Color.blue);
		b14.addActionListener(this);
		b15 = new JButton("*");
		b15.setForeground(Color.red);
		b15.addActionListener(this);
		b16 = new JButton("%");
		b16.setForeground(Color.blue);
		b16.addActionListener(this);
		b17 = new JButton("1");
		b17.setForeground(Color.blue);
		b17.addActionListener(this);
		b18 = new JButton("2");
		b18.setForeground(Color.blue);
		b18.addActionListener(this);
		b19 = new JButton("3");
		b19.setForeground(Color.blue);
		b19.addActionListener(this);
		b20 = new JButton("-");
		b20.setForeground(Color.red);
		b20.addActionListener(this);
		b21 = new JButton("1/X");
		b21.setForeground(Color.blue);
		b21.addActionListener(this);
		b22 = new JButton("0");
		b22.setForeground(Color.blue);
		b22.addActionListener(this);
		b23 = new JButton("+/-");
		b23.setForeground(Color.blue);
		b23.addActionListener(this);
		b24 = new JButton(".");
		b24.setForeground(Color.blue);
		b24.addActionListener(this);
		b25 = new JButton("+");
		b25.setForeground(Color.red);
		b25.addActionListener(this);
		b26 = new JButton("=");
		b26.setForeground(Color.red);
		b26.addActionListener(this);

		// 实例化四个面板
		p0 = new JPanel();
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		// 创建一个空字符串缓冲区
		str = new StringBuffer();

		// 添加面板p0中的组件和设置其在框架中的位置和大小
		p0.add(tf1);
		p0.setBounds(10, 5, 300, 40);
		// 添加面板p1中的组件和设置其在框架中的位置和大小
		p1.setLayout(gl1);
		p1.add(tf2);
		p1.add(b0);
		p1.add(b1);
		p1.add(b2);
		p1.setBounds(10, 45, 300, 25);
		// 添加面板p2中的组件并设置其的框架中的位置和大小
		p2.setLayout(gl2);
		p2.add(b3);
		p2.add(b4);
		p2.add(b5);
		p2.add(b6);
		p2.setBounds(10, 90, 40, 150);
		// 添加面板p3中的组件并设置其在框架中的位置和大小
		p3.setLayout(gl3);// 设置p3的布局
		p3.add(b7);
		p3.add(b8);
		p3.add(b9);
		p3.add(b10);
		p3.add(b11);
		p3.add(b12);
		p3.add(b13);
		p3.add(b14);
		p3.add(b15);
		p3.add(b16);
		p3.add(b17);
		p3.add(b18);
		p3.add(b19);
		p3.add(b20);
		p3.add(b21);
		p3.add(b22);
		p3.add(b23);
		p3.add(b24);
		p3.add(b25);
		p3.add(b26);
		p3.setBounds(60, 90, 250, 150);
		
		p0.addKeyListener(this);
		p1.addKeyListener(this);
		p2.addKeyListener(this);
		p3.addKeyListener(this);
		tf1.addKeyListener(this);
		tf2.addKeyListener(this);
		b0.addKeyListener(this);
		b1.addKeyListener(this);
		b2.addKeyListener(this);
		b3.addKeyListener(this);
		b4.addKeyListener(this);
		b5.addKeyListener(this);
		b6.addKeyListener(this);
		b7.addKeyListener(this);
		b8.addKeyListener(this);
		b9.addKeyListener(this);
		b10.addKeyListener(this);
		b11.addKeyListener(this);
		b12.addKeyListener(this);
		b13.addKeyListener(this);
		b14.addKeyListener(this);
		b15.addKeyListener(this);
		b16.addKeyListener(this);
		b17.addKeyListener(this);
		b18.addKeyListener(this);
		b19.addKeyListener(this);
		b20.addKeyListener(this);
		b21.addKeyListener(this);
		b22.addKeyListener(this);
		b23.addKeyListener(this);
		b24.addKeyListener(this);
		b25.addKeyListener(this);
		b26.addKeyListener(this);
		// 设置框架中的布局为空布局并添加4个面板
		setLayout(null);
		add(p0);
		add(p1);
		add(p2);
		add(p3);
		setResizable(false);// 禁止调整框架的大小
		setBackground(Color.lightGray);
		UIProp.setUIScreen(this, new Dimension(320, 300));
		setJMenu();
		this.setJMenuBar(menuBar);
		setVisible(true);
	}

	public static void main(String args[]) {
		new Calculator(null);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			if(e.getActionCommand().equals("media")){
				if(mdaAction.isPlay()){
					mdaAction.setPlay(false);
				}else{
					mdaAction.setPlay(true);
				}
			}else if(e.getActionCommand().equals("resume")){
				JOptionPane.showMessageDialog(this, "计算器支持快捷键操作(不分大小写)\n q-加，w-减，e-乘，r-除\n c(esc)-清零，回车(=)-等于");
			}
			// 计算器逻辑开始
			else if (e.getSource() == b1)// 选择"CE"清零
			{
				mdaAction.readPprocess.setRunFlag(false);
				mdaAction.playCal(clear, adp);
				tf1.setText("0");// 把显示屏清零
				str.setLength(0);// 清空字符串缓冲区以准备接收新的输入运算数
			} else if (e.getSource() == b2)// 选择"C"清零
			{
				mdaAction.readPprocess.setRunFlag(false);
				mdaAction.playCal(zero, adp);
				tf1.setText("0");// 把显示屏清零
				str.setLength(0);
			} else if (e.getSource() == b23)// 单击"+/-"选择输入的运算数是正数还是负数
			{
				x = Double.parseDouble(tf1.getText().trim());
				if(x>0){
					mdaAction.playCal(un, adp);
				}else{
					mdaAction.playCal(n, adp);
				}
				tf1.setText("" + (-x));
			} else if (e.getSource() == b25)// 单击加号按钮获得x的值和z的值并清空y的值
			{
				mdaAction.playCal(add, adp);
				x = Double.parseDouble(tf1.getText().trim());
				str.setLength(0);// 清空缓冲区以便接收新的另一个运算数
				y = 0d;
				z = 0;
			} else if (e.getSource() == b20)// 单击减号按钮获得x的值和z的值并清空y的值
			{
				mdaAction.playCal(less, adp);
				x = Double.parseDouble(tf1.getText().trim());
				str.setLength(0);
				y = 0d;
				z = 1;
			} else if (e.getSource() == b15)// 单击乘号按钮获得x的值和z的值并清空y的值
			{
				mdaAction.playCal(multiply, adp);
				x = Double.parseDouble(tf1.getText().trim());
				str.setLength(0);
				y = 0d;
				z = 2;
			} else if (e.getSource() == b10)// 单击除号按钮获得x的值和z的值并空y的值
			{
				mdaAction.playCal(divid, adp);
				x = Double.parseDouble(tf1.getText().trim());
				str.setLength(0);
				y = 0d;
				z = 3;
			} else if (e.getSource() == b26)// 单击等号按钮输出计算结果
			{
				str.setLength(0);
				String value="";
				switch (z) {
				case 0:
					value = "" + (x + y);
					break;
				case 1:
					value = "" + (x - y);
					break;
				case 2:
					value = "" + (x * y);
					break;
				case 3:
					value = "" + (x / y);
					break;
				default:
					return;
				}
				tf1.setText(value);
				mdaAction.playEqual(value);
			} else if (e.getSource() == b24)// 单击"."按钮输入小数
			{
				
				if (tf1.getText().trim().indexOf(".") != -1)// 判断字符串中是否已经包含了小数点
				{

				} else// 如果没数点有小
				{
					mdaAction.playCal(dot, adp);
					if (tf1.getText().trim().equals("0"))// 如果初时显示为0
					{
						str.setLength(0);
						tf1.setText((str.append("0" + e.getActionCommand()))
								.toString());
					} else if (tf1.getText().trim().equals(""))// 如果初时显示为空则不做任何操作
					{
					} else {
						tf1.setText(str.append(e.getActionCommand()).toString());
					}
				}

				y = 0d;

			} else if (e.getSource() == b11)// 求平方根
			{
				mdaAction.playCal(sqrt, adp);
				x = Double.parseDouble(tf1.getText().trim());
				tf1.setText("数字格式异常");
				if (x < 0)
					tf1.setText("负数没有平方根");
				else
					tf1.setText("" + Math.sqrt(x));
				str.setLength(0);
				y = 0d;
			} else if (e.getSource() == b16)// 单击了"%"按钮
			{
				mdaAction.playCal(bfh, adp);
				x = Double.parseDouble(tf1.getText().trim());
				tf1.setText("" + (0.01 * x));
				str.setLength(0);
				y = 0d;
			} else if (e.getSource() == b21)// 单击了"1/X"按钮
			{
				mdaAction.playCal(oneof, adp);
				x = Double.parseDouble(tf1.getText().trim());
				if (x == 0) {

					tf1.setText("除数不能为零");
				} else {
					tf1.setText("" + (1 / x));
				}
				str.setLength(0);
				y = 0d;
			} else if (e.getSource() == b3)// MC为清除内存
			{
				mdaAction.playCal(mc, adp);
				m = 0d;
				tf2.setText("");
				str.setLength(0);
			} else if (e.getSource() == b4)// MR为重新调用存储的数据
			{
				mdaAction.playCal(mr, adp);
				if (tf2.getText().trim() != "")// 有记忆数字
				{
					tf1.setText("" + m);
				}
			} else if (e.getSource() == b5)// MS为存储显示的数据
			{
				mdaAction.playCal(ms, adp);
				m = Double.parseDouble(tf1.getText().trim());
				tf2.setText("M");
				tf1.setText("0");
				str.setLength(0);
			} else if (e.getSource() == b6)// M+为将显示的数字与已经存储的数据相加要查看新的数字单击MR
			{
				mdaAction.playCal(ma, adp);
				m = m + Double.parseDouble(tf1.getText().trim());
			} else// 选择的是其他的按钮
			{
				if (e.getSource() == b22)// 如果选择的是"0"这个数字键
				{
					if (tf1.getText().trim().equals("0"))// 如果显示屏显示的为零不做操作
					{

					} else {
						mdaAction.playCal(mArr[0], adp);
						tf1.setText(str.append(e.getActionCommand())
										.toString());
						y = Double.parseDouble(tf1.getText().trim());
					}
				} else if (e.getSource() == b0)// 选择的是“BackSpace”按钮
				{
					mdaAction.playCal(back, adp);
					if (!tf1.getText().trim().equals("0"))// 如果显示屏显示的不是零
					{
						if (str.length() != 1) {
							tf1.setText(str.delete(str.length() - 1,
									str.length()).toString());// 可能抛出字符串越界异常
						} else {
							tf1.setText("0");
							str.setLength(0);
						}
					}
					y = Double.parseDouble(tf1.getText().trim());
				} else// 其他的数字键
				{
					mdaAction.playCal(mArr[Integer.parseInt(e.getActionCommand())], adp);
					tf1.setText(str.append(e.getActionCommand()).toString());
					y = Double.parseDouble(tf1.getText().trim());
				}
			}
		} catch (NumberFormatException exp) {
			tf1.setText("数字格式异常");
		} catch (StringIndexOutOfBoundsException exp) {
			tf1.setText("字符串索引越界");
		}
	}

	public void keyPressed(KeyEvent event) {
		// TODO Auto-generated method stub
		ActionEvent action;
		if(event.getKeyChar()==0x08){
			action=new ActionEvent(b0,0,"backspace");
			actionPerformed(action);
    	}else if(event.getKeyChar()==0x1b){
    		action=new ActionEvent(b1,1,"CE");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x43||event.getKeyChar()==0x63){
    		action=new ActionEvent(b2,2,"C");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x78){
    		action=new ActionEvent(b3,3,"MC");//F9
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x79){
    		action=new ActionEvent(b4,4,"MR");//F10
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x7a){
    		action=new ActionEvent(b5,5,"MS");//F11
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x7b){
    		action=new ActionEvent(b6,6,"M+");//F12
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x37){
    		action=new ActionEvent(b7,7,"7");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x38){
    		action=new ActionEvent(b8,8,"8");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x39){
    		action=new ActionEvent(b9,9,"9");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x2f||event.getKeyChar()==0x52||event.getKeyChar()==0x72){//F4
    		action=new ActionEvent(b10,10,"/");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x74){//F5
    		action=new ActionEvent(b11,11,"sqrt");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x34){
    		action=new ActionEvent(b12,12,"4");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x35){
    		action=new ActionEvent(b13,13,"5");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x36){
    		action=new ActionEvent(b14,14,"6");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x2a||event.getKeyChar()==0x45||event.getKeyChar()==0x65){//F3
    		action=new ActionEvent(b15,15,"*");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x25||event.getKeyChar()==0x75){//F6
    		action=new ActionEvent(b16,16,"%");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x31){
    		action=new ActionEvent(b17,17,"1");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x32){
    		action=new ActionEvent(b18,18,"2");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x33){
    		action=new ActionEvent(b19,19,"3");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x2d||event.getKeyChar()==0x57||event.getKeyChar()==0x77){//w
    		action=new ActionEvent(b20,20,"-");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x24){//HOME
    		action=new ActionEvent(b21,21,"1/x");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x30){
    		action=new ActionEvent(b22,22,"0");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x23){//END
    		action=new ActionEvent(b23,23,"+/-");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x2e){//
    		action=new ActionEvent(b24,24,".");
    		actionPerformed(action);
    	}else if(event.getKeyChar()==0x2b
    			||event.getKeyChar()==0x51||event.getKeyChar()==0x71){// q
    		action=new ActionEvent(b25,25,"+");
    		actionPerformed(action);
    	}else if(event.getKeyChar()=='\n'||event.getKeyChar()==0x3d){
    		action=new ActionEvent(b26,26,"=");
    		actionPerformed(action);
    	}
	}

	public void keyReleased(KeyEvent event) {
		// TODO Auto-generated method stub
	}

	public void keyTyped(KeyEvent event) {
		// TODO Auto-generated method stub
	}
	
	private void setJMenu(){
		menuBar.removeAll();
		JMenu cfg = new JMenu("配置及说明");
		
		JMenuItem media = new JMenuItem("取消/使用声音", ImageData.clock);
		media.addActionListener(this);
		media.setActionCommand("media");
		media.setAccelerator(KeyStroke.getKeyStroke('M',Event.ALT_MASK,false));
		
		JMenuItem help = new JMenuItem("快捷键说明", ImageData.help);
		help.addActionListener(this);
		help.setActionCommand("resume");
		help.setAccelerator(KeyStroke.getKeyStroke('H',Event.ALT_MASK,false));
		
		cfg.add(media);
		cfg.addSeparator();
		cfg.add(help);
		
		menuBar.add(cfg);
	}
	private JMenuBar menuBar = new JMenuBar();;
	private AudioClip adp;
	
	private String clear = "clear.wav";
	private String back = "back.wav";
	private String dot = "dot.wav";
	private String add = "add.wav";
	private String less = "less.wav";
	private String multiply = "multiply.wav";
	private String divid = "divid.wav";
	private String un = "un.wav";// 负数
	private String n = "n.wav";// 正数
	private String bfh = "%.wav";//等于
	private String oneof = "oneof.wav";
	private String mr = "MR.wav";
	private String ms = "MS.wav";
	private String ma = "M+.wav";
	private String mc = "MC.wav";
	private String sqrt = "sqrt.wav";
	private String zero = "zero.wav";
	private String mArr[] = {"a0.wav","a1.wav","a2.wav","a3.wav","a4.wav","a5.wav","a6.wav","a7.wav","a8.wav","a9.wav"};
}
