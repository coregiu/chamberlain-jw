package com.eagle.ui.demo;

//java记事本-V1.1共分5部分，此为第1部分。
//只需将5个部分直接拼接起来，编译运行即可。

import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.FontUIResource;
import javax.swing.undo.UndoManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Enumeration;

/**
 * java记事本 前后共用了1月的时间，本想把bug都解决掉的，但是后来发现很难再改进了，可能是水平有限吧！
 * 望见到此代码者，帮忙完善一下，并真诚期待你的宝贵意见与建议！ 百度空间：http://hi.baidu.com/xiboliya
 * 
 * @author chen
 * 
 */
public class NotePad extends JFrame implements ActionListener, CaretListener,
		UndoableEditListener, WindowFocusListener {
	private static final long serialVersionUID = -2865523784463884399L;

	JTextArea txaMain = new JTextArea(); // 用于编辑的文本域

	JMenuBar menuBar = new JMenuBar();

	JMenu menuFile = new JMenu("文件(F)");

	JMenuItem itemNew = new JMenuItem("新建(N)", 'N');

	JMenuItem itemOpen = new JMenuItem("打开(O)...", 'O');

	JMenuItem itemSave = new JMenuItem("保存(S)", 'S');

	JMenuItem itemSaveAs = new JMenuItem("另存为(A)...", 'A');

	JMenuItem itemExit = new JMenuItem("退出(X)", 'X');

	JMenu menuEdit = new JMenu("编辑(E)");

	JMenuItem itemUnDo = new JMenuItem("撤销(U)", 'U');

	JMenuItem itemReDo = new JMenuItem("重做(Y)", 'Y');

	JMenuItem itemCut = new JMenuItem("剪切(T)", 'T');

	JMenuItem itemCopy = new JMenuItem("复制(C)", 'C');

	JMenuItem itemPaste = new JMenuItem("粘贴(P)", 'P');

	JMenuItem itemDel = new JMenuItem("删除(L)", 'L');

	JMenuItem itemFind = new JMenuItem("查找(F)...", 'F');

	JMenuItem itemFindNext = new JMenuItem("查找下一个(N)", 'N');

	JMenuItem itemReplace = new JMenuItem("替换(R)...", 'R');

	JMenuItem itemGoto = new JMenuItem("转到(G)...", 'G');

	JMenuItem itemSelAll = new JMenuItem("全选(A)", 'A');

	JMenuItem itemDateTime = new JMenuItem("时间/日期(D)", 'D');

	JMenu menuStyle = new JMenu("格式(O)");

	JCheckBoxMenuItem itemLineWrap = new JCheckBoxMenuItem("自动换行(W)");

	JMenuItem itemFont = new JMenuItem("字体(F)...", 'F');

	JMenu menuView = new JMenu("查看(V)");

	JCheckBoxMenuItem itemStateBar = new JCheckBoxMenuItem("状态栏(S)");

	JMenu menuHelp = new JMenu("帮助(H)");

	JMenuItem itemHelp = new JMenuItem("帮助主题(H)", 'H');

	JMenuItem itemAbout = new JMenuItem("关于记事本(A)", 'A');

	JScrollPane srp = new JScrollPane(this.txaMain);

	JPopupMenu popMenu = new JPopupMenu();

	JMenuItem itemPopUnDo = new JMenuItem("撤销(U)", 'U');

	JMenuItem itemPopCut = new JMenuItem("剪切(T)", 'T');

	JMenuItem itemPopCopy = new JMenuItem("复制(C)", 'C');

	JMenuItem itemPopPaste = new JMenuItem("粘贴(P)", 'P');

	JMenuItem itemPopDel = new JMenuItem("删除(D)", 'D');

	JMenuItem itemPopSelAll = new JMenuItem("全选(A)", 'A');

	JFileChooser fcrOpen = new OpenFileChooser();

	JFileChooser fcrSave = new SaveFileChooser();

	String strTitle = "java记事本"; // 软件名称

	String strVersion = "V1.1"; // 软件版本号

	StringBuffer stbTitle = new StringBuffer(this.strTitle); // 标题栏字符串

	StringBuffer stbFileName = new StringBuffer(); // 当前编辑的文件名称

	StringBuffer stbFilePath = new StringBuffer(); // 当前编辑的文件路径

	StringBuffer stbText = new StringBuffer(); // 当前文本域中的字符串

	StringBuffer stbFindText = new StringBuffer(); // 查找的字符串

	StringBuffer stbStateAll = new StringBuffer("Chars : "); // 文本域中的总字符数

	StringBuffer stbStateCurLn = new StringBuffer("Ln : "); // 光标所在的当前行数

	StringBuffer stbStateCurCol = new StringBuffer("Col : "); // 光标所在的当前列数

	JPanel pnlState = new JPanel(new GridLayout(1, 2)); // 状态栏面板

	JLabel lblStateAll = new JLabel();

	JLabel lblStateCur = new JLabel();

	boolean isNew = true; // 文件是否已保存，如果未保存则为true

	boolean isChanged = false; // 文本是否已修改，如果已修改则为true

	UndoManager undoManager = new UndoManager(); // 撤销管理器

	Clipboard clip = this.getToolkit().getSystemClipboard(); // 剪贴板

	FontChooser fontChooser = null; // 字体对话框

	FindDialog findDialog = null; // 查找对话框

	ReplaceDialog replaceDialog = null; // 替换对话框

	GotoDialog gotoDialog = null; // 转到对话框

	AboutDialog aboutDialog = null; // 关于对话框

	/**
	 * 构造方法 用于初始化界面和设置
	 */
	private NotePad() {
		this.setTitle(this.stbTitle.toString());
		this.setSize(750, 600);
		this.setLocationRelativeTo(null); // 使窗口居中显示
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 设置默认关闭操作为空，以便添加窗口监听事件
		this.init();
		this.setVisible(true);
	}

	/**
	 * 初始化界面和添加监听器
	 */
	private void init() {
		this.addMenuItem();
		this.addTextArea();
		this.addStatePanel();
		this.addPopMenu();
		this.setMenuMnemonic();
		this.setMenuDefault(true);
		this.addListeners();
		this.addFileFilter();
	}

	/**
	 * 添加各组件的事件监听器
	 */
	private void addListeners() {
		this.txaMain.addCaretListener(this);
		this.txaMain.getDocument().addUndoableEditListener(this);

		this.itemAbout.addActionListener(this);
		this.itemCopy.addActionListener(this);
		this.itemCut.addActionListener(this);
		this.itemDateTime.addActionListener(this);
		this.itemDel.addActionListener(this);
		this.itemExit.addActionListener(this);
		this.itemFind.addActionListener(this);
		this.itemFindNext.addActionListener(this);
		this.itemFont.addActionListener(this);
		this.itemGoto.addActionListener(this);
		this.itemHelp.addActionListener(this);
		this.itemLineWrap.addActionListener(this);
		this.itemNew.addActionListener(this);
		this.itemOpen.addActionListener(this);
		this.itemPaste.addActionListener(this);
		this.itemPopCopy.addActionListener(this);
		this.itemPopCut.addActionListener(this);
		this.itemPopDel.addActionListener(this);
		this.itemPopPaste.addActionListener(this);
		this.itemPopSelAll.addActionListener(this);
		this.itemPopUnDo.addActionListener(this);
		this.itemReDo.addActionListener(this);
		this.itemReplace.addActionListener(this);
		this.itemSave.addActionListener(this);
		this.itemSaveAs.addActionListener(this);
		this.itemSelAll.addActionListener(this);
		this.itemStateBar.addActionListener(this);
		this.itemUnDo.addActionListener(this);
		// 为窗口添加事件监听器
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		// 为窗口添加焦点监听器
		this.addWindowFocusListener(this);
		// 为文本域添加鼠标事件监听器
		this.txaMain.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getComponent().equals(txaMain)) {
					if (e.getButton() == MouseEvent.BUTTON3) { // 点击右键时，显示快捷菜单
						popMenu.show(txaMain, e.getX(), e.getY());
					}
				}
			}
		});
		// 屏蔽JTextArea组件的默认热键：Ctrl+C、Ctrl+H、Ctrl+V、Ctrl+X
		InputMap inputMap = this.txaMain.getInputMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				KeyEvent.CTRL_DOWN_MASK), "CTRL_C");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				KeyEvent.CTRL_DOWN_MASK), "CTRL_H");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				KeyEvent.CTRL_DOWN_MASK), "CTRL_V");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				KeyEvent.CTRL_DOWN_MASK), "CTRL_X");
	}

	/**
	 * 主面板上添加文本域
	 */
	private void addTextArea() {
		this.getContentPane().add(this.srp, BorderLayout.CENTER); // 为使文本域有滚动条，必须在主面板上添加
		this.txaMain.setTabSize(4); // 为了美观，设置"Tab"键占用4个空格（默认占用8个空格）
		this.txaMain.setFont(new Font("宋体", Font.PLAIN, 15)); // 设置文本域的字体，大小为15。如用默认的12，则太小
	}

	/**
	 * 主面板上添加状态栏
	 */
	private void addStatePanel() {
		this.pnlState.setBorder(new EtchedBorder());
		this.stbStateAll.append(0);
		this.stbStateCurLn.append(1);
		this.stbStateCurCol.append(1);
		this.lblStateAll.setText(this.stbStateAll.toString());
		this.lblStateCur.setText(this.stbStateCurLn + " , "
				+ this.stbStateCurCol);
		this.lblStateAll.setHorizontalAlignment(JLabel.CENTER);
		this.pnlState.add(this.lblStateAll);
		this.pnlState.add(this.lblStateCur);
		this.getContentPane().add(this.pnlState, BorderLayout.SOUTH);
	}

	/**
	 * 主面板上添加菜单栏
	 */
	private void addMenuItem() {
		this.setJMenuBar(this.menuBar);
		this.menuBar.add(this.menuFile);
		this.menuFile.add(this.itemNew);
		this.menuFile.add(this.itemOpen);
		this.menuFile.add(this.itemSave);
		this.menuFile.add(this.itemSaveAs);
		this.menuFile.addSeparator();
		this.menuFile.add(this.itemExit);

		this.menuBar.add(this.menuEdit);
		this.menuEdit.add(this.itemUnDo);
		this.menuEdit.add(this.itemReDo);
		this.menuEdit.addSeparator();
		this.menuEdit.add(this.itemCut);
		this.menuEdit.add(this.itemCopy);
		this.menuEdit.add(this.itemPaste);
		this.menuEdit.add(this.itemDel);
		this.menuEdit.addSeparator();
		this.menuEdit.add(this.itemFind);
		this.menuEdit.add(this.itemFindNext);
		this.menuEdit.add(this.itemReplace);
		this.menuEdit.add(this.itemGoto);
		this.menuEdit.addSeparator();
		this.menuEdit.add(this.itemSelAll);
		this.menuEdit.add(this.itemDateTime);

		this.menuBar.add(this.menuStyle);
		this.menuStyle.add(this.itemLineWrap);
		this.menuStyle.add(this.itemFont);

		this.menuBar.add(this.menuView);
		this.menuView.add(this.itemStateBar);

		this.menuBar.add(this.menuHelp);
		this.menuHelp.add(this.itemHelp);
		this.menuHelp.addSeparator();
		this.menuHelp.add(this.itemAbout);
	}

	/**
	 * 初始化快捷菜单
	 */
	private void addPopMenu() {
		this.popMenu.add(this.itemPopUnDo);
		this.popMenu.addSeparator();
		this.popMenu.add(this.itemPopCut);
		this.popMenu.add(this.itemPopCopy);
		this.popMenu.add(this.itemPopPaste);
		this.popMenu.add(this.itemPopDel);
		this.popMenu.addSeparator();
		this.popMenu.add(this.itemPopSelAll);

		Dimension popSize = this.popMenu.getPreferredSize();
		popSize.width += popSize.width / 5; // 为了美观，适当加宽菜单的显示
		this.popMenu.setPopupSize(popSize);

	}

	/**
	 * 设置各菜单项的初始状态，即是否可用
	 * 
	 * @param isInit
	 *            是否用于界面初始化
	 */
	private void setMenuDefault(boolean isInit) {
		this.itemUnDo.setEnabled(false);
		this.itemReDo.setEnabled(false);
		this.itemCut.setEnabled(false);
		this.itemCopy.setEnabled(false);
		this.itemDel.setEnabled(false);
		this.itemFind.setEnabled(false);
		this.itemFindNext.setEnabled(false);
		this.itemReplace.setEnabled(false);
		this.itemGoto.setEnabled(false);
		this.itemSelAll.setEnabled(false);
		this.itemPopCopy.setEnabled(false);
		this.itemPopCut.setEnabled(false);
		this.itemPopDel.setEnabled(false);
		this.itemPopSelAll.setEnabled(false);
		this.itemPopUnDo.setEnabled(false);
		if (isInit) {
			this.itemLineWrap.setSelected(true);
			this.itemStateBar.setSelected(true);
			this.setLineWarp();
			this.setStateBar();
		}
	}

	/**
	 * 为文件选择器添加txt文件的文件过滤器
	 */
	private void addFileFilter() {
		TXTFilter txtFilter = new TXTFilter();
		this.fcrOpen.addChoosableFileFilter(txtFilter);
		this.fcrSave.addChoosableFileFilter(txtFilter);
	}

	/**
	 * 根据文本域中的字符是否为空，设置相关菜单的状态
	 * 
	 * @param isNotNull
	 *            文本域中是否有字符
	 */
	private void setMenuStateByTextArea(boolean isNotNull) {
		this.itemSelAll.setEnabled(isNotNull);
		this.itemPopSelAll.setEnabled(isNotNull);
		this.itemFind.setEnabled(isNotNull);
		this.itemFindNext.setEnabled(isNotNull);
		this.itemReplace.setEnabled(isNotNull);
		this.itemGoto.setEnabled(isNotNull);
	}

	/**
	 * 根据文本域中选择的字符串是否为空，设置相关菜单的状态
	 * 
	 * @param isNull
	 *            选择是否为空
	 */
	private void setMenuStateBySelectedText(boolean isNull) {
		this.itemCopy.setEnabled(isNull);
		this.itemCut.setEnabled(isNull);
		this.itemDel.setEnabled(isNull);
		this.itemPopCopy.setEnabled(isNull);
		this.itemPopCut.setEnabled(isNull);
		this.itemPopDel.setEnabled(isNull);
	}

	/**
	 * 设置撤销与重做菜单项的状态
	 */
	private void setMenuStateUndoRedo() {
		boolean canRedo = this.undoManager.canRedo();
		boolean canUndo = this.undoManager.canUndo();
		this.itemReDo.setEnabled(canRedo);
		this.itemUnDo.setEnabled(canUndo);
		this.itemPopUnDo.setEnabled(canUndo);
	}

	/**
	 * 为各菜单项设置助记符和快捷键
	 */
	private void setMenuMnemonic() {
		this.menuFile.setMnemonic('F');
		this.menuHelp.setMnemonic('H');
		this.menuEdit.setMnemonic('E');
		this.menuStyle.setMnemonic('O');
		this.menuView.setMnemonic('V');
		this.itemLineWrap.setMnemonic('W');
		this.itemStateBar.setMnemonic('S');
		this.itemNew.setAccelerator(KeyStroke.getKeyStroke('N',
				InputEvent.CTRL_DOWN_MASK));
		this.itemOpen.setAccelerator(KeyStroke.getKeyStroke('O',
				InputEvent.CTRL_DOWN_MASK));
		this.itemSave.setAccelerator(KeyStroke.getKeyStroke('S',
				InputEvent.CTRL_DOWN_MASK));
		this.itemExit.setAccelerator(KeyStroke.getKeyStroke('Q',
				InputEvent.CTRL_DOWN_MASK));
		this.itemAbout
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		this.itemUnDo.setAccelerator(KeyStroke.getKeyStroke('Z',
				InputEvent.CTRL_DOWN_MASK));
		this.itemReDo.setAccelerator(KeyStroke.getKeyStroke('Y',
				InputEvent.CTRL_DOWN_MASK));
		this.itemCut.setAccelerator(KeyStroke.getKeyStroke('X',
				InputEvent.CTRL_DOWN_MASK));
		this.itemCopy.setAccelerator(KeyStroke.getKeyStroke('C',
				InputEvent.CTRL_DOWN_MASK));
		this.itemPaste.setAccelerator(KeyStroke.getKeyStroke('V',
				InputEvent.CTRL_DOWN_MASK));
		this.itemDel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
				0));
		this.itemFind.setAccelerator(KeyStroke.getKeyStroke('F',
				InputEvent.CTRL_DOWN_MASK));
		this.itemFindNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,
				0));
		this.itemReplace.setAccelerator(KeyStroke.getKeyStroke('H',
				InputEvent.CTRL_DOWN_MASK));
		this.itemGoto.setAccelerator(KeyStroke.getKeyStroke('G',
				InputEvent.CTRL_DOWN_MASK));
		this.itemSelAll.setAccelerator(KeyStroke.getKeyStroke('A',
				InputEvent.CTRL_DOWN_MASK));
		this.itemDateTime.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,
				0));

	}

	/**
	 * 修改整个界面的默认字体
	 */
	private static void setDefaultFont() {
		FontUIResource fontRes = new FontUIResource(new Font("宋体", Font.PLAIN,
				12));
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}

	}

	/**
	 * 为各菜单项添加事件的处理方法
	 */
	public void actionPerformed(ActionEvent e) {
		if (this.itemAbout.equals(e.getSource())) {
			this.showAbout();
		} else if (this.itemCopy.equals(e.getSource())) {
			this.copyText();
		} else if (this.itemCut.equals(e.getSource())) {
			this.cutText();
		} else if (this.itemDateTime.equals(e.getSource())) {
			this.insertDateTime();
		} else if (this.itemDel.equals(e.getSource())) {
			this.deleteText();
		} else if (this.itemExit.equals(e.getSource())) {
			this.exit();
		} else if (this.itemFind.equals(e.getSource())) {
			this.openFindDialog();
		} else if (this.itemFindNext.equals(e.getSource())) {
			this.findNextText();
		} else if (this.itemFont.equals(e.getSource())) {
			this.openFontChooser();
		} else if (this.itemGoto.equals(e.getSource())) {
			this.openGotoDialog();
		} else if (this.itemHelp.equals(e.getSource())) {

		} else if (this.itemLineWrap.equals(e.getSource())) {
			this.setLineWarp();
		} else if (this.itemNew.equals(e.getSource())) {
			this.createNew();
		} else if (this.itemOpen.equals(e.getSource())) {
			this.openFile();
		} else if (this.itemPaste.equals(e.getSource())) {
			this.pasteText();
		} else if (this.itemPopCopy.equals(e.getSource())) {
			this.copyText();
		} else if (this.itemPopCut.equals(e.getSource())) {
			this.cutText();
		} else if (this.itemPopDel.equals(e.getSource())) {
			this.deleteText();
		} else if (this.itemPopPaste.equals(e.getSource())) {
			this.pasteText();
		} else if (this.itemPopSelAll.equals(e.getSource())) {
			this.selectAll();
		} else if (this.itemPopUnDo.equals(e.getSource())) {
			this.undoAction();
		} else if (this.itemReDo.equals(e.getSource())) {
			this.redoAction();
		} else if (this.itemReplace.equals(e.getSource())) {
			this.openReplaceDialog();
		} else if (this.itemSave.equals(e.getSource())) {
			this.fcrSave.setDialogTitle("保存");
			this.saveFile(false);
		} else if (this.itemSaveAs.equals(e.getSource())) {
			this.saveAsFile();
		} else if (this.itemSelAll.equals(e.getSource())) {
			this.selectAll();
		} else if (this.itemStateBar.equals(e.getSource())) {
			this.setStateBar();
		} else if (this.itemUnDo.equals(e.getSource())) {
			this.undoAction();
		}

	}

	/**
	 * "撤销"的处理方法
	 */
	private void undoAction() {
		boolean canUndo = this.undoManager.canUndo(); // 判断是否可以撤销
		if (canUndo) {
			this.undoManager.undo(); // 执行撤销操作
		}
		this.setMenuStateUndoRedo(); // 设置撤销和重做菜单的状态
	}

	/**
	 * "重做"的处理方法
	 */
	private void redoAction() {
		boolean canRedo = this.undoManager.canRedo(); // 判断是否可以重做
		if (canRedo) {
			this.undoManager.redo(); // 执行重做操作
		}
		this.setMenuStateUndoRedo(); // 设置撤销和重做菜单的状态
	}

	/**
	 * "时间/日期"的处理方法
	 */
	private void insertDateTime() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1; // 获取当前月份，由于返回值从0开始，故应加1
		int date = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		this.txaMain.replaceSelection("" + hour + ":" + minute + ":" + second
				+ " " + year + "-" + month + "-" + date);
	}

	/**
	 * "退出"的处理方法
	 */
	private void exit() {
		if (saveFileBeforeAct()) { // 关闭程序前检测文件是否已修改
			System.exit(0);
		}
	}

	/**
	 * "关于"的处理方法
	 */
	private void showAbout() {
		if (this.aboutDialog == null) {
			this.aboutDialog = new AboutDialog(this, true);
		} else {
			this.aboutDialog.setLocationRelativeTo(this);
			this.aboutDialog.setVisible(true);
		}
	}

	/**
	 * "自动换行"的处理方法
	 */
	private void setLineWarp() {
		this.txaMain.setLineWrap(this.itemLineWrap.isSelected());
	}

	/**
	 * 打开的文件内容已修改，当执行新建、关闭操作时，弹出对话框，让用户选择相应的操作
	 * 
	 * @return 用户选择了是或否时返回true，选择取消或关闭时返回false
	 */
	private boolean saveFileBeforeAct() {
		if (this.stbFileName.length() > 0 && this.isChanged) {
			int result = JOptionPane.showConfirmDialog(this, "文件："
					+ this.stbFileName + "的内容已经修改。\n想保存文件吗？", this.strTitle,
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				this.saveFile(false);
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		return true;
	}

	/**
	 * "新建"的处理方法
	 */
	private void createNew() {
		if (!this.saveFileBeforeAct()) {
			return;
		}
		this.txaMain.setText("");
		this.setFileNameAndPath(null);
		this.isNew = true;
		this.isChanged = false;
		this.setMenuDefault(false); // 恢复菜单的初始状态
		this.undoManager.discardAllEdits(); // 清空撤销管理器
		this.stbText = new StringBuffer();
	}

	/**
	 * "删除"的处理方法
	 */
	private void deleteText() {
		this.txaMain.replaceSelection("");
	}

	/**
	 * "复制"的处理方法
	 */
	private void copyText() {
		String selText = this.txaMain.getSelectedText();
		if (selText != null && selText.length() > 0) {
			StringSelection ss = new StringSelection(selText);
			this.clip.setContents(ss, ss); // 将选中的文本复制到剪贴板
			this.itemPaste.setEnabled(true);
			this.itemPopPaste.setEnabled(true);
		}
	}

	/**
	 * "剪切"的处理方法
	 */
	private void cutText() {
		this.copyText(); // 复制选中文本
		this.deleteText(); // 删除选中文本
	}

	/**
	 * "粘贴"的处理方法
	 */
	private void pasteText() {
		try {
			Transferable tf = this.clip.getContents(this);
			if (tf != null) {
				String str = tf.getTransferData(DataFlavor.stringFlavor)
						.toString(); // 如果剪贴板内的内容不是文本，则将抛出异常
				if (str != null) {
					this.deleteText();
					this.txaMain.insert(str, this.txaMain.getCaretPosition());
				}
			}
		} catch (Exception x) {
			// 剪贴板异常
			x.printStackTrace();
		}
	}

	/**
	 * "全选"的处理方法
	 */
	private void selectAll() {
		this.txaMain.selectAll();
	}

	/**
	 * "字体"的处理方法
	 */
	private void openFontChooser() {
		if (this.fontChooser == null) {
			this.fontChooser = new FontChooser(this, true);
		} else {
			this.fontChooser.updateListView();
			this.fontChooser.setFontView();
			this.fontChooser.setStyleView();
			this.fontChooser.setSizeView();
			this.fontChooser.setLocationRelativeTo(this);
			this.fontChooser.setVisible(true);
		}
	}

	/**
	 * 是否已经打开了查找或替换对话框
	 * 
	 * @return 如果已经打开了查找或替换对话框则返回false
	 */
	private boolean canOpenDialog() {
		if (this.findDialog != null && this.findDialog.isVisible()) {
			return false;
		}
		if (this.replaceDialog != null && this.replaceDialog.isVisible()) {
			return false;
		}
		return true;
	}

	/**
	 * "查找"的处理方法
	 */
	private void openFindDialog() {
		if (!this.canOpenDialog()) {
			return;
		}
		if (this.findDialog == null) {
			this.findDialog = new FindDialog(this, false);
		} else {
			this.findDialog.setLocationRelativeTo(this);
			this.findDialog.setVisible(true);
		}
		this.findDialog.txtFindText.setText(this.stbFindText.toString());
		this.findDialog.txtFindText.selectAll();
	}

	/**
	 * "查找下一个"的处理方法
	 */
	private void findNextText() {
		if (!this.canOpenDialog()) {
			return;
		}
		if (this.findDialog == null || this.stbFindText.length() == 0) {
			this.findDialog = new FindDialog(this, false);
		} else {
			this.findDialog.findText();
		}
		this.findDialog.txtFindText.setText(this.stbFindText.toString());
		this.findDialog.txtFindText.selectAll();
	}

	/**
	 * "替换"的处理方法
	 */
	private void openReplaceDialog() {
		if (!this.canOpenDialog()) {
			return;
		}
		if (this.replaceDialog == null) {
			this.replaceDialog = new ReplaceDialog(this, false);
		} else {
			this.replaceDialog.setLocationRelativeTo(this);
			this.replaceDialog.setVisible(true);
		}
		this.replaceDialog.txtFindText.setText(this.stbFindText.toString());
		this.replaceDialog.txtFindText.selectAll();
		this.replaceDialog.txtReplaceText.setText("");
	}

	/**
	 * "转到"的处理方法
	 */
	private void openGotoDialog() {
		if (this.gotoDialog == null) {
			this.gotoDialog = new GotoDialog(this, true);
		} else {
			this.gotoDialog.setLocationRelativeTo(this);
			this.gotoDialog.setVisible(true);
		}
	}

	/**
	 * "状态栏"的处理方法
	 */
	private void setStateBar() {
		if (this.itemStateBar.isSelected()) {
			this.pnlState.setVisible(true);
			this.lblStateAll.setVisible(true);
			this.lblStateCur.setVisible(true);
		} else {
			this.pnlState.setVisible(false);
			this.lblStateAll.setVisible(false);
			this.lblStateCur.setVisible(false);
		}
	}

	/**
	 * "打开"的处理方法
	 */
	private void openFile() {
		if (!this.saveFileBeforeAct()) {
			return;
		}
		this.fcrOpen.setSelectedFile(null);
		if (JFileChooser.APPROVE_OPTION != this.fcrOpen.showOpenDialog(this)) {
			return;
		}
		File file = this.fcrOpen.getSelectedFile();
		if (null != file) {
			if (file.exists()) {
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(file);
					br = new BufferedReader(fr);
					String line = br.readLine();
					StringBuffer stbTemp = new StringBuffer();
					while (line != null) {
						stbTemp.append(line);
						line = br.readLine();
						if (line != null) {
							stbTemp.append("\n");
						}
					}
					this.txaMain.setText(stbTemp.toString());
					br.close();
					fr.close();
				} catch (Exception x) {
					x.printStackTrace();
				}
				this.txaMain.setCaretPosition(0); // 将插入点设置为文本开头
				this.isChanged = false;
				this.isNew = false;
				this.setFileNameAndPath(file);
				this.undoManager.discardAllEdits();
				this.setMenuStateUndoRedo(); // 设置撤销和重做菜单的状态
			}
		}
	}

	/**
	 * "保存"的处理方法
	 * 
	 * @param isSaveAs
	 *            是否为"另存为"
	 */
	private void saveFile(boolean isSaveAs) {
		if (isSaveAs || this.isNew) {
			this.fcrSave.setSelectedFile(null);
			if (JFileChooser.APPROVE_OPTION != this.fcrSave
					.showSaveDialog(this)) {
				return;
			}
			File file = this.fcrSave.getSelectedFile();
			if (null != file) {
				String ext = ".txt";
				String strFile = this.fcrSave.getSelectedFile()
						.getAbsolutePath();
				if (!strFile.toLowerCase().endsWith(ext)) {
					strFile += ext;
					file = new File(strFile);
				}
				this.toSaveFile(file);
				this.setFileNameAndPath(file);
			} else {
				return;
			}
		} else {
			File file = new File(this.stbFilePath.toString());
			if (null != file) {
				this.toSaveFile(file);
			} else {
				return;
			}
		}
		this.isChanged = false;
		if (isSaveAs) {
			this.isNew = true;
		} else {
			this.isNew = false;
		}
	}

	/**
	 * "另存为"的处理方法
	 */
	private void saveAsFile() {
		this.fcrSave.setDialogTitle("另存为");
		this.saveFile(true);
	}

	/**
	 * 将文本域中的文本保存到文件
	 * 
	 * @param file
	 *            保存的文件
	 */
	private void toSaveFile(File file) {
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			String[] arrStr = this.txaMain.getText().split("\n");
			if (arrStr.length > 1) {
				for (int i = 0; i < arrStr.length; i++) {
					bw.write(arrStr[i]);
					bw.newLine();
				}
			} else {
				bw.write(this.txaMain.getText());
			}
			bw.flush();
			fw.flush();
			bw.close();
			fw.close();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	/**
	 * 设置文件的名称和路径
	 * 
	 * @param file
	 *            当前编辑的文件
	 */
	private void setFileNameAndPath(File file) {
		this.stbFileName = new StringBuffer();
		this.stbFilePath = new StringBuffer();
		this.stbTitle = new StringBuffer(this.strTitle);
		if (null != file && file.exists()) {
			this.stbFileName.append(file.getName());
			this.stbFilePath.append(file.getAbsolutePath());
			this.stbTitle.insert(0, " - ");
			this.stbTitle.insert(0, this.stbFilePath);
		}
		this.setTitle(this.stbTitle.toString());
	}

	/**
	 * 更新状态栏的文本总字数
	 */
	private void updateStateAll() {
		int total = this.stbText.length();
		this.stbStateAll = new StringBuffer("Chars : ");
		this.stbStateAll.append(total);
		this.lblStateAll.setText(this.stbStateAll.toString());
	}

	/**
	 * 更新状态栏的当前光标所在的行和列
	 */
	private void updateStateCur() {
		int caretPos = txaMain.getCaretPosition();
		StringBuffer sbTemp = new StringBuffer(this.stbText.substring(0,
				caretPos));
		this.stbStateCurLn = new StringBuffer("Ln : ");
		this.stbStateCurCol = new StringBuffer("Col : ");
		int curLn = 1;
		int curCol = 1;
		for (int n = 0; n < sbTemp.length(); n++) {
			if ('\n' == sbTemp.charAt(n)) {
				curLn++;
			}
		}
		int curLine = sbTemp.lastIndexOf("\n");
		if (curLine >= 0) {
			curCol = sbTemp.length() - curLine;
		} else {
			curCol = caretPos + 1;
		}
		this.stbStateCurLn.append(curLn);
		this.stbStateCurCol.append(curCol);
		this.lblStateCur.setText(this.stbStateCurLn + " , "
				+ this.stbStateCurCol);
	}

	/**
	 * 当文本域中的光标变化时，将触发此事件
	 */
	public void caretUpdate(CaretEvent e) {
		String strText = this.txaMain.getText();
		this.stbText = new StringBuffer(strText);
		this.updateStateCur();
		String selText = this.txaMain.getSelectedText();
		if (selText != null && selText.length() > 0) {
			this.setMenuStateBySelectedText(true);
		} else {
			this.setMenuStateBySelectedText(false);
		}
	}

	/**
	 * 当文本域中的文本发生变化时，将触发此事件
	 */

	public void undoableEditHappened(UndoableEditEvent e) {
		this.undoManager.addEdit(e.getEdit());
		this.setMenuStateUndoRedo(); // 设置撤销和重做菜单的状态
		if (this.stbText == null || this.stbText.length() == 0) {
			this.setMenuStateByTextArea(false);
		} else {
			this.setMenuStateByTextArea(true);
		}
		this.isChanged = true;
		this.updateStateAll();
	}

	// java记事本-V1.1共分5部分，此为第3部分。
	// 只需将5个部分直接拼接起来，编译运行即可。

	/**
	 * 当主窗口获得焦点时，将触发此事件
	 */
	public void windowGainedFocus(WindowEvent e) {
		try {
			Transferable tf = this.clip.getContents(this);
			if (null == tf) {
				this.itemPaste.setEnabled(false);
				this.itemPopPaste.setEnabled(false);
			}
			if (tf != null) {
				String str = tf.getTransferData(DataFlavor.stringFlavor)
						.toString(); // 如果剪贴板内的内容不是文本，则将抛出异常
				if (null != str && str.length() > 0) {
					this.itemPaste.setEnabled(true);
					this.itemPopPaste.setEnabled(true);
				}
			}
		} catch (Exception x) {
			// 剪贴板异常
			x.printStackTrace();
			this.itemPaste.setEnabled(false);
			this.itemPopPaste.setEnabled(false);
		}
	}

	/**
	 * 当主窗口失去焦点时，将触发此事件
	 */
	public void windowLostFocus(WindowEvent e) {

	}

	/**
	 * 程序的总入口
	 * 
	 * @param args
	 *            命令行参数
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception x) {
			x.printStackTrace();
		}
		setDefaultFont();
		new NotePad();
	}

	/**
	 * "打开"文件选择器
	 * 
	 * @author chen
	 * 
	 */
	private class OpenFileChooser extends JFileChooser {
		private static final long serialVersionUID = 8821683749700749035L;

		/**
		 * 当用户确认时将调用此方法
		 */
		public void approveSelection() {
			File file = this.getSelectedFile();
			if (null != file && file.exists()) { // 当用户未选择文件或选择的文件不存在时，将弹出提示框
				super.approveSelection();
			} else {
				JOptionPane.showMessageDialog(this, "文件不存在，请重新选择！", strTitle,
						JOptionPane.CANCEL_OPTION);
			}
		}
	}

	/**
	 * "保存"文件选择器
	 * 
	 * @author chen
	 * 
	 */
	private class SaveFileChooser extends JFileChooser {
		private static final long serialVersionUID = 4786643594126348299L;

		/**
		 * 当用户确认时将调用此方法
		 */
		public void approveSelection() {
			File file = this.getSelectedFile();
			if (null != file && file.exists()) { // 当用户选择的文件已经存在时，将弹出提示框
				int result = JOptionPane.showConfirmDialog(this, file
						+ " 已存在。\n是否覆盖？", "保存", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (JOptionPane.YES_OPTION == result) { // 用户选择覆盖
					super.approveSelection();
				}
			} else
				super.approveSelection();
		}
	}

	/**
	 * "关于"对话框
	 * 
	 * @author chen
	 * 
	 */
	private class AboutDialog extends JDialog implements ActionListener {
		private static final long serialVersionUID = 8999530329803047571L;

		JLabel lblSoftWare = new JLabel("软件：" + strTitle);

		JLabel lblVersion = new JLabel("版本：" + strVersion);

		JLabel lblAuthor = new JLabel("作者：冰原");

		JLabel lblBlog = new JLabel(
				"<html>博客：<a href='http://hi.baidu.com/xiboliya'>http://hi.baidu.com/xiboliya</a></html>");

		JLabel lblCopyright = new JLabel("版权：此为自由软件可以任意引用或修改");

		JLabel lblLeft = new JLabel(" ");

		JLabel lblRight = new JLabel(" ");

		JButton btnOk = new JButton("确定");

		JPanel pnlContent = new JPanel(new GridLayout(5, 1));

		JPanel pnlButton = new JPanel();

		JPanel pnlLeft = new JPanel();

		JPanel pnlRight = new JPanel();

		EscKeyAdapter escKeyAdapter = new EscKeyAdapter(this);

		/**
		 * 构造方法
		 */
		public AboutDialog(JFrame owner, boolean modal) {
			super(owner, modal);
			this.setTitle("关于");
			this.init();
			this.addListeners();
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setSize(260, 150);
			this.setLocationRelativeTo(owner);
			this.setResizable(false);
			this.setVisible(true);
		}

		/**
		 * 界面初始化
		 */
		private void init() {
			this.pnlContent.add(this.lblSoftWare);
			this.pnlContent.add(this.lblVersion);
			this.pnlContent.add(this.lblAuthor);
			this.pnlContent.add(this.lblBlog);
			this.pnlContent.add(this.lblCopyright);
			this.getContentPane().add(this.pnlContent, BorderLayout.CENTER);
			this.pnlButton.add(this.btnOk);
			this.getContentPane().add(this.pnlButton, BorderLayout.SOUTH);
			this.pnlLeft.add(this.lblLeft);
			this.getContentPane().add(this.pnlLeft, BorderLayout.WEST);
			this.pnlRight.add(this.lblRight);
			this.getContentPane().add(this.pnlRight, BorderLayout.EAST);
			this.lblBlog.setCursor(Cursor
					.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		/**
		 * 添加事件监听器
		 */
		private void addListeners() {
			this.btnOk.addActionListener(this);
			this.lblBlog.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					try {
						// 当用户用鼠标点击此标签时，将调用系统命令，打开网页
						Runtime.getRuntime().exec(
								"cmd /c start http://hi.baidu.com/xiboliya");
					} catch (Exception x) {
						// 如果操作系统不支持此命令，将抛出异常
						x.printStackTrace();
					}
				}
			});
			this.btnOk.addKeyListener(this.escKeyAdapter);
		}

		/**
		 * 为组件添加事件的处理方法
		 */
		public void actionPerformed(ActionEvent e) {
			if (this.btnOk.equals(e.getSource())) {
				this.dispose();
			}
		}
	}

	/**
	 * "txt"文件过滤器
	 * 
	 * @author chen
	 * 
	 */
	private class TXTFilter extends FileFilter {
		public boolean accept(File f) {
			if (f.getName().toLowerCase().endsWith(".txt") || f.isDirectory()) {
				return true; // 只显示目录和扩展名为txt的文件
			} else {
				return false;
			}
		}

		public String getDescription() {
			return "文本文档(*.txt)"; // 返回文件选择器中的显示文字
		}
	}

	/**
	 * "字体"对话框
	 * 
	 * @author chen
	 * 
	 */
	private class FontChooser extends JDialog implements ActionListener,
			ListSelectionListener, CaretListener {
		private static final long serialVersionUID = 848425588319643354L;

		JLabel lblFont = new JLabel("字体：");

		JLabel lblStyle = new JLabel("字形：");

		JLabel lblSize = new JLabel("大小：");

		JLabel lblView = new JLabel(strTitle);

		JTextField txtFont = new JTextField();

		JTextField txtStyle = new JTextField();

		JTextField txtSize = new JTextField();

		JList listFont = new JList();

		JList listStyle = new JList();

		JList listSize = new JList();

		JScrollPane srpFont = new JScrollPane(this.listFont);

		JScrollPane srpStyle = new JScrollPane(this.listStyle);

		JScrollPane srpSize = new JScrollPane(this.listSize);

		JButton btnOk = new JButton("确定");

		JButton btnCancel = new JButton("取消");

		JPanel pnlMain = (JPanel) this.getContentPane();

		EscKeyAdapter escKeyAdapter = new EscKeyAdapter(this);

		/**
		 * 构造方法
		 * 
		 * @param owner
		 *            父窗口
		 * @param modal
		 *            是否为模式窗口
		 */
		public FontChooser(JFrame owner, boolean modal) {
			super(owner, modal);
			this.setTitle("字体");
			this.init();
			this.fillFontList();
			this.fillStyleList();
			this.fillSizeList();
			this.setView(false);
			this.addListeners();
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setSize(365, 300);
			this.setLocationRelativeTo(owner);
			this.setResizable(false);
			this.setVisible(true);
		}

		/**
		 * 初始化界面
		 */
		private void init() {
			this.txtFont.setBackground(txaMain.getBackground());
			this.txtStyle.setBackground(txaMain.getBackground());
			this.txtSize.setBackground(txaMain.getBackground());
			this.lblView.setBorder(new TitledBorder("示例"));
			this.lblView.setHorizontalAlignment(JLabel.CENTER);

			this.pnlMain.setLayout(null);

			this.lblFont.setBounds(10, 10, 130, 20);
			this.txtFont.setBounds(10, 31, 130, 20);
			this.srpFont.setBounds(10, 52, 130, 120);
			this.pnlMain.add(this.lblFont);
			this.pnlMain.add(this.txtFont);
			this.pnlMain.add(this.srpFont);

			this.lblStyle.setBounds(150, 10, 100, 20);
			this.txtStyle.setBounds(150, 31, 100, 20);
			this.srpStyle.setBounds(150, 52, 100, 120);
			this.pnlMain.add(this.lblStyle);
			this.pnlMain.add(this.txtStyle);
			this.pnlMain.add(this.srpStyle);

			this.lblSize.setBounds(260, 10, 80, 20);
			this.txtSize.setBounds(260, 31, 80, 20);
			this.srpSize.setBounds(260, 52, 80, 120);
			this.pnlMain.add(this.lblSize);
			this.pnlMain.add(this.txtSize);
			this.pnlMain.add(this.srpSize);

			this.lblView.setBounds(10, 180, 240, 80);
			this.pnlMain.add(this.lblView);

			this.btnOk.setBounds(260, 190, 80, 23);
			this.btnCancel.setBounds(260, 230, 80, 23);
			this.pnlMain.add(this.btnOk);
			this.pnlMain.add(this.btnCancel);

		}

		/**
		 * 填充"字体"列表
		 */
		private void fillFontList() {
			// 获取系统所有字体的名称列表
			String[] fontFamilys = java.awt.GraphicsEnvironment
					.getLocalGraphicsEnvironment()
					.getAvailableFontFamilyNames();
			this.listFont.setListData(fontFamilys);
			Font font = txaMain.getFont();
			this.listFont.setSelectedValue(font.getFamily(), true);
			this.listFont.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.setFontView();
		}

		/**
		 * 填充"字形"列表
		 */
		private void fillStyleList() {
			String[] fontStyles = new String[] { "常规", "粗体", "斜体", "粗斜体" };
			this.listStyle.setListData(fontStyles);
			Font font = txaMain.getFont();
			this.listStyle.setSelectedIndex(font.getStyle());
			this.listStyle
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.setStyleView();
		}

		/**
		 * 填充"大小"列表
		 */
		private void fillSizeList() {
			int[] fontSizesTemp = new int[] { 8, 9, 10, 11, 12, 13, 14, 15, 16,
					17, 18, 19, 20, 22, 24, 26, 28, 36, 48, 72 };
			Integer[] fontSizes = new Integer[fontSizesTemp.length];
			for (int i = 0; i < fontSizes.length; i++) {
				fontSizes[i] = fontSizesTemp[i];
			}
			this.listSize.setListData(fontSizes);
			Font font = txaMain.getFont();
			this.listSize.setSelectedValue(font.getSize(), true);
			this.listSize.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.setSizeView();
		}

		/**
		 * 更新列表的显示
		 */
		private void updateListView() {
			Font font = txaMain.getFont();
			this.listFont.setSelectedValue(font.getFamily(), true);
			this.listStyle.setSelectedIndex(font.getStyle());
			this.listSize.setSelectedValue(font.getSize(), true);
		}

		/**
		 * 根据当前"字体"列表的选择，更新顶部文本框的显示
		 */
		private void setFontView() {
			String strFont = this.listFont.getSelectedValue().toString();
			if (null != strFont) {
				this.txtFont.setText(strFont);
			}
		}

		/**
		 * 根据当前"字形"列表的选择，更新顶部文本框的显示
		 */
		private void setStyleView() {
			String strStyle = this.listStyle.getSelectedValue().toString();
			if (null != strStyle) {
				this.txtStyle.setText(strStyle);
			}
		}

		/**
		 * 根据当前"大小"列表的选择，更新顶部文本框的显示
		 */
		private void setSizeView() {
			String strSize = this.listSize.getSelectedValue().toString();
			if (null != strSize) {
				this.txtSize.setText(strSize);
			}
		}

		/**
		 * 设置示例文字和文本域的字体
		 * 
		 * @param isOk
		 *            用户是否点击了"确定"按钮
		 */
		private void setView(boolean isOk) {
			Font font = new Font(this.listFont.getSelectedValue().toString(),
					this.listStyle.getSelectedIndex(), (Integer) this.listSize
							.getSelectedValue());
			this.lblView.setFont(font);
			if (isOk) {
				txaMain.setFont(font);
			}
		}

		/**
		 * 添加各组件的事件监听器
		 */
		private void addListeners() {
			this.btnCancel.addActionListener(this);
			this.btnOk.addActionListener(this);
			this.listFont.addListSelectionListener(this);
			this.listStyle.addListSelectionListener(this);
			this.listSize.addListSelectionListener(this);
			this.listFont.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					txtFont.setText(listFont.getSelectedValue().toString());
				}
			});
			this.listStyle.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					txtStyle.setText(listStyle.getSelectedValue().toString());
				}
			});
			this.listSize.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					txtSize.setText(listSize.getSelectedValue().toString());
				}
			});
			this.txtFont.addCaretListener(this);
			this.txtStyle.addCaretListener(this);
			this.txtSize.addCaretListener(this);
			// 以下为各可获得焦点的组件添加键盘事件，即当用户按下Esc键时关闭"字体"对话框
			this.txtFont.addKeyListener(this.escKeyAdapter);
			this.txtStyle.addKeyListener(this.escKeyAdapter);
			this.txtSize.addKeyListener(this.escKeyAdapter);
			this.listFont.addKeyListener(this.escKeyAdapter);
			this.listStyle.addKeyListener(this.escKeyAdapter);
			this.listSize.addKeyListener(this.escKeyAdapter);
			this.btnCancel.addKeyListener(this.escKeyAdapter);
			this.btnOk.addKeyListener(this.escKeyAdapter);
		}

		/**
		 * "取消"按钮的处理方法
		 */
		private void onCancel() {
			this.dispose();
		}

		/**
		 * "确定"按钮的处理方法
		 */
		private void onOk() {
			this.setView(true);
			this.onCancel();
		}

		// java记事本-V1.1共分5部分，此为第4部分。
		// 只需将5个部分直接拼接起来，编译运行即可。

		/**
		 * 为各组件添加事件的处理方法
		 */
		public void actionPerformed(ActionEvent e) {
			if (this.btnCancel.equals(e.getSource())) {
				this.onCancel();
			} else if (this.btnOk.equals(e.getSource())) {
				this.onOk();
			}
		}

		/**
		 * 当列表框改变选择时，触发此事件
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (this.listFont.equals(e.getSource())) {
				if (!this.txtFont.getText().equals(
						this.listFont.getSelectedValue())) {
					this.txtFont.setText(this.listFont.getSelectedValue()
							.toString());
				}
			} else if (this.listStyle.equals(e.getSource())) {
				if (!this.txtStyle.getText().equals(
						this.listStyle.getSelectedValue())) {
					this.txtStyle.setText(this.listStyle.getSelectedValue()
							.toString());
				}
			} else if (this.listSize.equals(e.getSource())) {
				try {
					String strSize = this.txtSize.getText();
					if (strSize.length() > 0) {
						if (!new Integer(strSize).equals(this.listSize
								.getSelectedValue())) {
							this.txtSize.setText(this.listSize
									.getSelectedValue().toString());
						}
					}
				} catch (NumberFormatException x) {
					// 被转化的字符串不是数字
					x.printStackTrace();
				}
			}
			this.setView(false);
		}

		/**
		 * 当文本框的光标发生变化时，触发此事件
		 */
		public void caretUpdate(CaretEvent e) {
			if (this.txtFont.equals(e.getSource())) {
				this.listFont.setSelectedValue(this.txtFont.getText(), true);
			} else if (this.txtStyle.equals(e.getSource())) {
				this.listStyle.setSelectedValue(this.txtStyle.getText(), true);
			} else if (this.txtSize.equals(e.getSource())) {
				try {
					String strSize = this.txtSize.getText();
					if (strSize.length() > 0) {
						this.listSize.setSelectedValue(new Integer(strSize),
								true);
					}
				} catch (NumberFormatException x) {
					// 被转化的字符串不是数字
					x.printStackTrace();
				}
			}
		}

	}

	/**
	 * "查找"对话框
	 * 
	 * @author chen
	 * 
	 */
	private class FindDialog extends JDialog implements ActionListener,
			CaretListener {

		private static final long serialVersionUID = -5062238781614670004L;

		JLabel lblFindText = new JLabel("查找内容：");

		JTextField txtFindText = new JTextField();

		JCheckBox chkNotIgnoreCase = new JCheckBox("区分大小写(C)", false);

		JRadioButton radFindUp = new JRadioButton("向上(U)", false);

		JRadioButton radFindDown = new JRadioButton("向下(D)", true);

		JButton btnFind = new JButton("查找下一个(F)");

		JButton btnCancel = new JButton("取消");

		ButtonGroup bgpFindUpDown = new ButtonGroup();

		JPanel pnlFindUpDown = new JPanel(new GridLayout(1, 2));

		JPanel pnlMain = (JPanel) this.getContentPane();

		boolean isFindDown = true; // 向下查找

		boolean isIgnoreCase = true; // 忽略大小写

		Insets insets = new Insets(1, 1, 1, 1); // 组件边框

		EscKeyAdapter escKeyAdapter = new EscKeyAdapter(this);

		public FindDialog(JFrame owner, boolean modal) {
			super(owner, modal);
			this.setTitle("查找");
			this.txtFindText.setBackground(txaMain.getBackground());
			this.init();
			this.setMnemonic();
			this.addListeners();
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setSize(380, 115);
			this.setLocationRelativeTo(owner);
			this.setResizable(false);
			this.setVisible(true);
		}

		private void init() {
			this.pnlMain.setLayout(null);

			this.lblFindText.setBounds(10, 10, 60, 20);
			this.txtFindText.setBounds(80, 10, 180, 20);
			this.pnlMain.add(this.lblFindText);
			this.pnlMain.add(this.txtFindText);

			this.insets.set(0, 0, 0, 0);
			this.chkNotIgnoreCase.setMargin(this.insets);
			this.chkNotIgnoreCase.setBounds(6, 55, 101, 15);

			this.pnlMain.add(this.chkNotIgnoreCase);

			this.bgpFindUpDown.add(this.radFindDown);
			this.bgpFindUpDown.add(this.radFindUp);
			this.pnlFindUpDown.setBounds(111, 35, 150, 45);
			this.pnlFindUpDown.setBorder(new TitledBorder("方向"));
			this.pnlFindUpDown.add(this.radFindUp);
			this.pnlFindUpDown.add(this.radFindDown);
			this.pnlMain.add(this.pnlFindUpDown);

			this.btnFind.setEnabled(false);
			this.insets.set(1, 1, 1, 1);
			this.btnFind.setMargin(this.insets);
			this.btnFind.setBounds(270, 10, 95, 23);
			this.btnCancel.setBounds(270, 48, 95, 23);
			this.pnlMain.add(this.btnFind);
			this.pnlMain.add(this.btnCancel);
		}

		private void setMnemonic() {
			this.chkNotIgnoreCase.setMnemonic('C');
			this.btnFind.setMnemonic('F');
			this.radFindUp.setMnemonic('U');
			this.radFindDown.setMnemonic('D');
		}

		private void addListeners() {
			this.txtFindText.addCaretListener(this);
			this.btnFind.addActionListener(this);
			this.btnCancel.addActionListener(this);
			this.radFindDown.addActionListener(this);
			this.radFindUp.addActionListener(this);
			this.chkNotIgnoreCase.addActionListener(this);
			this.txtFindText.addKeyListener(this.escKeyAdapter);
			this.chkNotIgnoreCase.addKeyListener(this.escKeyAdapter);
			this.radFindDown.addKeyListener(this.escKeyAdapter);
			this.radFindUp.addKeyListener(this.escKeyAdapter);
			this.btnCancel.addKeyListener(this.escKeyAdapter);
			this.btnFind.addKeyListener(this.escKeyAdapter);
		}

		private void cancelFind() {
			this.dispose();
		}

		/**
		 * 开始查找字符串
		 */
		private void findText() {
			String strFindText = this.txtFindText.getText();
			if (strFindText != null && strFindText.length() > 0) {
				stbFindText = new StringBuffer(strFindText);
				int caretPos = txaMain.getCaretPosition();
				if (this.isFindDown) {
					this.findDownText(caretPos);
				} else {
					this.findUpText(caretPos);
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
				sbFindTemp = new StringBuffer(stbFindText.toString()
						.toLowerCase());
			}
			if (sbTemp != null && sbTemp.length() > 0) {
				int index = sbTemp.indexOf(sbFindTemp.toString());
				if (index >= 0) {
					caretPos += index;
					txaMain.select(caretPos, caretPos + sbFindTemp.length());
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
				sbFindTemp = new StringBuffer(stbFindText.toString()
						.toLowerCase());
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

		public void actionPerformed(ActionEvent e) {
			if (this.btnCancel.equals(e.getSource())) {
				this.cancelFind();
			} else if (this.btnFind.equals(e.getSource())) {
				this.findText();
			} else if (this.chkNotIgnoreCase.equals(e.getSource())) {
				this.isIgnoreCase = !this.chkNotIgnoreCase.isSelected();
			} else if (this.radFindDown.equals(e.getSource())) {
				this.isFindDown = true;
			} else if (this.radFindUp.equals(e.getSource())) {
				this.isFindDown = false;
			}
		}

		public void caretUpdate(CaretEvent e) {
			String strText = this.txtFindText.getText();
			if (strText == null || strText.length() == 0) {
				this.btnFind.setEnabled(false);
			} else {
				this.btnFind.setEnabled(true);
			}
		}

	}

	/**
	 * "替换"对话框
	 * 
	 * @author chen
	 * 
	 */
	private class ReplaceDialog extends JDialog implements ActionListener,
			CaretListener {

		private static final long serialVersionUID = -7602293855824973899L;

		JLabel lblFindText = new JLabel("查找内容：");

		JLabel lblReplaceText = new JLabel("替换为：");

		JTextField txtFindText = new JTextField();

		JTextField txtReplaceText = new JTextField();

		JCheckBox chkNotIgnoreCase = new JCheckBox("区分大小写(C)", false);

		JButton btnFind = new JButton("查找下一个(F)");

		JButton btnReplace = new JButton("替换(R)");

		JButton btnReplaceAll = new JButton("全部替换(A)");

		JButton btnCancel = new JButton("取消");

		JPanel pnlMain = (JPanel) this.getContentPane();

		boolean isIgnoreCase = true; // 忽略大小写

		Insets insets = new Insets(1, 1, 1, 1);

		EscKeyAdapter escKeyAdapter = new EscKeyAdapter(this);

		public ReplaceDialog(JFrame owner, boolean modal) {
			super(owner, modal);
			this.setTitle("替换");
			this.txtFindText.setBackground(txaMain.getBackground());
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

		private boolean findText() {
			String strFindText = this.txtFindText.getText();
			if (null == strFindText || strFindText.length() == 0) {
				return false;
			}
			stbFindText = new StringBuffer(strFindText);
			int caretPos = txaMain.getCaretPosition();
			if (!this.findDownText(caretPos)) {
				JOptionPane.showMessageDialog(this, "找不到\"" + stbFindText
						+ "\"", strTitle, JOptionPane.NO_OPTION);
				return false;
			}
			return true;
		}

		private boolean findDownText(int caretPos) {
			StringBuffer stbFindTemp = new StringBuffer(stbFindText);
			StringBuffer stbTemp = new StringBuffer();
			stbTemp.append(stbText.substring(caretPos, stbText.length()));
			if (this.isIgnoreCase) {
				stbTemp = new StringBuffer(stbTemp.toString().toLowerCase());
				stbFindTemp = new StringBuffer(stbFindText.toString()
						.toLowerCase());
			}
			if (stbTemp != null && stbTemp.length() > 0) {
				int index = stbTemp.indexOf(stbFindTemp.toString());
				if (index >= 0) {
					caretPos += index;
					txaMain.select(caretPos, caretPos + stbFindTemp.length());
					return true;
				}
			}
			return false;
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
					stbFindTextTemp = new StringBuffer(stbFindTextTemp
							.toString().toLowerCase());
					stbTextAllTemp = new StringBuffer(stbTextAllTemp.toString()
							.toLowerCase());
				}
				while (caretPos >= 0) {
					index = stbTextAllTemp.indexOf(stbFindTextTemp.toString(),
							caretPos);
					if (index >= 0) {
						stbTextAll.replace(index, index
								+ stbFindTextTemp.length(), strReplaceText);
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

	}

	/**
	 * "转到"对话框
	 * 
	 * @author chen
	 * 
	 */
	private class GotoDialog extends JDialog implements ActionListener {
		private static final long serialVersionUID = 569111487274212730L;

		JLabel lblGotoLine = new JLabel("行数：");

		JTextField txtGotoLine = new JTextField();

		JButton btnGoto = new JButton("确定");

		JButton btnCancel = new JButton("取消");

		JPanel pnlMain = (JPanel) this.getContentPane();

		EscKeyAdapter escKeyAdapter = new EscKeyAdapter(this);

		public GotoDialog(JFrame owner, boolean modal) {
			super(owner, modal);
			this.setTitle("转到下列行");
			this.txtGotoLine.setBackground(txaMain.getBackground());
			this.init();
			this.addListeners();
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setSize(255, 115);
			this.setLocationRelativeTo(owner);
			this.setResizable(false);
			this.setVisible(true);
		}

		private void init() {
			this.pnlMain.setLayout(null);

			this.lblGotoLine.setBounds(30, 10, 40, 20);
			this.txtGotoLine.setBounds(90, 10, 100, 20);
			this.pnlMain.add(this.lblGotoLine);
			this.pnlMain.add(this.txtGotoLine);

			this.btnGoto.setBounds(30, 50, 85, 23);
			this.btnCancel.setBounds(130, 50, 85, 23);
			this.pnlMain.add(this.btnGoto);
			this.pnlMain.add(this.btnCancel);
		}

		private void addListeners() {
			this.btnGoto.addActionListener(this);
			this.btnCancel.addActionListener(this);
			this.txtGotoLine.addKeyListener(this.escKeyAdapter);
			this.btnCancel.addKeyListener(this.escKeyAdapter);
			this.btnGoto.addKeyListener(this.escKeyAdapter);
		}

		/**
		 * "取消"按钮的处理方法
		 */
		private void cancelGoto() {
			this.dispose();
			try {
				String str = this.txtGotoLine.getText().trim();
				if (0 == str.length()) {
					return;
				}
				Integer.parseInt(str);
				this.txtGotoLine.setText(str);
			} catch (NumberFormatException x) {
				x.printStackTrace();
				this.txtGotoLine.setText("1");
			}
			this.txtGotoLine.selectAll();
		}

		/**
		 * 转到指定行
		 */
		private void gotoLine() {
			int totalLine = 1;
			int line = 1;
			for (int n = 0; n < stbText.length(); n++) {
				if ('\n' == stbText.charAt(n)) {
					totalLine++;
				}
			}
			try {
				line = Integer.parseInt(this.txtGotoLine.getText().trim());
			} catch (NumberFormatException x) {
				x.printStackTrace();
				JOptionPane.showMessageDialog(this, "格式错误！", strTitle,
						JOptionPane.CANCEL_OPTION);
				this.txtGotoLine.setText(totalLine + "");
				return;
			}
			if (line <= 0) {
				JOptionPane.showMessageDialog(this, "行数必须大于0！", strTitle,
						JOptionPane.CANCEL_OPTION);
				this.txtGotoLine.setText(1 + "");
			} else if (line > totalLine) {
				JOptionPane.showMessageDialog(this, "行数超出范围！", strTitle,
						JOptionPane.CANCEL_OPTION);
				this.txtGotoLine.setText(totalLine + "");
			} else if (line == 1) {
				txaMain.setCaretPosition(0);
				this.cancelGoto();
			} else {
				for (int n = 0, tempLine = 1; n < stbText.length(); n++) {
					if ('\n' == stbText.charAt(n)) {
						if (++tempLine == line) {
							txaMain.setCaretPosition(n + 1);
							break;
						}
					}
				}
				this.cancelGoto();
			}
		}

		public void actionPerformed(ActionEvent e) {
			if (this.btnCancel.equals(e.getSource())) {
				this.cancelGoto();
			} else if (this.btnGoto.equals(e.getSource())) {
				this.gotoLine();
			}
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
