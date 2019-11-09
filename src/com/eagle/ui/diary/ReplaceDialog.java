package com.eagle.ui.diary;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.eagle.prop.NameProp;

/**
 * "替换"对话框
 * 
 * @author chen
 * 
 */
public class ReplaceDialog extends JDialog implements ActionListener, CaretListener {

	private static final long serialVersionUID = -7602293855824973899L;

	JLabel lblFindText = new JLabel("查找内容：");

	JLabel lblReplaceText = new JLabel("替换内容：");

	JTextField txtFindText = new JTextField();

	JTextField txtReplaceText = new JTextField();

	JCheckBox chkNotIgnoreCase = new JCheckBox("区分大小写", false);

	JButton btnFind = new JButton("查找下一个(F)");

	JButton btnReplace = new JButton("替换(R)");

	JButton btnReplaceAll = new JButton("全部替换(A)");

	JButton btnCancel = new JButton("取消");

	JPanel pnlMain = (JPanel) this.getContentPane();

	boolean isIgnoreCase = true; // 忽略大小写

	Insets insets = new Insets(1, 1, 1, 1);

	EscKeyAdapter escKeyAdapter = new EscKeyAdapter(this);

	private MyBook txaMain;

	private String strTitle = NameProp.TITLE;

	private StringBuffer stbText = new StringBuffer();// 当前文本域中的字符串

	private StringBuffer stbFindText = new StringBuffer();

	JRadioButton radFindUp = new JRadioButton("向上", false);

	JRadioButton radFindDown = new JRadioButton("向下", false);

	JRadioButton circleFind = new JRadioButton("全文", true);

	ButtonGroup bgpFindUpDown = new ButtonGroup();

	JPanel pnlFindUpDown = new JPanel(new GridLayout(1, 2));

	boolean isFindUp = false; // 向上查找

	boolean isFindDown = false; // 向下查找

	boolean isFindAll = true; // 全文查找

	public ReplaceDialog(JFrame owner, boolean modal, MyBook txaMain) {
		super(owner,"查找&替换",modal);
		this.txaMain = txaMain;
		this.stbText = new StringBuffer(txaMain.getText());
		this.txtFindText.setBackground(this.txaMain.getBackground());
		this.txtReplaceText.setBackground(txaMain.getBackground());
		this.init();
		this.setMnemonic();
		this.addListeners();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setSize(385, 155);
		this.setLocationRelativeTo(owner);
		this.setResizable(false);
		this.setVisible(true);
	}

	private void init() {
		this.pnlMain.setLayout(null);

		this.radFindDown.addActionListener(this);
		this.radFindUp.addActionListener(this);
		this.circleFind.addActionListener(this);
		this.bgpFindUpDown.add(this.radFindDown);
		this.bgpFindUpDown.add(this.radFindUp);
		this.bgpFindUpDown.add(this.circleFind);
		this.pnlFindUpDown.setBounds(111, 70, 150, 45);
		this.pnlFindUpDown.setBorder(new TitledBorder("方向"));
		this.pnlFindUpDown.add(this.radFindUp);
		this.pnlFindUpDown.add(this.radFindDown);
		this.pnlFindUpDown.add(this.circleFind);
		this.pnlMain.add(this.pnlFindUpDown);

		this.lblFindText.setBounds(10, 10, 60, 20);
		this.txtFindText.setBounds(80, 10, 180, 20);
		this.pnlMain.add(this.lblFindText);
		this.pnlMain.add(this.txtFindText);

		this.lblReplaceText.setBounds(10, 38, 60, 20);
		this.txtReplaceText.setBounds(80, 38, 180, 20);
		this.pnlMain.add(this.lblReplaceText);
		this.pnlMain.add(this.txtReplaceText);

		this.insets.set(0, 0, 0, 0);
		this.chkNotIgnoreCase.setMargin(this.insets);
		this.chkNotIgnoreCase.setBounds(10, 88, 101, 15);
		this.pnlMain.add(this.chkNotIgnoreCase);

		this.btnFind.setEnabled(false);
		this.btnReplace.setEnabled(false);
		this.btnReplaceAll.setEnabled(false);
		this.insets.set(1, 1, 1, 1);
		this.btnFind.setMargin(this.insets);
		this.btnFind.setBounds(270, 10, 95, 23);
		this.btnReplace.setBounds(270, 38, 95, 23);
		this.btnReplaceAll.setMargin(this.insets);
		this.btnReplaceAll.setBounds(270, 66, 95, 23);
		this.btnCancel.setBounds(270, 94, 95, 23);
		this.pnlMain.add(this.btnFind);
		this.pnlMain.add(this.btnReplace);
		this.pnlMain.add(this.btnReplaceAll);
		this.pnlMain.add(this.btnCancel);
	}

