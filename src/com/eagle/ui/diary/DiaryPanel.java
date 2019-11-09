package com.eagle.ui.diary;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterJob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.undo.UndoManager;

import org.apache.log4j.Logger;
import org.jfree.ui.ExtensionFileFilter;

import com.eagle.action.*;
import com.eagle.data.*;
import com.eagle.printer.MyPrinter;
import com.eagle.prop.NameProp;
import com.eagle.ui.datechooser.DateChooser;
import com.eagle.util.StringUtil;

/**
 * 日记本&资料档案界面
 * @author szhang
 *
 */
public class DiaryPanel 
		implements TreeSelectionListener,
				ListSelectionListener,
				ActionListener, 
				MouseListener, 
				KeyListener, 
				UndoableEditListener, 
				WindowFocusListener, 
				ItemListener,
				FocusListener,
				ChangeListener {
	private static Logger log = Logger.getLogger(DiaryPanel.class);
	// 日记本根节点
	private DefaultMutableTreeNode diaryRoot = new DefaultMutableTreeNode("我的日记");
	// 档案资料根节点
	private DefaultMutableTreeNode archivesRoot = new DefaultMutableTreeNode("档案资料");
	// 日记当前节点
	private DefaultMutableTreeNode diaryCurrentNode;
	// 档案当前节点
	private DefaultMutableTreeNode arkCurrentNode;
	// 日记本面板
	private JScrollPane diaryTreePane;
	// 档案资料面板
	private JScrollPane archivesTreePane;
	// 日记本事件处理
	private DiaryAction diaryAction = new DiaryAction();
	// 档案事件处理
	private ArkAction arkAction = new ArkAction();
	// 父窗体
	private JFrame parentFrame;
	// 当前数据
	private DiaryData listDiaryData = new DiaryData();
	// 标签面板
	private JTabbedPane tabbedPane;
	// 日记树
	private JTree diaryTree;
	// 档案资料树
	private JTree archivesTree;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private JButton undo = new JButton();
	private JButton redo = new JButton();
	
	private ReplaceDialog replaceDialog;
	private JComboBox size = new JComboBox();
	private JComboBox font = new JComboBox();
	
	public DiaryPanel(JFrame f){
		try {
			// 设置当前节点
			parentFrame = f;
			initCurData();
			checkCurDate(listDiaryData);
			editor.getDocument().addUndoableEditListener(this);
			editor.addFocusListener(this);
			
			// 加载树节点数据,并指定当前节点
			diaryAction.setDiaryMenu(diaryRoot);
			arkAction.setArchivesMenu(archivesRoot);
			diaryCurrentNode = diaryAction.getDayNode(listDiaryData.getDate(), diaryRoot);
			arkCurrentNode = archivesRoot;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
	}
	
	/**
	 * 构造日记界面
	 * @return
	 */
	public JComponent getDiaryPanel(int i){
		// 加载树
		diaryTree = getDiaryTree(diaryRoot);
		diaryTreePane = new JScrollPane(diaryTree);
		archivesTree = getArchiveTree(archivesRoot);
		archivesTreePane = new JScrollPane(archivesTree);
		
		tabbedPane = new JTabbedPane();
		// 添加第一个标签
		tabbedPane.addTab("日记", diaryTreePane);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		// 添加第二个标签
		tabbedPane.addTab("档案", archivesTreePane);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		tabbedPane.addChangeListener(this);
		
		JComponent editPanel = getEditPanel();
		JSplitPane splitPane= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tabbedPane, editPanel);
		splitPane.setDividerLocation(135);
		popup = createPopupMenu(true, true, true);
		extendTree(i);
		return splitPane;
	}
	
	/**
	 * 日记树
	 * @return
	 */
	private JTree getDiaryTree(DefaultMutableTreeNode node){
		JTree tree = new JTree(node);
		tree.addMouseListener(this);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this); //检查是否有TreeSelectionEvent事件。
        return tree;
	}
	
	/**
	 * 档案树
	 * @return
	 */
	private JTree getArchiveTree(DefaultMutableTreeNode node){
		JTree tree = new JTree(node);
		tree.addMouseListener(this);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this); //检查是否有TreeSelectionEvent事件。
        return tree;
	}
	
	/**
	 * 构造日记界面
	 * @return
	 */
	public JComponent getEditPanel(){
		JButton newDiary = new JButton();
		JButton refresh = new JButton();
		JButton rename = new JButton();
		JButton delete = new JButton();
		JButton printer = new JButton();
		JButton search = new JButton();
		JButton save = new JButton();
		JButton undo = new JButton();
		JButton redo = new JButton();
		JButton cut = new JButton();
		JButton copy = new JButton();
		JButton paste = new JButton();
		JButton bolder = new JButton();
		JButton italic = new JButton();
		JButton underline = new JButton();
		JButton color = new JButton();
		
		newDiary.addActionListener(this);
		newDiary.setActionCommand("newDiary");
		newDiary.setIcon(ImageData.newDiary);
		newDiary.setToolTipText("新建");// Ctrl+S
		
		refresh.addActionListener(this);
		refresh.setActionCommand("refresh");
		refresh.setIcon(ImageData.refresh);
		refresh.setToolTipText("刷新");// Ctrl+S
		
		rename.addActionListener(this);
		rename.setActionCommand("rename");
		rename.setIcon(ImageData.rename);
		rename.setToolTipText("重命名");// Ctrl+S
		
		delete.addActionListener(this);
		delete.setActionCommand("delete");
		delete.setIcon(ImageData.delete);
		delete.setToolTipText("删除");// Ctrl+S
		
		save.addActionListener(this);
		save.setActionCommand("save");
		save.setIcon(ImageData.save);
		save.setToolTipText("保存");// Ctrl+S
		
		printer.addActionListener(this);
		printer.setActionCommand("printer");
		printer.setIcon(ImageData.printer);
		printer.setToolTipText("打印");// Ctrl+P
		
		search.addActionListener(this);
		search.setActionCommand("search");
		search.setIcon(ImageData.search);
		search.setToolTipText("搜索&替换");// Ctrl+F
		
		undo.addActionListener(this);
		undo.setActionCommand("undo");
		undo.setIcon(ImageData.undo);
		undo.setToolTipText("回退");// Ctrl+Z
		
		redo.addActionListener(this);
		redo.setActionCommand("redo");
		redo.setIcon(ImageData.redo);
		redo.setToolTipText("重做");// Ctrl+Y
		
		cut.addActionListener(this);
		cut.setActionCommand("cut");
		cut.setIcon(ImageData.cut);
		cut.setToolTipText("剪切 Ctrl+X");
		
		copy.addActionListener(this);
		copy.setActionCommand("copy");
		copy.setIcon(ImageData.copy);
		copy.setToolTipText("拷贝 Ctrl+C");
		
		paste.addActionListener(this);
		paste.setActionCommand("paste");
		paste.setIcon(ImageData.paste);
		paste.setToolTipText("粘贴 Ctrl+V");
		
		bolder.addActionListener(this);
		bolder.setActionCommand("bolder");
		bolder.setIcon(ImageData.bolder);
		bolder.setToolTipText("粗体");
		
		italic.addActionListener(this);
		italic.setActionCommand("italic");
		italic.setIcon(ImageData.italic);
		italic.setToolTipText("斜体");
		
		underline.addActionListener(this);
		underline.setActionCommand("underline");
		underline.setIcon(ImageData.underline);
		underline.setToolTipText("下划线");
		
		color.addActionListener(this);
		color.setActionCommand("color");
		color.setIcon(ImageData.color);
		color.setToolTipText("字体颜色");
		

		setItem(font, size);
		font.addItemListener(this);
		font.setToolTipText("字体类型");
		
		size.addItemListener(this);
		size.setToolTipText("字体大小");
        
        JPanel panelAdd=new JPanel();
        JScrollPane sPanel = new JScrollPane(editor);
        sPanel.setPreferredSize(new Dimension(550,400));
        
        JToolBar bar1 = new JToolBar();
        JToolBar bar2 = new JToolBar();
        JToolBar bar3 = new JToolBar();
        bar1.add(newDiary);
        bar1.add(save);
        bar1.add(refresh);
        bar1.add(rename);
        bar1.add(delete);
        bar2.add(printer);
        bar2.add(search);
        bar2.add(undo);
        bar2.add(redo);
        bar2.add(cut);
        bar2.add(copy);
        bar2.add(paste);
        bar3.add(bolder);
        bar3.add(italic);
        bar3.add(underline);
        bar3.add(color);
        bar3.add(font);
        bar3.add(size);
        bar1.setRollover(true);
        bar1.setToolTipText("文档管理");
        bar1.setName("文档管理");
        bar1.setFloatable(false);
        bar2.setRollover(true); 
        bar2.setToolTipText("文档编辑");
        bar2.setName("文档编辑");
        bar2.setFloatable(false);
        bar3.setRollover(true);  
        bar3.setToolTipText("字体颜色");
        bar3.setName("字体颜色");
        bar3.setFloatable(false);
        
        panelAdd.add(bar1, BorderLayout.NORTH);
        panelAdd.add(bar2, BorderLayout.NORTH);
        panelAdd.add(bar3, BorderLayout.NORTH);
        panelAdd.add(sPanel, BorderLayout.CENTER);
        return panelAdd;
	}
	
	/**
	 * 事件响应
	 */
	public void actionPerformed(ActionEvent e) {
		try{
			// TODO Auto-generated method stub
			String action = e.getActionCommand();
			int tabbedPaneIndex = tabbedPane.getSelectedIndex();
			//-----------------------------------------------------日记本----------------------------------------------
			// 日记本
			if(0==tabbedPaneIndex){
				//刷新
				if("refresh".equals(action)){
					refresh();
				}
				//新建
				else if("newDiary".equals(action)){
					// 1 输入日记名				
					String dateStr;
					String itemStr;
					if(diaryCurrentNode==null||diaryCurrentNode.getLevel()<3){
						DateChooser dateChooser = new DateChooser(parentFrame);
						dateChooser.showChooser(null, 600, 300);
						Date date = dateChooser.getDate();
						if(date==null){
							return;
						}
						dateStr = df.format(date);
						itemStr = "Default";
					}else{
						dateStr = diaryAction.getDateFromNode(diaryCurrentNode);
						itemStr = JOptionPane.showInputDialog(parentFrame, "请输入日记名:", "");
					}
					if(StringUtil.isEmpty(dateStr)||StringUtil.isEmpty(itemStr)){
						return;
					}
					// 2 验重
					DiaryData diaryData = new DiaryData();
					diaryData.setDate(dateStr);
					diaryData.setItem(itemStr);
					if(diaryAction.isRepeat(diaryData)){
						JOptionPane.showMessageDialog(parentFrame, "日记已存在！");
						return;
					}
					// 3 新建
					diaryAction.newDiary(diaryData);
					// 4 刷新
					refresh();
				}
				// 保存
				else if("save".equals(action)){
					DiaryData diaryData = new DiaryData();
					diaryData.setDate(listDiaryData.getDate());
					diaryData.setItem(listDiaryData.getItem());
					diaryData.setContent(editor.getText());
					diaryAction.saveDiary(diaryData);
				}
				// 重命名
				else if("rename".equals(action)){
					// 只能针对叶子节点操作
					if(diaryCurrentNode==null||diaryCurrentNode.getLevel()!=4){
						JOptionPane.showMessageDialog(parentFrame, "当前日记时间不能修改！");
						return;
					}
					// 新名称
					String rename = JOptionPane.showInputDialog(parentFrame, "请输入新名称：", "");
					if(StringUtil.isEmpty(rename)){
						return;
					}
					DiaryData diaryData = diaryAction.getDiaryData(diaryCurrentNode);
					diaryData.setRename(rename);
					// 查重
					DiaryData renameDiary = new DiaryData();
					renameDiary.setDate(diaryData.getDate());
					renameDiary.setItem(diaryData.getRename());
					if(diaryAction.isRepeat(renameDiary)){
						JOptionPane.showMessageDialog(parentFrame, "日记已存在！");
						return;
					}
					// 重命名
					diaryAction.renameDiary(diaryData);
					// 刷新
					refresh();
					// 展示到当前新建节点
				}
				// 删除
				else if("delete".equals(action)){
 					if(JOptionPane.showConfirmDialog(parentFrame, "确定要删除当前日记吗？", "删除提醒", JOptionPane.OK_CANCEL_OPTION)==1){
						return;
					}
					// 删除
					DiaryData diaryData = diaryAction.getDiaryData(diaryCurrentNode);
					diaryAction.deleteDiary(diaryData);
					initCurData();
					// 刷新
					refresh();
				}
			}
			// ---------------------------------------------------------------文档资料管理-----------------------------------------------------------
			// 文档资料
			else{
				//刷新
				if("refresh".equals(action)){
					refresh();
				}
				//新建
				else if("newDiary".equals(action)){
					// 0 判断是否选中节点
					if(arkCurrentNode==null){
						JOptionPane.showMessageDialog(parentFrame, "请选择要添加的目录！");
						return;
					}
					// 1 输入档案名				
					String itemStr = JOptionPane.showInputDialog(parentFrame, "请输入档案资料名:", "");
					if(StringUtil.isEmpty(itemStr)){
						return;
					}
					// 2 验重
					if(arkAction.isRepeat(arkCurrentNode, itemStr)){
						JOptionPane.showMessageDialog(parentFrame, "档案资料已存在！");
						return;
					}
					// 3 新建
					arkAction.createArkMenu(arkCurrentNode, itemStr);
					arkCurrentNode = arkAction.setAddedNode(arkCurrentNode, itemStr);
					// 4 刷新
					refresh();
				}
				// 保存
				else if("save".equals(action)){
					arkAction.saveArkMenu(arkCurrentNode, editor.getText());
				}
				// 重命名
				else if("rename".equals(action)){
					// 0 判断是否选中节点
					if(arkCurrentNode==null){
						JOptionPane.showMessageDialog(parentFrame, "请选择要修改的目录！");
						return;
					}
					// 1 新名称
					String rename = JOptionPane.showInputDialog(parentFrame, "请输入档案资料新名称：", "");
					if(StringUtil.isEmpty(rename)){
						return;
					}
					// 查重
					if(arkAction.isRepeatL(arkCurrentNode, rename)){
						JOptionPane.showMessageDialog(parentFrame, "档案资料已存在！");
						return;
					}
					// 重命名
					arkAction.renameArkMenu(arkCurrentNode, rename);
					arkCurrentNode = arkAction.setUpdatedNode(arkCurrentNode, rename);
					// 刷新
					refresh();
				}
				// 删除
				else if("delete".equals(action)){
					// 0 判断是否选中节点
					if(arkCurrentNode==null){
						JOptionPane.showMessageDialog(parentFrame, "请选择要删除的目录！");
						return;
					}
					if(JOptionPane.showConfirmDialog(parentFrame, "确定要删除当前档案资料吗？", "删除提醒", JOptionPane.OK_CANCEL_OPTION)==1){
						return;
					}
					// 删除
					arkAction.deleteArkMenu(arkCurrentNode);
					arkCurrentNode = null;
					// 刷新
					refresh();
				}
			}
			
			//----------------------------------------------------------其它通用项----------------------------------------------------
			if("printer".equals(action)){
				PrinterJob printerJob = PrinterJob.getPrinterJob();
				MyPrinter printer = new MyPrinter(editor, parentFrame);
				printerJob.setPrintable(printer);
				printerJob.printDialog();				
				printerJob.print();
			}else if("search".equals(action)){
				openReplaceDialog();
			}else if("undo".equals(action)){
				editor.undo();
				setMenuStateUndoRedo();
			}else if("redo".equals(action)){
				editor.redo();
				setMenuStateUndoRedo();
			}if("copy".equals(action)){
				editor.copy();
			}else if("cut".equals(action)){
				editor.cut();
			}else if("paste".equals(action)){
				editor.paste();
			}else if("color".equals(action)){
				Color newColor=JColorChooser.showDialog(parentFrame, "请选择文字颜色", editor.getForeground());
				if(newColor==null){
					return;
				}
				DataBakAction baction = new DataBakAction();
				baction.setFSC("color", String.valueOf(newColor.hashCode()));
				editor.setForeground(newColor);  
			}else if("underline".equals(action)){
				DataBakAction baction = new DataBakAction();
				baction.isBIU("underline", editor.isUnderline());
				Font f = editor.getMyFont();
				editor.setFont(f);
			}else if("bolder".equals(action)){
				DataBakAction baction = new DataBakAction();
				baction.isBIU("bolder", editor.isBolder());
				Font f = editor.getMyFont();
				editor.setFont(f);
			}else if("italic".equals(action)){
				DataBakAction baction = new DataBakAction();
				baction.isBIU("italic", editor.isItalic());
				Font f = editor.getMyFont();
				editor.setFont(f);
			}else if("export".equals(action)){
				JFileChooser fileChooser = new JFileChooser();
		        fileChooser.setCurrentDirectory(null);
		        ExtensionFileFilter filter = new ExtensionFileFilter("*.html", ".html");
		        fileChooser.addChoosableFileFilter(filter);

		        int option = fileChooser.showSaveDialog(parentFrame);
		        if (option == JFileChooser.APPROVE_OPTION) {
		            String filename = fileChooser.getSelectedFile().getPath();
	                if (!filename.endsWith(".html")) {
	                    filename = filename + ".html";
	                }
	                if(0==tabbedPane.getSelectedIndex()){
	                	diaryAction.grt2HTML(filename, tabbedPane.getSelectedIndex(), diaryCurrentNode);
	                }else{
	                	diaryAction.grt2HTML(filename, tabbedPane.getSelectedIndex(), arkCurrentNode);
	                }
		        }
			}		
		}catch(Exception e1){log.error(e);}
	}
	
	public void valueChanged(TreeSelectionEvent e) {
        try {
        	// 自动保存内容
			actionPerformed(new ActionEvent("", 0, "save"));
        	int tabbedPaneIndex = tabbedPane.getSelectedIndex();
        	JTree tree = (JTree) e.getSource();
        	if(0==tabbedPaneIndex){
        		diaryCurrentNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	            if(diaryCurrentNode==null||diaryCurrentNode.getLevel()<3){
	            	return;
	            }
	            if(diaryCurrentNode.getLevel()==3){
	            	listDiaryData.setItem("Default");
	            	listDiaryData.setDate(diaryAction.getDateFromNode(diaryCurrentNode));
	            }else{
	            	listDiaryData.setItem(diaryCurrentNode.toString());
	            	listDiaryData.setDate(diaryAction.getDateFromNode(diaryCurrentNode));
	            }
	            listDiaryData.setContent(diaryAction.getContent(listDiaryData));
				editor.setText(listDiaryData.getContent());
        	}else{
        		arkCurrentNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        		editor.setText(arkAction.getArkContent(arkCurrentNode));
        	}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.error(e);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 构造右键
	 * @param couldRefresh
	 * @param couldNew
	 * @param couldDelete
	 * @return
	 */
	protected JPopupMenu createPopupMenu(boolean couldRefresh,
			boolean couldNew, boolean couldDelete) {

		JPopupMenu result = new JPopupMenu("Chart:");
		boolean separator = false;

		if (couldRefresh) {
			JMenuItem refreshItem = new JMenuItem("刷新", ImageData.refresh);
			refreshItem.setActionCommand("refresh");
			refreshItem.addActionListener(this);
			//refreshItem.setAccelerator(KeyStroke.getKeyStroke('R',Event.CTRL_MASK,false));
			result.add(refreshItem);
			separator = true;
		}

		if (couldNew) {
			if (separator) {
				result.addSeparator();
				separator = false;
			}
			JMenuItem copyItem = new JMenuItem("新建", ImageData.newDiary);
			copyItem.setActionCommand("newDiary");
			copyItem.addActionListener(this);
			//copyItem.setAccelerator(KeyStroke.getKeyStroke('N',Event.CTRL_MASK,false));
			result.add(copyItem);
			separator = true;
		}
		
		if (couldNew) {
			if (separator) {
				result.addSeparator();
				separator = false;
			}
			JMenuItem renameItem = new JMenuItem("重命名", ImageData.rename);
			renameItem.setActionCommand("rename");
			renameItem.addActionListener(this);
			//renameItem.setAccelerator(KeyStroke.getKeyStroke('N',Event.CTRL_MASK,false));
			result.add(renameItem);
			separator = !couldNew;
		}

		if (couldDelete) {
			if (separator) {
				result.addSeparator();
				separator = false;
			}
			JMenuItem deleteItem = new JMenuItem("删除", ImageData.delete);
			deleteItem.setActionCommand("delete");
			deleteItem.addActionListener(this);
			//deleteItem.setAccelerator(KeyStroke.getKeyStroke('D',Event.CTRL_MASK,false));
			result.add(deleteItem);
			separator = true;
		}
		
		if (couldDelete) {
			if (separator) {
				result.addSeparator();
				separator = false;
			}
			JMenuItem exportItem = new JMenuItem("导出日记资料", ImageData.exportData);
			exportItem.setActionCommand("export");
			exportItem.addActionListener(this);
			//deleteItem.setAccelerator(KeyStroke.getKeyStroke('D',Event.CTRL_MASK,false));
			result.add(exportItem);
			separator = true;
		}

		return result;

	}
	
	protected void displayPopupMenu(JComponent tree, int x, int y) {
		if (this.popup != null) {
			this.popup.show(tree, x, y);
		}
	}
	
	private MyBook editor=new MyBook();
	private JPopupMenu popup;	
	
	/**
	 * 刷新界面
	 *
	 */
	private void refresh(){
		try {
			int tabbedPaneIndex = tabbedPane.getSelectedIndex();
			if(tabbedPaneIndex==0){
				diaryRoot.removeAllChildren();
				diaryAction.setDiaryMenu(diaryRoot);
				Container container = parentFrame.getContentPane();
				container.removeAll();
	            container.add(getDiaryPanel(0), BorderLayout.CENTER);
	            parentFrame.setContentPane(container);
	            parentFrame.invalidate();
			}else{
				archivesRoot.removeAllChildren();
				arkAction.setArchivesMenu(archivesRoot);
				Container container = parentFrame.getContentPane();
				container.removeAll();
	            container.add(getDiaryPanel(1), BorderLayout.CENTER);
	            parentFrame.setContentPane(container);
	            parentFrame.invalidate();
	            tabbedPane.setSelectedIndex(1);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.error(e1);
		}
	}
	
	/**
	 * 展开树默认节点
	 *
	 */
	private void extendTree(int idx){
		if(idx==0){
			diaryCurrentNode = diaryAction.getDayNode(listDiaryData.getDate(), diaryRoot);
			if(diaryCurrentNode == null){
				diaryCurrentNode = diaryRoot;
			}
			TreePath treePath = new TreePath(diaryCurrentNode.getPath());
			diaryTree.expandPath(treePath);
			diaryTree.setSelectionPath(treePath);
			diaryTree.scrollPathToVisible(treePath);
		}else{
			arkCurrentNode = arkAction.getArkMenu(arkCurrentNode, archivesRoot);
			if(arkCurrentNode == null){
				arkCurrentNode = archivesRoot;
			}
			TreePath treePath = new TreePath(arkCurrentNode.getPath());
			archivesTree.expandPath(treePath);
			archivesTree.setSelectionPath(treePath);
			archivesTree.scrollPathToVisible(treePath);
		}
	}
	
	/**
	 * 设置撤销与重做菜单项的状态
	 */
	private void setMenuStateUndoRedo() {
		UndoManager undoManager = editor.getUndoManager();
		boolean canRedo = undoManager.canRedo();
		boolean canUndo = undoManager.canUndo();
		redo.setEnabled(canRedo);
		undo.setEnabled(canUndo);
	}
	
	/**
	 * 是否已经打开了查找或替换对话框
	 * 
	 * @return 如果已经打开了查找或替换对话框则返回false
	 */
	private boolean canOpenDialog() {
		if (this.replaceDialog != null && this.replaceDialog.isVisible()) {
			return false;
		}
		return true;
	}
	
	/**
	 * "替换"的处理方法
	 */
	private void openReplaceDialog() {
		if (!this.canOpenDialog()) {
			return;
		}
		if (this.replaceDialog == null) {
			editor.setFocusable(true);
			replaceDialog = new ReplaceDialog(parentFrame, false, editor);
		} else {
			editor.setFocusable(true);
			this.replaceDialog.setLocationRelativeTo(parentFrame);
			this.replaceDialog.setVisible(true);
		}
		editor.requestFocus();
	}
	
	/**
	 * 当主窗口获得焦点时，将触发此事件
	 */
	public void windowGainedFocus(WindowEvent e) {

	}

	/**
	 * 当主窗口失去焦点时，将触发此事件
	 */
	public void windowLostFocus(WindowEvent e) {

	}
	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//if(e.isPopupTrigger()){
		if(e.getButton()==MouseEvent.BUTTON3){
			displayPopupMenu((JComponent)e.getSource(),e.getX(),e.getY());
		}
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 当文本域中的文本发生变化时，将触发此事件
	 */
	public void undoableEditHappened(UndoableEditEvent e) {
		editor.getUndoManager().addEdit(e.getEdit());
		setMenuStateUndoRedo(); // 设置撤销和重做菜单的状态
	}
	
	/**
	 * 设置下列列表
	 * @param font
	 * @param size
	 */
	private void setItem(JComboBox font, JComboBox size){
		ComboData cd;
		Integer key;
		Enumeration en = fontTable.keys();
		while(en.hasMoreElements()){
			cd = new ComboData();
			key = (Integer)en.nextElement();
			cd.setValue(key);
			cd.setName(fontTable.get(key));
			font.addItem(cd);
			if(key==editor.getFontType()){
				font.setSelectedItem(cd);
			}
		}
		
		size.addItem("5");
		size.addItem("6");
		size.addItem("8");
		size.addItem("10");
		size.addItem("12");
		size.addItem("13");
		size.addItem("14");
		size.addItem("16");
		size.addItem("18");
		size.addItem("20");
		size.addItem("22");
		size.addItem("26");
		size.addItem("30");
		size.addItem("48");
		size.addItem("72");
		size.setSelectedItem(String.valueOf(editor.getFontSize()));
	}
	public static Hashtable<Integer, String> fontTable = new Hashtable<Integer, String>();
	static{
		fontTable.put(1, "方正舒体"); 
		fontTable.put(2, "方正姚体"); 
		fontTable.put(3, "仿宋_gb2312");
		fontTable.put(4, "黑体");     
		fontTable.put(5, "华文彩云"); 
		fontTable.put(6, "华文仿宋"); 
		fontTable.put(7, "华文细黑"); 
		fontTable.put(8, "华文新魏"); 
		fontTable.put(9, "华文行楷"); 
		fontTable.put(10, "华文中宋");
		fontTable.put(11, "楷体_gb2312");
		fontTable.put(12, "隶书");    
		fontTable.put(13, "宋体");    
		fontTable.put(14, "新宋体");  
		fontTable.put(15, "幼圆"); 
	}

	/**
	 * 下拉框选择
	 */
	public void itemStateChanged(ItemEvent e) {
		try{
			if(e.getSource()==font){
				ComboData cdata = (ComboData)font.getSelectedItem();
				if(cdata.getValue()!=editor.getFontType()){
					DataBakAction baction = new DataBakAction();
					
					baction.setFSC("font", String.valueOf(cdata.getValue()));
					Font f = editor.getMyFont();
					editor.setFont(f);
				}
			}else if(e.getSource()==size){
				if(Integer.parseInt(size.getSelectedItem().toString()) != editor.getFontSize()){
					DataBakAction baction = new DataBakAction();
					baction.setFSC("size", size.getSelectedItem().toString());
					Font f = editor.getMyFont();
					editor.setFont(f);
				}
			}
		}catch(Exception ex){
			log.error(ex);
		}
	}

	public void focusGained(FocusEvent e) {
		
	}

	/**
	 * 文本框失去焦点事件
	 */
	public void focusLost(FocusEvent e) {
		actionPerformed(new ActionEvent("", 0, "save"));
	}
	
	/**
	 * 当前节点日记不存在的话创建日记
	 * @param curData
	 */
	public void checkCurDate(DiaryData curData){
		try {
			if(!diaryAction.isRepeat(curData)){
				diaryAction.newDiary(curData);
			}else{
				editor.setText(diaryAction.getContent(curData));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
	}
	
	/**
	 * 打开日记本初始化当前节点
	 *
	 */
	public void initCurData(){
		listDiaryData.setDate(df.format(new Date()));
		listDiaryData.setItem(NameProp.DEFAULT);
	}

	/**
	 * TAB标签切换事件
	 */
	public void stateChanged(ChangeEvent e) {
		int tabbedPaneIndex = tabbedPane.getSelectedIndex();
		if(tabbedPaneIndex==0){
			editor.setText(diaryAction.getContent(listDiaryData));
			extendTree(0);
		}else{
			if(arkCurrentNode==null){
				arkCurrentNode = archivesRoot;
			}else{
				DefaultMutableTreeNode rtNode = (DefaultMutableTreeNode)arkCurrentNode.getRoot();
				if("我的日记".equals(rtNode.getUserObject())){
					arkCurrentNode = archivesRoot;
				}
			}
			
			editor.setText(arkAction.getArkContent(arkCurrentNode));
		}
	}
}