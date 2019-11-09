package com.eagle.ui.plain;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.eagle.data.ImageData;

class IconNodeRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		Icon icon = ((IconNode) value).getIcon();
		if (icon == null) {
			icon = ImageData.completed;
		} else {
			setIcon(icon);
		}
		return this;
	}
}
