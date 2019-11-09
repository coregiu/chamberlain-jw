package com.eagle.ui.demo;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.tree.*;

import com.eagle.data.ImageData;

@SuppressWarnings("serial")
public class TreeIconDemo extends JFrame {
	public TreeIconDemo() {
		super("AnimatedIconTreeExample");
		String[] strs = { "CARNIVORA", // 0
				"Felidae", // 1
				"Acinonyx jutatus (cheetah)", // 2
				"Panthera leo (lion)", // 3
				"Canidae", // 4
				"Canis lupus (wolf)", // 5
				"Lycaon pictus (lycaon)", // 6
				"Vulpes Vulpes (fox)" }; // 7
		IconNode[] nodes = new IconNode[strs.length];
		for (int i = 0; i < strs.length; i++) {
			nodes[i] = new IconNode(strs[i]);
		}
		nodes[0].add(nodes[1]);
		nodes[0].add(nodes[4]);
		nodes[1].add(nodes[2]);
		nodes[1].add(nodes[3]);
		nodes[4].add(nodes[5]);
		nodes[4].add(nodes[6]);
		nodes[4].add(nodes[7]);
		nodes[2].setIcon(ImageData.delete);
		nodes[3].setIcon(ImageData.delete);
		nodes[5].setIcon(ImageData.delete);
		nodes[6].setIcon(ImageData.delete);
		nodes[7].setIcon(ImageData.delete);
		JTree tree = new JTree(nodes[0]);
		tree.setCellRenderer(new IconNodeRenderer());
		//setImageObserver(tree, nodes);
		JScrollPane sp = new JScrollPane(tree);
		getContentPane().add(sp, BorderLayout.CENTER);
	}

	@SuppressWarnings("unused")
	private void setImageObserver(JTree tree, IconNode[] nodes) {
		for (int i = 0; i < nodes.length; i++) {
			ImageIcon icon = (ImageIcon) nodes[i].getIcon();
			if (icon != null) {
				icon.setImageObserver(new NodeImageObserver(tree, nodes[i]));
			}
		}
	}

	class NodeImageObserver implements ImageObserver {
		JTree tree;

		DefaultTreeModel model;

		TreeNode node;

		NodeImageObserver(JTree tree, TreeNode node) {
			this.tree = tree;
			this.model = (DefaultTreeModel) tree.getModel();
			this.node = node;
		}

		public boolean imageUpdate(Image img, int flags, int x, int y, int w,
				int h) {
			if ((flags & (FRAMEBITS | ALLBITS)) != 0) {
				TreePath path = new TreePath(model.getPathToRoot(node));
				Rectangle rect = tree.getPathBounds(path);
				if (rect != null) {
					tree.repaint(rect);
				}
			}
			return (flags & (ALLBITS | ABORT)) == 0;
		}
	}

	public static void main(String args[]) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.Swing.plaf.Windows.WindowsLookAndFeel");
		} catch (Exception evt) {
		}
		TreeIconDemo frame = new TreeIconDemo();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setSize(280, 200);
		frame.setVisible(true);
	}
}

@SuppressWarnings("serial")
class IconNodeRenderer extends DefaultTreeCellRenderer {
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		Icon icon = ((IconNode) value).getIcon();
		if (icon == null) {
			Hashtable icons = (Hashtable) tree.getClientProperty("title4.gif");
			String name = ((IconNode) value).getIconName();
			if ((icons != null) && (name != null)) {
				icon = (Icon) icons.get(name);
				if (icon != null) {
					setIcon(icon);
				}
			}
		} else {
			setIcon(icon);
		}
		return this;
	}
}

@SuppressWarnings("serial")
class IconNode extends DefaultMutableTreeNode {
	protected Icon icon;

	protected String iconName;

	public IconNode() {
		this(null);
	}

	public IconNode(Object userObject) {
		this(userObject, true, null);
	}

	public IconNode(Object userObject, boolean allowsChildren, Icon icon) {
		super(userObject, allowsChildren);
		this.icon = icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public Icon getIcon() {
		return icon;
	}

	public String getIconName() {
		if (iconName != null) {
			return iconName;
		} else {
			String str = userObject.toString();
			int index = str.lastIndexOf(".");
			if (index != -1) {
				return str.substring(++index);
			} else {
				return null;
			}
		}
	}

	public void setIconName(String name) {
		iconName = name;
	}
}
