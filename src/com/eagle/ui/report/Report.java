package com.eagle.ui.report;

import java.awt.event.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.experimental.chart.plot.CombinedXYPlot;
import org.jfree.ui.ExtensionFileFilter;

import com.eagle.action.ExcelAction;
import com.eagle.action.ReportAction;
import com.eagle.data.ImageData;
import com.eagle.data.ViewTimeBean;
import com.eagle.util.*;

public class Report implements ActionListener{
	private static final long serialVersionUID = -5065631024639830299L;
	private ReportAction reportAction;
	private JScrollPane scrollPane1;
	private JFrame parentFrame;
	private JPanel panelTable;
	private ViewTimeBean vd;
	/**
	 * 
	 * @param f
	 * @param vd
	 * @param statisType 统计对象 i-收入分析, o-支出分析,a-收支分析
	 */
	public Report(JFrame f, ViewTimeBean vd, char statisType){
		parentFrame = f;
		this.vd = vd;
		reportAction = new ReportAction(vd, statisType);
	}
	
	/**
	 * 构造显示界面
	 * @param flag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JPanel getAnalysisPanel(char viewType){
		panelTable = new JPanel();
        JPanel panelButton=new JPanel(new GridLayout(2,4,1,1));
        JPanel panel_1=new JPanel();
        JPanel panel_2=new JPanel();
        JButton button_1=new JButton("图形分析");
        button_1.addActionListener(this);
        button_1.setActionCommand("img");
        button_1.setIcon(ImageData.oReport);
        JButton button_2=new JButton("报表分析");
        button_2.addActionListener(this);
        button_2.setActionCommand("rep");
        button_2.setIcon(ImageData.ioReport);
        JButton button_3=new JButton("导出EXCEL");
        button_3.addActionListener(this);
        button_3.setActionCommand("excel");
        button_3.setIcon(ImageData.exportData);

        panel_2.add(button_1);
        panel_2.add(button_2);
        panel_2.add(button_3);
        panel_1.add(new JLabel(""));//  显示选中的列信息
        panelButton.add(panel_1);
        panelButton.add(panel_2);
        
        if(viewType=='1'){
        	if("类型".equals(vd.getDtType())){
        		scrollPane1 = new JScrollPane(getImgRoundPanel());
        	}else if("时间".equals(vd.getDtType())){
        		scrollPane1 = new JScrollPane(getImgPanel());
        	}else{
        		scrollPane1 = new JScrollPane(getTablePanel());
        	}
        }else{
        	scrollPane1 = new JScrollPane(getTablePanel());
        }
        JSplitPane splitPane= new JSplitPane(JSplitPane.VERTICAL_SPLIT,true, scrollPane1,panel_2);
        panelTable.add(splitPane,BorderLayout.CENTER);
        return panelTable;
	}
	
	/**
	 * 构造表格
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JComponent getTablePanel(){
		DefaultTableModel model;
		while(reportAction.getTHead().size()==0){
			
		}
		model = new DefaultTableModel(reportAction.getTData(), reportAction.getTHead());
		JTable table=new JTable(model);
		TableRowSorter sorter = new TableRowSorter(model);
		table.setRowSorter(sorter);


        table.setPreferredScrollableViewportSize(new Dimension(680,371));
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//实现左右滚动
        table.setCellSelectionEnabled(true);
        //table.setAutoCreateRowSorter(true);
        TableUtil.paintRow(table);
		return table;
	}
	
	/**
	 * 构造曲线、柱状图
	 * @return
	 */
	public JComponent getImgPanel(){
		TimeSeries combineData[] = reportAction.getCombineData(); 
		JFreeChart chart = createCombinedChart(combineData);
		JPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new java.awt.Dimension(680, 390));
		return panel;
	}
	
	/**
	 * 画图
	 * @param combineData
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private JFreeChart createCombinedChart(TimeSeries combineData[]) {
		// 1 设置数据
		TimeSeriesCollection dataSet = new TimeSeriesCollection();
		for(TimeSeries aCombineData: combineData){
			dataSet.addSeries(aCombineData);
		}
		// 2 画曲线
		XYSplineRenderer renderer1 = new XYSplineRenderer();
		renderer1.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("yyyy-MM-dd"), new DecimalFormat("0.00")));
		renderer1.setSeriesStroke(0, new BasicStroke(4.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		renderer1.setSeriesPaint(0, Color.red);
		renderer1.setSeriesPaint(1, Color.blue);
		DateAxis domainAxis = new DateAxis("时间");
		domainAxis.setLowerMargin(0.0);
		domainAxis.setUpperMargin(0.02);
		ValueAxis rangeAxis = new NumberAxis("RMB");
		XYPlot plot1 = new XYPlot(dataSet, null, rangeAxis, renderer1);
		plot1.setBackgroundPaint(Color.decode("#f3fff7"));
		plot1.setDomainGridlinePaint(Color.decode("#c7c503"));
		plot1.setRangeGridlinePaint(Color.decode("#c7c503"));

		// 3 画柱状图
		// 3.1 第一种画法
		XYBarRenderer renderer2 = new XYBarRenderer();
		renderer2.setSeriesPaint(0, Color.red);
		renderer2.setSeriesPaint(1, Color.blue);
		renderer2.setDrawBarOutline(false);
		renderer2.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("yyyy-MMM-dd"), new DecimalFormat("0.00")));
		renderer2.setShadowVisible(false);
		
		XYPlot plot2 = new XYPlot(dataSet, null, new NumberAxis("RMB"), renderer2);
		plot2.setBackgroundPaint(Color.decode("#f3fff7"));
		plot2.setDomainGridlinePaint(Color.decode("#c7c503"));
		plot2.setRangeGridlinePaint(Color.decode("#c7c503"));
		
		// 3.2 第二种画法
		/*CombinedRangeXYPlot plot2 = new CombinedRangeXYPlot(new NumberAxis("Value"));
		for(TimeSeries aCombineData: combineData){
			XYItemRenderer barRender = new XYBarRenderer(0.20);
			barRender.setToolTipGenerator(
					new StandardXYToolTipGenerator(
							StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT, new SimpleDateFormat("yyyy-MM-dd"), new DecimalFormat("0,000.0")
			     )
			);
			XYPlot subplot = new XYPlot(new TimeSeriesCollection(aCombineData), new DateAxis("Date"), null, barRender);
			plot2.add(subplot, 1);
		}		*/

		CombinedXYPlot cplot = new CombinedXYPlot(domainAxis, rangeAxis);
		cplot.add(plot1, 3);
		cplot.add(plot2, 2);
		cplot.setGap(8.0);
		cplot.setDomainGridlinePaint(Color.white);
		cplot.setDomainGridlinesVisible(true);

		// return a new chart containing the overlaid plot...
		JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, cplot, false);
		chart.setBackgroundPaint(Color.decode("#efede4"));
		LegendTitle legend = new LegendTitle(cplot);
		chart.addSubtitle(legend);
		return chart;
	}
	
	/**
	 * 按分类统计用饼状图显示
	 * @return
	 */
	private JComponent getImgRoundPanel(){
		DefaultPieDataset data = reportAction.getPieDataset(); 

        PiePlot3D plot = new PiePlot3D(data);//生成一个3D饼图 
        //plot.setURLGenerator(new StandardPieURLGenerator("DegreedView.jsp"));//设定图片链接 
        // 设置显示百分比
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0} {2}",
                NumberFormat.getNumberInstance(),
                new DecimalFormat("0.00%")));

        JFreeChart chart = new JFreeChart("",JFreeChart.DEFAULT_TITLE_FONT, plot, true); 
        chart.setBackgroundPaint(Color.decode("#f3fff7"));//可选，设置图片背景色 
        
        JPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new java.awt.Dimension(680, 390));
        return panel;
	}
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent event){
		if("img".equals(event.getActionCommand())){
			Container container = parentFrame.getContentPane();
			container.removeAll();
            container.add(getAnalysisPanel('1'), BorderLayout.CENTER);
            parentFrame.setContentPane(container);
            parentFrame.invalidate();
		}else if("rep".equals(event.getActionCommand())){
			Container container = parentFrame.getContentPane();
			container.removeAll();
            container.add(getAnalysisPanel('0'), BorderLayout.CENTER);
            parentFrame.setContentPane(container);
            parentFrame.invalidate();
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
                ExcelAction.grt2Excel("统计数据明详", filename, reportAction.getTHead(), reportAction.getTData());
	        }
		}
	}
}
