package com.eagle.ui.plain;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;
import org.jfree.ui.ExtensionFileFilter;

import com.eagle.action.ExcelAction;
import com.eagle.action.PlainAction;
import com.eagle.data.ImageData;
import com.eagle.data.PlainData;
import com.eagle.ui.datechooser.DateChooser;
import com.eagle.util.StringUtil;
import com.eagle.util.TableUtil;

/**
 * 工作学习计划界面
 * @author szhang
 *
 */
public class PlainPanel implements TreeSelectionListener,ListSelectionListener,
ActionListener, MouseListener, KeyListener, UndoableEditListener, WindowFocusListener, ItemListener{
	private static Logger log = Logger.getLogger(PlainPanel.class);
	private JPopupMenu popup;
	// 工作学习计划根节点
	private IconNode plainRoot = new IconNode("工作学习计划");
	
	private IconNode currentNode;
	// 工作学习计划面板
	private JScrollPane plainTreePane;
	// 工作学习计划事件处理
	private PlainAction plainAction = new PlainAction();
	// 当前数据
	private PlainData currentPlainData = new PlainData();
	// 标签面板
	private JTabbedPane tabbedPane;
	// 工作学习计划树
	private JTree plainTree;
	// 父窗体
	private JFrame parentFrame;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private JTextField filterData = new JTextField("", 10);
	private TableRowSorter sorter;
	private JTable table;
	
	public PlainPanel(JFrame f){
		try {
			parentFrame = f;
			// 加载树节点数据,并指定当前节点
			currentPlainData.setDate(df.format(new Date()));
			plainAction.checkToday(currentPlainData);
			plainAction.setPlainMenu(plainRoot);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
	}
	
	/**
	 * 构造学习工作计划界面
	 * @return
	 */
	public JComponent getPlainPanel(){
		// 加载树
		plainTree = getListTree(plainRoot);
		plainTree.setCellRenderer(new IconNodeRenderer());
		plainTreePane = new JScrollPane(plainTree);
		
		tabbedPane = new JTabbedPane();
		// 添加第一个标签
		tabbedPane.addTab("工作学习计划", plainTreePane);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		extendTree();
		JComponent editPanel = getEditPanel();
		JSplitPane splitPane= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tabbedPane, editPanel);
		splitPane.setDividerLocation(135);
        
        popup = createPopupMenu(true, true, true);
        
		return splitPane;
	}
	
	/**
	 * 工作学习计划树
	 * @return
	 */
	private JTree getListTree(IconNode node){
		JTree tree = new JTree(node);
		tree.addMouseListener(this);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this); //检查是否有TreeSelectionEvent事件。
        return tree;
	}
	
	/**
	 * 构造工作学习界面
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JComponent getEditPanel(){
		JPanel panelTable = new JPanel();
        JPanel panelButton=new JPanel(new GridLayout(2,4,1,1));
        JPanel panel_1=new JPanel();
        JPanel panel_2=new JPanel();
        JButton button_1=new JButton("新建");
        button_1.addActionListener(this);
        button_1.setActionCommand("add");
        JButton button_2=new JButton("明日计划");
        button_2.addActionListener(this);
        button_2.setActionCommand("addTmr");
        JButton button_3=new JButton("删除");
        button_3.addActionListener(this);
        button_3.setActionCommand("delete");
        JButton button_4=new JButton("查询");
        button_4.addActionListener(this);
        button_4.setActionCommand("view");
        JButton button_5=new JButton("昨日计划");
        button_5.addActionListener(this);
        button_5.setActionCommand("addYsd");
        JButton button_6=new JButton("确认完成");
        button_6.addActionListener(this);
        button_6.setActionCommand("completed");
        JButton button_7=new JButton("详情&修改");
        button_7.addActionListener(this);
        button_7.setActionCommand("update");

        panel_2.add(filterData);
        panel_2.add(button_4);
        panel_2.add(button_1);
        panel_2.add(button_7);
        panel_2.add(button_6);
        panel_2.add(button_3);
        panel_2.add(button_2);
        panel_2.add(button_5);
        panel_1.add(new JLabel(""));//  显示选中的列信息
        panelButton.add(panel_1);
        panelButton.add(panel_2);
       
        DefaultTableModel model = new DefaultTableModel(plainAction.getPlainData(currentPlainData.getDate()), plainAction.getPlainHead());
        table=new JTable(model){
        	// 不可编辑
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
        };
        // 列不可拖动
        table.getTableHeader().setReorderingAllowed(false);
		sorter = new TableRowSorter(model);
		table.setRowSorter(sorter);

        table.setPreferredScrollableViewportSize(new Dimension(535,370));
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//实现左右滚动
        table.setCellSelectionEnabled(true);
        //table.setAutoCreateRowSorter(true);
        ListSelectionModel  selectionMode=table.getSelectionModel();//取得table的ListSelectionModel.
        selectionMode.addListSelectionListener((ListSelectionListener) this);
        TableUtil.paintPlainRow(table);
        JScrollPane scrollPane1 = new JScrollPane(table);
        JScrollPane scrollPane2 = new JScrollPane(panel_2);
        JSplitPane splitPane= new JSplitPane(JSplitPane.VERTICAL_SPLIT,true, scrollPane1,scrollPane2);
        panelTable.add(splitPane,BorderLayout.CENTER);
        return panelTable;
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
			result.add(refreshItem);
			separator = true;
		}

		if (couldNew) {
			if (separator) {
				result.addSeparator();
				separator = false;
			}
			JMenuItem copyItem = new JMenuItem("新建", ImageData.newDiary);
			copyItem.setActionCommand("newPlainDay");
			copyItem.addActionListener(this);
			result.add(copyItem);
			separator = false;
		}

		if (couldDelete) {
			if (separator) {
				result.addSeparator();
				separator = false;
			}
			JMenuItem deleteItem = new JMenuItem("删除", ImageData.delete);
			deleteItem.setActionCommand("deletePlainDay");
			deleteItem.addActionListener(this);
			result.add(deleteItem);
			separator = true;
		}
		
		if (couldDelete) {
			if (separator) {
				result.addSeparator();
				separator = false;
			}
			JMenuItem exportItem = new JMenuItem("导到本地", ImageData.exportData);
			exportItem.setActionCommand("export");
			exportItem.addActionListener(this);
			result.add(exportItem);
			separator = true;
		}

		return result;
	}
	
	/**
	 * 事件响应
	 */
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if("refresh".equals(action)){
			refresh();
		}else if("newPlainDay".equals(action)){
			DateChooser dateChooser = new DateChooser(parentFrame);
			dateChooser.showChooser(null, 600, 300);
			Date date = dateChooser.getDate();
			if(date==null){
				return;
			}
			String dateStr = df.format(date);
			boolean flag = plainAction.newPlainDay(dateStr);
			if(flag){
				currentPlainData.setDate(dateStr);
				currentPlainData.setCurrentPlainData(null);
				refresh();
			}else{
				JOptionPane.showMessageDialog(parentFrame, "工作计划已存在");
			}
		}else if("deletePlainDay".equals(action)){
			if(StringUtil.isEmpty(currentPlainData.getDate())){
				JOptionPane.showMessageDialog(parentFrame, "请选择要删除的计划日期！");
			}else{
				if(JOptionPane.showConfirmDialog(parentFrame, "确定要删除此数据吗？", "删除提示", 0)==0){
					plainAction.deletePlainDay(currentPlainData.getDate());
					currentNode = null;
					refresh();
				}
			}
		}else if("export".equals(action)){
			JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setCurrentDirectory(null);
	        ExtensionFileFilter filter = new ExtensionFileFilter("*.xls", ".xls");
	        fileChooser.addChoosableFileFilter(filter);

	        int option = fileChooser.showSaveDialog(parentFrame);
	        if (option == JFileChooser.APPROVE_OPTION) {
	            String filename = fileChooser.getSelectedFile().getPath();
                if (!filename.endsWith(".xls")) {
                    filename = filename + ".xls";
                }
                ExcelAction.grt2Excel("工作学习计划明细", filename, plainAction.getExcelPlainHead(), plainAction.getExportData(currentPlainData.getDate()));
	        }
		}else if("add".equals(action)){
			new PlainAddPanel(parentFrame, currentPlainData);
			refreshTable();
		}else if("addTmr".equals(action)){
			Calendar cal = new GregorianCalendar();
			cal.add(Calendar.DATE, 1);
			String tmrDate = df.format(cal.getTime());
			plainAction.newPlainDay(tmrDate);
			currentPlainData.setDate(tmrDate);
			currentPlainData.setCurrentPlainData(null);
			refresh();
		}else if("addYsd".equals(action)){
			Calendar cal = new GregorianCalendar();
			cal.add(Calendar.DATE, -1);
			String tmrDate = df.format(cal.getTime());
			plainAction.newPlainDay(tmrDate);
			currentPlainData.setDate(tmrDate);
			currentPlainData.setCurrentPlainData(null);
			refresh();
		}else if("delete".equals(action)){
			if(currentPlainData.getCurrentPlainData()==null||StringUtil.isEmpty(currentPlainData.getCurrentPlainData().getId())){
				JOptionPane.showMessageDialog(parentFrame, "请选择要删除的计划");
			}else{
				if(JOptionPane.showConfirmDialog(parentFrame, "确定要删除当前工作吗？", "确认提示", 0)==0){
					plainAction.deletePlain(currentPlainData.getCurrentPlainData());
					refreshTable();
					currentPlainData.setCurrentPlainData(null);
				}
			}
		}else if("view".equals(action)){
			String text = filterData.getText();
			if (text.length() == 0){
				sorter.setRowFilter(null);
			}else{
				sorter.setRowFilter(RowFilter.regexFilter(text));
			}
		}else if("completed".equals(action)){
			if(currentPlainData.getCurrentPlainData()==null||StringUtil.isEmpty(currentPlainData.getCurrentPlainData().getId())){
				JOptionPane.showMessageDialog(parentFrame, "请选择确认完成的计划");
			}else{
				if(JOptionPane.showConfirmDialog(parentFrame, "确定当前工作已完成了吗？", "确认提示", 0)==0){
					currentPlainData.getCurrentPlainData().setCompleted("是");
					plainAction.completePlain(currentPlainData.getCurrentPlainData());
					refreshTable();
				}
			}
		}else if("update".equals(action)){
			if(currentPlainData.getCurrentPlainData()==null||StringUtil.isEmpty(currentPlainData.getCurrentPlainData().getId())){
				JOptionPane.showMessageDialog(parentFrame, "请选择要查看或者修改的计划");
			}else{
				new PlainUpdatePanel(parentFrame, currentPlainData);
				refreshTable();
			}
		}
	}
	
	public void valueChanged(TreeSelectionEvent e) {
        try {
        	JTree tree = (JTree) e.getSource();
        	currentNode = (IconNode)tree.getLastSelectedPathComponent();
        	setPlainData(currentNode);
            refreshTable();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.error(e1);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		try{
			PlainData plainData = new PlainData();
            int[] rows=table.getSelectedRows();
            if(rows.length==0){
            	return;
            }
            plainData.setDate(currentPlainData.getDate());
            plainData.setId(table.getValueAt(rows[0],0).toString().trim());
            plainData.setTitle(table.getValueAt(rows[0],1).toString().trim());
            plainData.setContent(table.getValueAt(rows[0],2).toString().trim());
            plainData.setCompleted(table.getValueAt(rows[0],3).toString().trim());
            currentPlainData.setCurrentPlainData(plainData);
        }catch(Exception ex){
        	log.error("ArrayError:"+ex.getMessage());
        }
	}
	
	/**
	 * 刷新界面
	 *
	 */
	private void refresh(){
		try {
			plainRoot.removeAllChildren();
			plainAction.setPlainMenu(plainRoot);
			Container container = parentFrame.getContentPane();
			container.removeAll();
            container.add(getPlainPanel(), BorderLayout.CENTER);
            parentFrame.setContentPane(container);
            parentFrame.invalidate();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.error(e1);
		}
	}
	
	/**
	 * 展开树默认节点
	 *
	 */
	private void extendTree(){
		try{
			currentNode = plainAction.getDayNode(currentPlainData.getDate(), plainRoot);
			if(currentNode!=null){
				TreePath treePath = new TreePath(currentNode.getPath());
				plainTree.expandPath(treePath);
				plainTree.setSelectionPath(treePath);
				plainTree.scrollPathToVisible(treePath);
			}
		}catch(Exception e){
			log.error(e);
		}
	}
	
	/**
	 * 根据节点构造工作学习计划BEAN
	 * @param node
	 * @return
	 * @throws Exception
	 */
	private void setPlainData(IconNode node) throws Exception{
		if(node.getLevel()==1){
			currentPlainData.setDate(node.toString().replace("年", ""));
		}else if(node.getLevel()==2){
			currentPlainData.setDate((node.getParent().toString()+"-"+node.toString()).replace("年", "").replace("月", ""));
		}else if(node.getLevel()==3){
			currentPlainData.setDate(plainAction.getDateFromNode(node));
		}
	}
	
	private void refreshTable(){
		try{
			if(table == null||table.getModel()==null){
				return;
			}
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			model.setDataVector(plainAction.getPlainData(currentPlainData.getDate()), plainAction.getPlainHead());
			model.fireTableDataChanged();
			TableUtil.paintPlainRow(table);
		}catch(Exception e){
			log.error(e);
		}
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
		if(e.getButton()==MouseEvent.BUTTON3){
			displayPopupMenu((JComponent)e.getSource(),e.getX(),e.getY());
		}
	}
	
	protected void displayPopupMenu(JComponent tree, int x, int y) {
		if (this.popup != null) {
			this.popup.show(tree, x, y);
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
	
	}

	public void itemStateChanged(ItemEvent e) {
		
	}
}