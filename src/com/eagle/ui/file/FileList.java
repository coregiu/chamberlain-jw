package com.eagle.ui.file;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

import com.eagle.util.StringUtil;

/**
 * 文件选择
 * @author zhangsai
 * @time 20091102
 */
public class FileList extends JTree implements ActionListener, TreeSelectionListener {
	private static final long serialVersionUID = 1L;

	private TreePath path;

	private FileNode node;

	private JDialog dg;

	private String sltPath = "";

	public FileList(JDialog dg) {
		this.dg = dg;
		setModel(createTreeModel());
		
		addTreeExpansionListener(new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent e) {
			}

			public void treeExpanded(TreeExpansionEvent e) {
				path = e.getPath();
				if (path != null) {
					node = (FileNode) path.getLastPathComponent();
					if (!node.isExplored()) {
						DefaultTreeModel model = (DefaultTreeModel) getModel();
						node.explore();
						model.nodeStructureChanged(node);
					}
				}
			}
		});
		this.setCellRenderer(new DefaultTreeCellRenderer() {
			private static final long serialVersionUID = 1L;

			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				TreePath tp = tree.getPathForRow(row);
				if (tp != null) {
					FileNode node = (FileNode) tp.getLastPathComponent();
					File f = node.getFile();
					try {
						Icon icon = FileSystemView.getFileSystemView().getSystemIcon(f);
						this.setIcon(icon);
						this.setLeafIcon(icon);
						this.setOpenIcon(icon);
						this.setClosedIcon(icon);
						this.setDisabledIcon(icon);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			}
		});
		super.setScrollsOnExpand(true);
		
		addListener();
	}

	private DefaultTreeModel createTreeModel() {
		File root = FileSystemView.getFileSystemView().getRoots()[0];
		FileNode rootNode = new FileNode(root);
		rootNode.explore();
		return new DefaultTreeModel(rootNode);
	}

	public JSplitPane createTreePanel() {
		JPanel pane = new JPanel();
		JButton button_1 = new JButton("确定");
		button_1.setActionCommand("submit");
		button_1.addActionListener(this);
		JButton button_2 = new JButton("取消");
		button_2.setActionCommand("cancel");
		button_2.addActionListener(this);
		pane.add(button_1);
		pane.add(button_2);

		JPanel treePanel = new JPanel();
		this.addTreeSelectionListener(this);
		treePanel.setLayout(new BorderLayout());
		treePanel.add(new JScrollPane(this), BorderLayout.CENTER);
		treePanel.setBorder(BorderFactory.createTitledBorder(""));
		treePanel.setPreferredSize(new Dimension(400, 400));
		JSplitPane splitPane;
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, treePanel, pane);
		return splitPane;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")) {
			sltPath = "";
			dg.setVisible(false);
			dg.dispose();
		} else if (e.getActionCommand().equals("submit")) {
			if(StringUtil.isEmpty(sltPath)){
				JOptionPane.showMessageDialog(dg, "请选择一个目录文件！");
				return;
			}
			dg.setVisible(false);
			dg.dispose();
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		path = e.getPath();
		if (path != null) {
			node = (FileNode) path.getLastPathComponent();
			if (!node.isExplored()) {
				DefaultTreeModel model = (DefaultTreeModel) getModel();
				node.explore();
				model.nodeStructureChanged(node);
			}
		}
		sltPath = node.getFile().getPath();
	}
	
	private void addListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	if(e.getClickCount() == 2) {
            		checkFile();
            	}
            }
        });
    }
	
	public String getPath(){
		return sltPath;
	}
	
	public void checkFile(){
		node = (FileNode)this.getLastSelectedPathComponent();
		if (!node.isExplored()) {
			DefaultTreeModel model = (DefaultTreeModel) getModel();
			node.explore();
			model.nodeStructureChanged(node);
		}
		
		sltPath = node.getFile().getPath();
		dg.setVisible(false);
		dg.dispose();
	}
}