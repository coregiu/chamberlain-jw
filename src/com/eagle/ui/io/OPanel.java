package com.eagle.ui.io;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.apache.commons.logging.*;
import org.jfree.ui.ExtensionFileFilter;

import com.eagle.action.ExcelAction;
import com.eagle.action.IOConfAction;
import com.eagle.data.ConfBean;
import com.eagle.data.IODataBean;
import com.eagle.data.ImageData;
import com.eagle.util.*;

public class OPanel implements ActionListener, ListSelectionListener{
	private static final long serialVersionUID = -5065631024639830299L;
	private Log log = LogFactory.getLog(OPanel.class);
	private JTable table;
	private IOConfAction ioAction;
	private JFrame parentFrame;
	private Vector ioDataVec = new Vector();
	private JTextField filterData = new JTextField("", 10);
	private TableRowSorter sorter;
	public OPanel(JFrame f){
		parentFrame = f;
		ioAction = new IOConfAction();
	}
	@SuppressWarnings("unchecked")
	public JPanel getOConfPanel(){
		JPanel panelTable=new JPanel();
        JPanel panelButton=new JPanel(new GridLayout(2,4,1,1));
        JPanel panel_1=new JPanel();
        JPanel panel_2=new JPanel();
        JButton button_1=new JButton("添加");
        button_1.addActionListener(this);
        button_1.setActionCommand("add");
        button_1.setIcon(ImageData.add);
        JButton button_2=new JButton("修改");
        button_2.addActionListener(this);
        button_2.setActionCommand("update");
        button_2.setIcon(ImageData.update);
        JButton button_3=new JButton("删除");
        button_3.addActionListener(this);
        button_3.setActionCommand("delete");
        button_3.setIcon(ImageData.deleteUser);
        JButton button_4=new JButton("查询");
        button_4.addActionListener(this);
        button_4.setActionCommand("view");
        button_4.setIcon(ImageData.search);
        JButton button_5=new JButton("导出EXCEL");
        button_5.addActionListener(this);
        button_5.setActionCommand("excel");
        button_5.setIcon(ImageData.exportData);

        panel_2.add(filterData);
        panel_2.add(button_4);
        panel_2.add(button_1);
        panel_2.add(button_2);
        panel_2.add(button_3);
        panel_2.add(button_5);
        panel_1.add(new JLabel(""));//  显示选中的列信息
        panelButton.add(panel_1);
        panelButton.add(panel_2);
       
        DefaultTableModel model = new DefaultTableModel(ioAction.getOData(), ioAction.getOTHead());
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

        table.setPreferredScrollableViewportSize(new Dimension(680,370));
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//实现左右滚动
        table.setCellSelectionEnabled(true);
        //table.setAutoCreateRowSorter(true);
        ListSelectionModel  selectionMode=table.getSelectionModel();//取得table的ListSelectionModel.
        selectionMode.addListSelectionListener((ListSelectionListener) this);
        TableUtil.paintRow(table);
        JScrollPane scrollPane1 = new JScrollPane(table);
        JSplitPane splitPane= new JSplitPane(JSplitPane.VERTICAL_SPLIT,true, scrollPane1, panel_2);
        panelTable.add(splitPane,BorderLayout.CENTER);
        return panelTable;
	}
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent event){
		if("add".equals(event.getActionCommand())){// 添加
			new OAddInfoDialog(parentFrame);
		}else if("update".equals(event.getActionCommand())){// 修改
			if(ioDataVec.size()==0){
				JOptionPane.showMessageDialog(parentFrame, "请选择要修改的数据");
				return ;
			}
			IODataBean ioDataEditBean = (IODataBean)ioDataVec.get(0);
			new OUpdateInfoDialog(parentFrame, ioDataEditBean);
		}else if("delete".equals(event.getActionCommand())){// 删除 
			if(ioDataVec.size()==0){
				JOptionPane.showMessageDialog(parentFrame, "请选择要删除的数据");
				return ;
			}
			if(JOptionPane.showConfirmDialog(parentFrame, "确定要删除此数据吗？", "删除提示", 0)==0){
				IODataBean ioDataDeleteBean;
				for(int i=0;i<ioDataVec.size();i++){
					ioDataDeleteBean = (IODataBean)ioDataVec.get(i);
					ioAction.deleteOInfo(ioDataDeleteBean);
				}
				ConfBean.loadIOInfo();
			}
		}else if("view".equals(event.getActionCommand())){// 查询
			String text = filterData.getText();
			if (text.length() == 0){
				sorter.setRowFilter(null);
			}else{
				sorter.setRowFilter(RowFilter.regexFilter(text));
			}
		}else if("excel".equals(event.getActionCommand())){
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
                ExcelAction.grt2Excel("支出数据明详", filename, ioAction.getOTHead(), ioAction.getOData());
	        }
		}
		ioDataVec.removeAllElements();
		refreshTable();
		TableUtil.paintRow(table);
	}
	@SuppressWarnings("unchecked")
	public void valueChanged(ListSelectionEvent el){
		try{
			ioDataVec.removeAllElements();
            //int columnsCount=table.getColumnCount();
            int[] rows=table.getSelectedRows();
            
            for(int i=0;i<rows.length;i++){
            	IODataBean ioDataDeleteBean = new IODataBean();
            	ioDataDeleteBean.setOId(table.getValueAt(rows[i],0).toString().trim());
            	ioDataDeleteBean.setOName(table.getValueAt(rows[i],1).toString().trim());
            	ioDataDeleteBean.setOMoney(table.getValueAt(rows[i],2).toString().trim());
            	ioDataDeleteBean.setOType(table.getValueAt(rows[i],3).toString().trim());
            	ioDataDeleteBean.setOResume(table.getValueAt(rows[i],4).toString().trim());
            	ioDataDeleteBean.setODate(table.getValueAt(rows[i],5).toString().trim());
            	ioDataVec.add(ioDataDeleteBean);
            }
        }catch(Exception e){
           log.error("ArrayError:"+e.getMessage());
        }
	}
	/**
	 * 刷新表格
	 * */
	private void refreshTable(){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setDataVector(ioAction.getOData(), ioAction.getOTHead());
		model.fireTableDataChanged();
	}
}