	private void setMnemonic() {
		this.chkNotIgnoreCase.setMnemonic('C');
		this.btnFind.setMnemonic('F');
		this.btnReplace.setMnemonic('R');
		this.btnReplaceAll.setMnemonic('A');
	}

	// java记事本-V1.1共分5部分，此为第5部分。
	// 只需将5个部分直接拼接起来，编译运行即可。

	private void addListeners() {
		this.txtFindText.addCaretListener(this);
		this.btnFind.addActionListener(this);
		this.btnReplace.addActionListener(this);
		this.btnReplaceAll.addActionListener(this);
		this.btnCancel.addActionListener(this);
		this.chkNotIgnoreCase.addActionListener(this);

		this.txtFindText.addKeyListener(this.escKeyAdapter);
		this.txtReplaceText.addKeyListener(this.escKeyAdapter);
		this.chkNotIgnoreCase.addKeyListener(this.escKeyAdapter);
		this.btnCancel.addKeyListener(this.escKeyAdapter);
		this.btnReplace.addKeyListener(this.escKeyAdapter);
		this.btnFind.addKeyListener(this.escKeyAdapter);
		this.btnReplaceAll.addKeyListener(this.escKeyAdapter);

	}

	private void cancelFind() {
		this.dispose();
	}

	/**
	 * 开始查找字符串
	 */
	private void findText() {
		String strFindText = this.txtFindText.getText();
		stbText = new StringBuffer(this.txaMain.getText());
		if (strFindText != null && strFindText.length() > 0) {
			stbFindText = new StringBuffer(strFindText);
			int caretPos = txaMain.getCaretPosition();
			if (this.isFindDown) {
				this.findDownText(caretPos);
			} else if (this.isFindUp) {
				this.findUpText(caretPos);
			} else {
				this.findAllText(caretPos);
			}
		}
	}

	/**
	 * 向下查找字符串
	 * 
	 * @param caretPos
	 *            当前光标的所在位置
	 * 
	 */
	private void findDownText(int caretPos) {
		StringBuffer sbTemp = new StringBuffer();
		StringBuffer sbFindTemp = new StringBuffer(stbFindText);
		sbTemp.append(stbText.substring(caretPos, stbText.length()));
		if (this.isIgnoreCase) {
			sbTemp = new StringBuffer(sbTemp.toString().toLowerCase());
			sbFindTemp = new StringBuffer(stbFindText.toString().toLowerCase());
		}
		if (sbTemp != null && sbTemp.length() > 0) {
			int index = sbTemp.indexOf(sbFindTemp.toString());
			if (index >= 0) {
				caretPos += index;
				txaMain.select(caretPos, caretPos + sbFindTemp.length());
				txaMain.setSelectedTextColor(Color.red);
				return;
			}
		}
		JOptionPane.showMessageDialog(this, "找不到\"" + stbFindText + "\"",
				strTitle, JOptionPane.NO_OPTION);
	}

	/**
	 * 向上查找字符串
	 * 
	 * @param caretPos
	 *            当前光标的所在位置
	 */
	private void findUpText(int caretPos) {
		StringBuffer sbTemp = new StringBuffer();
		String strSel = txaMain.getSelectedText();
		if (strSel != null) {
			if (this.isIgnoreCase) {
				if (strSel.toLowerCase().equals(
						stbFindText.toString().toLowerCase())) {
					caretPos -= stbFindText.length();
					if (caretPos < 0) {
						caretPos = 0;
					}
				}
			} else {
				if (strSel.equals(stbFindText.toString())) {
					caretPos -= stbFindText.length();
					if (caretPos < 0) {
						caretPos = 0;
					}
				}
			}
		}
		StringBuffer sbFindTemp = new StringBuffer(stbFindText);
		sbTemp.append(stbText.substring(0, caretPos));
		if (this.isIgnoreCase) {
			sbTemp = new StringBuffer(sbTemp.toString().toLowerCase());
			sbFindTemp = new StringBuffer(stbFindText.toString().toLowerCase());
		}
		if (sbTemp != null && sbTemp.length() > 0) {
			int index = sbTemp.lastIndexOf(sbFindTemp.toString());
			if (index >= 0) {
				caretPos = index;
				txaMain.select(caretPos, caretPos + sbFindTemp.length());
				txaMain.setSelectedTextColor(Color.red);
				return;
			}
		}
		JOptionPane.showMessageDialog(this, "找不到\"" + stbFindText + "\"",
				strTitle, JOptionPane.NO_OPTION);
	}

