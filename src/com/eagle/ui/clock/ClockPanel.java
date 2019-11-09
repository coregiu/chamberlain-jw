package com.eagle.ui.clock;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.eagle.action.ClockAction;
import com.eagle.data.ClockData;
import com.eagle.data.ImageData;
import com.eagle.util.*;

/**
 * 闹钟面板
 * 报文格式
 * <Extension>
 * 		<clock>
 * 			<aClock date="2010-10-20" time="0800">
 * 				<remindType/>
 * 				<isRepeat/>
 * 				<remindContent/>
 * 				<isValid/>
 * 			</aClock>
 * 			............
 * 		</clock>
 * </Extension>
 * 
 * @author szhang
 *
 */

public class ClockPanel implements ActionListener, ListSelectionListener{
	private static final long serialVersionUID = -5065631024639830299L;
	private Log log = LogFactory.getLog(ClockPanel.class);
	private JTable table;
	private ClockAction clkAction;
	private JFrame parentFrame;
	private Vector clkDataVec = new Vector();
	private JTextField filterData = new JTextField("", 10);
	private TableRowSorter sorter;
	public ClockPanel(JFrame f, ClockAction clkAction){
		parentFrame = f;
		this.clkAction = clkAction;
	}
	@SuppressWarnings("unchecked")
	public JPanel getClkPanel(){
		JPanel panelTable = new JPanel();
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
        button_4.setIcon(ImageData.search);

        panel_2.add(filterData);
        panel_2.add(button_4);
        panel_2.add(button_1);
        panel_2.add(button_2);
        panel_2.add(button_3);
        panel_1.add(new JLabel(""));//  显示选中的列信息
        panelButton.add(panel_1);
        panelButton.add(panel_2);
       
        DefaultTableModel model = new DefaultTableModel(clkAction.getClkData(), clkAction.getClkTHead());
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
        JSplitPane splitPane= new JSplitPane(JSplitPane.VERTICAL_SPLIT,true, scrollPane1,panel_2);
        panelTable.add(splitPane,BorderLayout.CENTER);
        return panelTable;
	}
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent event){
		if("add".equals(event.getActionCommand())){// 添加
			new ClockAddDialog(parentFrame);
			clkAction.restart();
		}else if("update".equals(event.getActionCommand())){// 修改
			if(clkDataVec.size()==0){
				JOptionPane.showMessageDialog(parentFrame, "请选择要修改的数据");
				return ;
			}
			ClockData clkData = (ClockData)clkDataVec.get(0);
			new ClockUpdateDialog(parentFrame, clkData);
			clkAction.restart();
		}else if("delete".equals(event.getActionCommand())){// 删除 
			if(clkDataVec.size()==0){
				JOptionPane.showMessageDialog(parentFrame, "请选择要删除的数据");
				return ;
			}
			if(JOptionPane.showConfirmDialog(parentFrame, "确定要删除此数据吗？", "删除提示", 0)==0){
				ClockData clockData;
				for(int i=0;i<clkDataVec.size();i++){
					clockData = (ClockData)clkDataVec.get(i);
					clkAction.deleteIInfo(clockData);
				}
				clkAction.restart();
			}
		}else if("view".equals(event.getActionCommand())){// 查询
			String text = filterData.getText();
			if (text.length() == 0){
				sorter.setRowFilter(null);
			}else{
				sorter.setRowFilter(RowFilter.regexFilter(text));
			}
		}
		
		clkDataVec.removeAllElements();
		refreshTable();
		TableUtil.paintRow(table);
	}
	@SuppressWarnings("unchecked")
	public void valueChanged(ListSelectionEvent el){
		try{
			clkDataVec.removeAllElements();
            //int columnsCount=table.getColumnCount();
            int[] rows=table.getSelectedRows();
            
            for(int i=0;i<rows.length;i++){
            	ClockData clockData = new ClockData();
            	clockData.setRemindTime(table.getValueAt(rows[i],0).toString().trim());
            	clockData.setRemindType(table.getValueAt(rows[i],1).toString().trim());
            	clockData.setIsRepeat(table.getValueAt(rows[i],2).toString().trim());
            	clockData.setRemindContent(table.getValueAt(rows[i],3).toString().trim());
            	clockData.setIsValid(table.getValueAt(rows[i],4).toString().trim());
            	
            	clkDataVec.add(clockData);
            }
        }catch(Exception e){
        	log.error(e);
        }
	}
	/**
	 * 刷新表格
	 * */
	private void refreshTable(){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setDataVector(clkAction.getClkData(), clkAction.getClkTHead());
		model.fireTableDataChanged();
	}
}
