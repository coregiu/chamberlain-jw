package com.eagle.ui.demo;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.print.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;

public class PrintDemo implements Printable {
	JFrame frame;

	JTable tableView;

	public PrintDemo() {
		frame = new JFrame("Sales Report");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		final String[] headers = { "Description", "open price", "latest price",
				"End Date", "Quantity" };
		final Object[][] data = {
				{ "Box of Biros", "1.00", "4.99", new Date(), new Integer(2) },
				{ "Blue Biro", "0.10", "0.14", new Date(), new Integer(1) },
				{ "legal pad", "1.00", "2.49", new Date(), new Integer(1) },
				{ "tape", "1.00", "1.49", new Date(), new Integer(1) },
				{ "stapler", "4.00", "4.49", new Date(), new Integer(1) },
				{ "legal pad", "1.00", "2.29", new Date(), new Integer(5) } };

		TableModel dataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return headers.length;
			}

			public int getRowCount() {
				return data.length;
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			public String getColumnName(int column) {
				return headers[column];
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				return getValueAt(0, col).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return (col == 1);
			}

			public void setValueAt(Object aValue, int row, int column) {
				data[row][column] = aValue;
			}
		};

		tableView = new JTable(dataModel);
		JScrollPane scrollpane = new JScrollPane(tableView);

		scrollpane.setPreferredSize(new Dimension(500, 80));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(BorderLayout.CENTER, scrollpane);
		frame.pack();
		JButton printButton = new JButton();

		printButton.setText("print me!");

		frame.getContentPane().add(BorderLayout.SOUTH, printButton);

		// for faster printing turn double buffering off

		RepaintManager.currentManager(frame).setDoubleBufferingEnabled(false);

		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				PrinterJob pj = PrinterJob.getPrinterJob();
				pj.setPrintable(PrintDemo.this);
				pj.printDialog();
				try {
					pj.print();
				} catch (Exception PrintException) {
				}
			}
		});

		frame.setVisible(true);
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		int fontHeight = g2.getFontMetrics().getHeight();
		int fontDesent = g2.getFontMetrics().getDescent();

		// leave room for page number
		double pageHeight = pageFormat.getImageableHeight() - fontHeight;
		double pageWidth = pageFormat.getImageableWidth();
		double tableWidth = (double) tableView.getColumnModel()
				.getTotalColumnWidth();
		double scale = 1;
		if (tableWidth >= pageWidth) {
			scale = pageWidth / tableWidth;
		}

		double headerHeightOnPage = tableView.getTableHeader().getHeight()
				* scale;
		double tableWidthOnPage = tableWidth * scale;

		double oneRowHeight = (tableView.getRowHeight() + tableView
				.getRowMargin())
				* scale;
		int numRowsOnAPage = (int) ((pageHeight - headerHeightOnPage) / oneRowHeight);
		double pageHeightForTable = oneRowHeight * numRowsOnAPage;
		int totalNumPages = (int) Math.ceil(((double) tableView.getRowCount())
				/ numRowsOnAPage);
		if (pageIndex >= totalNumPages) {
			return NO_SUCH_PAGE;
		}

		g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		// bottom center
		g2.drawString("Page: " + (pageIndex + 1), (int) pageWidth / 2 - 35,
				(int) (pageHeight + fontHeight - fontDesent));

		g2.translate(0f, headerHeightOnPage);
		g2.translate(0f, -pageIndex * pageHeightForTable);

		// If this piece of the table is smaller
		// than the size available,
		// clip to the appropriate bounds.
		if (pageIndex + 1 == totalNumPages) {
			int lastRowPrinted = numRowsOnAPage * pageIndex;
			int numRowsLeft = tableView.getRowCount() - lastRowPrinted;
			g2.setClip(0, (int) (pageHeightForTable * pageIndex), (int) Math
					.ceil(tableWidthOnPage), (int) Math.ceil(oneRowHeight
					* numRowsLeft));
		}
		// else clip to the entire area available.
		else {
			g2.setClip(0, (int) (pageHeightForTable * pageIndex), (int) Math
					.ceil(tableWidthOnPage), (int) Math
					.ceil(pageHeightForTable));
		}

		g2.scale(scale, scale);
		tableView.paint(g2);
		g2.scale(1 / scale, 1 / scale);
		g2.translate(0f, pageIndex * pageHeightForTable);
		g2.translate(0f, -headerHeightOnPage);
		g2.setClip(0, 0, (int) Math.ceil(tableWidthOnPage), (int) Math
				.ceil(headerHeightOnPage));
		g2.scale(scale, scale);
		tableView.getTableHeader().paint(g2);
		// paint header at top

		return Printable.PAGE_EXISTS;
	}

	public static void main(String[] args) {
		new PrintDemo();
	}
}