	/**
	 * 全文查找字符串
	 * 
	 * @param caretPos
	 *            当前光标的所在位置
	 */
	private void findAllText(int caretPos) {
		StringBuffer sbTemp = new StringBuffer();
		StringBuffer sbFindTemp = new StringBuffer(stbFindText);
		if(stbText.indexOf(sbFindTemp.toString())==-1){
			JOptionPane.showMessageDialog(this, "找不到\"" + stbFindText + "\"",
					strTitle, JOptionPane.NO_OPTION);
			return;
		}
		if(stbText.substring(caretPos, stbText.length()).indexOf(sbFindTemp.toString())==-1){
			caretPos=0;
		}
		sbTemp.append(stbText.substring(caretPos, stbText.length()));
		
		if (this.isIgnoreCase) {
			sbTemp = new StringBuffer(sbTemp.toString().toLowerCase());
			sbFindTemp = new StringBuffer(stbFindText.toString().toLowerCase());
		}
		if (sbTemp != null && sbTemp.length() > 0) {
			int index = sbTemp.indexOf(sbFindTemp.toString());
			if (index >= 0) {
				caretPos += index;
				txaMain.select(caretPos, caretPos + sbFindTemp.length());
				txaMain.setSelectedTextColor(Color.red);
				return;
			}
		}
	}

	/**
	 * 替换字符串
	 */
	private void replaceText() {
		String strSel = txaMain.getSelectedText();
		if (strSel != null && strSel.equals(this.txtFindText.getText())) {
			txaMain.replaceSelection(this.txtReplaceText.getText());
			stbFindText = new StringBuffer(this.txtFindText.getText());
		} else {
			this.findText();
		}

	}

	/**
	 * 替换所有字符串
	 */
	private void replaceAllText() {
		String strFindText = this.txtFindText.getText();
		StringBuffer stbFindTextTemp = new StringBuffer(strFindText);
		String strReplaceText = this.txtReplaceText.getText();
		StringBuffer stbTextAll = new StringBuffer(txaMain.getText()); //
		StringBuffer stbTextAllTemp = new StringBuffer(stbTextAll); //
		if (strFindText != null && strFindText.length() > 0) {
			stbFindText = new StringBuffer(strFindText);
			int caretPos = 0;
			int index = 0;
			int times = 0; // 循环次数
			if (this.isIgnoreCase) {
				stbFindTextTemp = new StringBuffer(stbFindTextTemp.toString()
						.toLowerCase());
				stbTextAllTemp = new StringBuffer(stbTextAllTemp.toString()
						.toLowerCase());
			}
			while (caretPos >= 0) {
				index = stbTextAllTemp.indexOf(stbFindTextTemp.toString(),
						caretPos);
				if (index >= 0) {
					stbTextAll.replace(index, index + stbFindTextTemp.length(),
							strReplaceText);
					caretPos = index + strReplaceText.length();
					stbTextAllTemp = new StringBuffer(stbTextAll); //
					if (this.isIgnoreCase) {
						stbTextAllTemp = new StringBuffer(stbTextAllTemp
								.toString().toLowerCase());
					}
				} else {
					break;
				}
				times++;
			}
			if (times > 0) {
				txaMain.setText(stbTextAll.toString());
				txaMain.setCaretPosition(0);
			} else {
				JOptionPane.showMessageDialog(this, "找不到\"" + stbFindText
						+ "\"", strTitle, JOptionPane.NO_OPTION);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (this.btnCancel.equals(e.getSource())) {
			this.cancelFind();
		} else if (this.btnFind.equals(e.getSource())) {
			this.findText();
		} else if (this.btnReplace.equals(e.getSource())) {
			this.replaceText();
		} else if (this.btnReplaceAll.equals(e.getSource())) {
			this.replaceAllText();
		} else if (this.chkNotIgnoreCase.equals(e.getSource())) {
			this.isIgnoreCase = !this.chkNotIgnoreCase.isSelected();
		} else if (this.radFindDown.equals(e.getSource())) {
			this.isFindDown = true;
			this.isFindUp = false;
			this.isFindAll = false;
		} else if (this.radFindUp.equals(e.getSource())) {
			this.isFindDown = false;
			this.isFindUp = true;
			this.isFindAll = false;
		} else if (this.circleFind.equals(e.getSource())){
			this.isFindDown = false;
			this.isFindUp = false;
			this.isFindAll = true;
		}
	}

	public void caretUpdate(CaretEvent e) {
		String strText = this.txtFindText.getText();
		if (strText == null || strText.length() == 0) {
			this.btnFind.setEnabled(false);
			this.btnReplace.setEnabled(false);
			this.btnReplaceAll.setEnabled(false);
		} else {
			this.btnFind.setEnabled(true);
			this.btnReplace.setEnabled(true);
			this.btnReplaceAll.setEnabled(true);
		}
	}

	/**
	 * 当用户按下Esc键时关闭当前对话框的键盘事件类
	 * 
	 * @author chen
	 * 
	 */
	class EscKeyAdapter extends KeyAdapter {
		private JDialog dialog = null;

		public EscKeyAdapter(JDialog dialog) {
			this.dialog = dialog;
		}

		public void keyPressed(KeyEvent e) {
			if (null == this.dialog) {
				return;
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.dialog.dispose();
			}
		}
	}
}