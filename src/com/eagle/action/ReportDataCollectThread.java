package com.eagle.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.eagle.util.StringUtil;
import com.eagle.xmlparse.*;

import org.apache.log4j.Logger;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.*;

import com.eagle.data.ViewTimeBean;

/**
 * 报表数据收集
 * 
 * @author szhang
 *
 */
public class ReportDataCollectThread extends Thread implements Runnable {
	private Logger log = Logger.getLogger(ReportDataCollectThread.class);
	
	// 报表表头
	protected Vector<String> tHead = new Vector<String>();
	// 报表表体
	protected Vector<Vector> tData = new Vector<Vector>();
	// 曲线图数据 样式： series1.add(new Month(1, 2005), 7627.743);  按时间统计时用
	protected TimeSeries inData = new TimeSeries("收入");
	protected TimeSeries outData = new TimeSeries("支出");
	// 饼图数据 样式：pieDataset.setValue("高中以下",380);   按类型统计时用
	protected DefaultPieDataset pieDataset = new DefaultPieDataset();
	
	private Thread reportThread=null;
	private ViewTimeBean vd;
	private char type;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Calendar cal = Calendar.getInstance();
	/**
	 * 构造函数
	 * @param vd   统计条件
	 * @param type 统计对象 i-收入分析, o-支出分析,a-收支分析
	 */
	public ReportDataCollectThread(ViewTimeBean vd, char type){
		this.vd = vd;
		this.type = type;
		if(reportThread == null){
			reportThread = new Thread (this);
			reportThread.start();
		}
	}
	
	public void run(){
		while(Thread.currentThread()==reportThread){
			setReportData();
		}
	}
	
	/**
	 * 开始计算数据
	 */
	private void setReportData(){
		try{
			Vector<Vector> iVc = XMLParser.getIODataAsVec('i');
			Vector<Vector> oVc = XMLParser.getIODataAsVec('o');
			vd.setBgCal(df.parse(vd.getBgTime()));
			vd.setEdCal(df.parse(vd.getEdTime()));
			// 1 收支分析只有统计
			// 时间段内，统计方式， 统计时段长
			// 时间 收入 支出
			if(type=='a'){
				setAllDataByTime(iVc, oVc);
			}
			// 2 收入 分详细和统计两类
			else if(type=='i'){
				// 2.1 详细 （时间 类型 金额，最后合计）
				if(vd.getType()=='0'){
					vd.setDpType("日");
					setIDataByTime(iVc);
				}
				// 2.2 统计
				
				// 2.2.1 按时间统计（时间 金额）
				else if(vd.getType()=='1'&&"时间".equals(vd.getDtType())){
					setIDataByTime(iVc);
				}
				// 2.2.2 按类型统计（类型 金额）
				else if(vd.getType()=='1'&&"类型".equals(vd.getDtType())){
					setIDataByType(iVc);
				}
				// 2.2.3 按时间类型统计（时间 类型 金额，只有表没有图）
				else{
					setIDataByTimeAndType(iVc);
				}
			}
			// 3 支出 分详细和统计两类
			else if(type=='o'){
				// 3.1 详细 （时间 类型 金额，最后合计）
				if(vd.getType()=='0'){
					vd.setDpType("日");
					setODataByTime(oVc);
				}
				// 3.2 统计
				
				// 3.2.1 按时间统计（时间 金额）
				else if(vd.getType()=='1'&&"时间".equals(vd.getDtType())){
					setODataByTime(oVc);
				}
				// 3.2.2 按类型统计（类型 金额）
				else if(vd.getType()=='1'&&"类型".equals(vd.getDtType())){
					setODataByType(oVc);
				}
				// 3.2.3 按时间类型统计（时间 类型 金额，只有表没有图）
				else{
					setODataByTimeAndType(oVc);
				}
			}
		}catch(Exception e){
			log.error(e);
		}finally{
			finishTask();
		}
	}
	
	private void finishTask(){
		reportThread = null;
	}
	/**
	 * 按时间统计收入数据
	 * @param iVc
	 */
	private void setIDataByTime(Vector<Vector> iVc) throws Exception{
		// 1 遍历收入数据，存储于HASHTABLE
		Hashtable<String, Object[]> dataHash = new Hashtable<String, Object[]>();
		String dateStr;
		Double moneyDbl;
		Double moneySum=0.0;
		Double moneyTmp;
		String key;
		Object[] dataObj;
		for(Vector aVc: iVc){
			// 时间
			dateStr = (String)aVc.get(6);
			// 金额
			moneyDbl = Double.parseDouble((String)aVc.get(3));
			// 生成标识符
			key = createKey(dateStr,"");
			if(key==null||"".equals(key)){
				continue;
			}
			//  计算总额
			moneySum+= moneyDbl;
			// 存储到HASHTABLE中
			dataObj = dataHash.get(key);
			if(dataObj==null){
				dataObj = new Object[2];
				dataObj[0] = moneyDbl;
				dataObj[1] = createTimePeriod(dateStr);
			}else{
				moneyTmp = (Double)dataObj[0];
				moneyTmp+=moneyDbl;
				dataObj[0] = moneyTmp;
			}
			dataHash.put(key, dataObj);
		}
		// 2 设置表头
		tHead.add("时间");
		tHead.add("金额");
		// 3 设置表数据和曲线数据
		Enumeration<String> en = dataHash.keys();
		Vector<Object> itemVc;
		while(en.hasMoreElements()){
			key = en.nextElement();
			// 表数据
			itemVc = new Vector<Object>();
			itemVc.add(key);
			itemVc.add(dataHash.get(key)[0]);
			tData.add(itemVc);
			// 曲线数据
			inData.add((RegularTimePeriod)dataHash.get(key)[1], (Double)dataHash.get(key)[0]);
		}
		itemVc = new Vector<Object>();
		itemVc.add("合计");
		itemVc.add(moneySum);
		tData.add(itemVc);
	}
	
	/**
	 * 按时间统计支出数据
	 * @param iVc
	 */
	private void setODataByTime(Vector<Vector> oVc)throws Exception{
		// 遍历收入数据，存储于HASHTABLE
		Hashtable<String, Object[]> dataHash = new Hashtable<String, Object[]>();
		String dateStr;
		Double moneyDbl;
		Double moneySum=0.0;
		Double moneyTmp;
		String key;
		Object[] dataObj;
		for(Vector aVc: oVc){
			// 时间
			dateStr = (String)aVc.get(5);
			// 金额
			moneyDbl = Double.parseDouble((String)aVc.get(2));
			// 生成标识符
			key = createKey(dateStr,"");
			if(key==null||"".equals(key)){
				continue;
			}
			// 计算总额
			moneySum+= moneyDbl;
			// 存储到HASHTABLE中
			dataObj = dataHash.get(key);
			if(dataObj==null){
				dataObj = new Object[2];
				dataObj[0] = moneyDbl;
				dataObj[1] = createTimePeriod(dateStr);
			}else{
				moneyTmp = (Double)dataObj[0];
				moneyTmp+=moneyDbl;
				dataObj[0] = moneyTmp;
			}
			dataHash.put(key, dataObj);
		}
		// 2 设置表头
		tHead.add("时间");
		tHead.add("金额");
		// 3 设置表数据和曲线数据
		Enumeration<String> en = dataHash.keys();
		Vector<Object> itemVc;
		while(en.hasMoreElements()){
			key = en.nextElement();
			// 表数据
			itemVc = new Vector<Object>();
			itemVc.add(key);
			itemVc.add(dataHash.get(key)[0]);
			tData.add(itemVc);
			// 曲线数据
			outData.add((RegularTimePeriod)dataHash.get(key)[1], (Double)dataHash.get(key)[0]);
		}
		itemVc = new Vector<Object>();
		itemVc.add("合计");
		itemVc.add(moneySum);
		tData.add(itemVc);
	}
	
	/**
	 * 按类型统计收入数据
	 * @param iVc
	 */
	private void setIDataByType(Vector<Vector> iVc)throws Exception{
		// 1 遍历收入数据，存储于HASHTABLE
		Hashtable<String, Double> dataHash = new Hashtable<String, Double>();
		String dateStr;
		String typeStr;
		Double moneyDbl;
		Double moneySum=0.0;
		Double moneyTmp;
		String key;
		for(Vector aVc: iVc){
			// 时间
			dateStr = (String)aVc.get(6);
			// 类型
			typeStr = (String)aVc.get(4);
			// 金额
			moneyDbl = Double.parseDouble((String)aVc.get(3));
			// 生成标识符
			key = createKey(dateStr, typeStr);
			if(key==null||"".equals(key)){
				continue;
			}
			// 计算总额
			moneySum+= moneyDbl;
			// 存储到HASHTABLE中
			moneyTmp = dataHash.get(key);
			if(moneyTmp==null){
				moneyTmp = moneyDbl;
			}else{
				moneyTmp+=moneyDbl;
			}
			dataHash.put(key, moneyTmp);
		}
		// 2 设置表头
		tHead.add("类型");
		tHead.add("金额");
		// 3 设置表数据和曲线数据
		Enumeration<String> en = dataHash.keys();
		Vector<Object> itemVc;
		while(en.hasMoreElements()){
			key = en.nextElement();
			// 表数据
			itemVc = new Vector<Object>();
			itemVc.add(key);
			itemVc.add(dataHash.get(key));
			tData.add(itemVc);
			// 曲线数据
			pieDataset.setValue(key, (Double)dataHash.get(key));
		}
		itemVc = new Vector<Object>();
		itemVc.add("合计");
		itemVc.add(moneySum);
		tData.add(itemVc);
	}
	
	/**
	 * 按类型统计支出数据
	 * @param iVc
	 */
	private void setODataByType(Vector<Vector> oVc)throws Exception{
		// 1 遍历收入数据，存储于HASHTABLE
		Hashtable<String, Double> dataHash = new Hashtable<String, Double>();
		String dateStr;
		String typeStr;
		Double moneyDbl;
		Double moneySum=0.0;
		Double moneyTmp;
		String key;
		for(Vector aVc: oVc){
			// 时间
			dateStr = (String)aVc.get(5);
			// 类型
			typeStr = (String)aVc.get(3);
			// 金额
			moneyDbl = Double.parseDouble((String)aVc.get(2));
			// 生成标识符
			key = createKey(dateStr, typeStr);
			if(key==null||"".equals(key)){
				continue;
			}
			// 计算总额
			moneySum+= moneyDbl;
			// 存储到HASHTABLE中
			moneyTmp = dataHash.get(key);
			if(moneyTmp==null){
				moneyTmp = moneyDbl;
			}else{
				moneyTmp+=moneyDbl;
			}
			dataHash.put(key, moneyTmp);
		}
		// 2 设置表头
		tHead.add("类型");
		tHead.add("金额");
		// 3 设置表数据和曲线数据
		Enumeration<String> en = dataHash.keys();
		Vector<Object> itemVc;
		while(en.hasMoreElements()){
			key = en.nextElement();
			// 表数据
			itemVc = new Vector<Object>();
			itemVc.add(key);
			itemVc.add(dataHash.get(key));
			tData.add(itemVc);
			// 曲线数据
			pieDataset.setValue(key, (Double)dataHash.get(key));
		}
		itemVc = new Vector<Object>();
		itemVc.add("合计");
		itemVc.add(moneySum);
		tData.add(itemVc);
	}
	/**
	 * 按时间和类型统计收入数据
	 * @param iVc
	 */
	private void setIDataByTimeAndType(Vector<Vector> iVc)throws Exception{
		// 1 遍历收入数据，存储于HASHTABLE
		Hashtable<String, Double> dataHash = new Hashtable<String, Double>();
		String dateStr;
		String typeStr;
		Double moneyDbl;
		Double moneySum=0.0;
		Double moneyTmp;
		String key;
		for(Vector aVc: iVc){
			// 时间
			dateStr = (String)aVc.get(6);
			// 类型
			typeStr = (String)aVc.get(4);
			// 金额
			moneyDbl = Double.parseDouble((String)aVc.get(3));
			// 生成标识符
			key = createKey(dateStr, typeStr);
			if(key==null||"".equals(key)){
				continue;
			}
			// 计算总额
			moneySum+= moneyDbl;
			// 存储到HASHTABLE中
			moneyTmp = dataHash.get(key);
			if(moneyTmp==null){
				moneyTmp = moneyDbl;
			}else{
				moneyTmp+= moneyDbl;;
			}
			dataHash.put(key, moneyTmp);
		}
		// 2 设置表头
		tHead.add("时间");
		tHead.add("类型");
		tHead.add("金额");
		// 3 设置表数据和曲线数据
		Enumeration<String> en = dataHash.keys();
		Vector<Object> itemVc;
		String dataArr[];
		while(en.hasMoreElements()){
			key = en.nextElement();
			// 表数据
			itemVc = new Vector<Object>();
			dataArr = key.split(",");
			itemVc.add(dataArr[0]);
			itemVc.add(dataArr[1]);
			itemVc.add(dataHash.get(key));
			tData.add(itemVc);
		}
		itemVc = new Vector<Object>();
		itemVc.add("合计");
		itemVc.add(" ");
		itemVc.add(moneySum);
		tData.add(itemVc);
	}
	
	/**
	 * 按时间和类型统计支出数据
	 * @param iVc
	 */
	private void setODataByTimeAndType(Vector<Vector> oVc)throws Exception{
		// 1 遍历收入数据，存储于HASHTABLE
		Hashtable<String, Double> dataHash = new Hashtable<String, Double>();
		String dateStr;
		String typeStr;
		Double moneyDbl;
		Double moneySum=0.0;
		Double moneyTmp;
		String key;
		for(Vector aVc: oVc){
			// 时间
			dateStr = (String)aVc.get(5);
			// 类型
			typeStr = (String)aVc.get(3);
			// 金额
			moneyDbl = Double.parseDouble((String)aVc.get(2));
			// 生成标识符
			key = createKey(dateStr, typeStr);
			if(key==null||"".equals(key)){
				continue;
			}
			// 计算总额
			moneySum+= moneyDbl;
			// 存储到HASHTABLE中
			moneyTmp = dataHash.get(key);
			if(moneyTmp==null){
				moneyTmp = moneyDbl;
			}else{
				moneyTmp+= moneyDbl;;
			}
			dataHash.put(key, moneyTmp);
		}
		// 2 设置表头
		tHead.add("时间");
		tHead.add("类型");
		tHead.add("金额");
		// 3 设置表数据和曲线数据
		Enumeration<String> en = dataHash.keys();
		Vector<Object> itemVc;
		String dataArr[];
		while(en.hasMoreElements()){
			key = en.nextElement();
			// 表数据
			itemVc = new Vector<Object>();
			dataArr = key.split(",");
			itemVc.add(dataArr[0]);
			itemVc.add(dataArr[1]);
			itemVc.add(dataHash.get(key));
			tData.add(itemVc);
		}
		itemVc = new Vector<Object>();
		itemVc.add("合计");
		itemVc.add(" ");
		itemVc.add(moneySum);
		tData.add(itemVc);
	}
	/**
	 * 收支对比分析
	 * @param iVc
	 * @param oVc
	 * @throws Exception
	 */
	public void setAllDataByTime(Vector<Vector> iVc, Vector<Vector> oVc) throws Exception{
		// 1 遍历收入数据，存储于HASHTABLE
		Hashtable<String, Object[]> dataHash = new Hashtable<String, Object[]>();
		String dateStr;
		Double moneyDbl;
		Double inMoneySum=0.0;
		Double outMoneySum=0.0;
		Double moneyTmp;
		String key;
		Object[] dataObj;//0-收入额；1-支出额；2-时间序列
		// 收入数据
		for(Vector aVc: iVc){
			// 时间
			dateStr = (String)aVc.get(6);
			// 金额
			moneyDbl = Double.parseDouble((String)aVc.get(3));
			// 生成标识符
			key = createKey(dateStr,"");
			if(key==null||"".equals(key)){
				continue;
			}
			// 计算总额
			inMoneySum+= moneyDbl;
			// 存储到HASHTABLE中
			dataObj = dataHash.get(key);
			if(dataObj==null){
				dataObj = new Object[3];
				dataObj[0] = moneyDbl;
				dataObj[1] = 0.0;
				dataObj[2] = createTimePeriod(dateStr);
			}else{
				moneyTmp = (Double)dataObj[0];
				moneyTmp+=moneyDbl;
				dataObj[0] = moneyTmp;
			}
			dataHash.put(key, dataObj);
		}
		// 支出数据
		for(Vector aVc: oVc){
			// 时间
			dateStr = (String)aVc.get(5);
			// 金额
			moneyDbl = Double.parseDouble((String)aVc.get(2));
			// 生成标识符
			key = createKey(dateStr,"");
			if(key==null||"".equals(key)){
				continue;
			}
			// 计算总额
			outMoneySum+= moneyDbl;
			// 存储到HASHTABLE中
			dataObj = dataHash.get(key);
			if(dataObj==null){
				dataObj = new Object[3];
				dataObj[0] = 0.0;
				dataObj[1] = moneyDbl;
				dataObj[2] = createTimePeriod(dateStr);
			}else{
				moneyTmp = (Double)dataObj[1];
				moneyTmp+=moneyDbl;
				dataObj[1] = moneyTmp;
			}
			dataHash.put(key, dataObj);
		}
		// 2 设置表头
		tHead.add("时间");
		tHead.add("收入金额");
		tHead.add("支出金额");
		// 3 设置表数据和曲线数据
		Enumeration<String> en = dataHash.keys();
		Vector<Object> itemVc;
		while(en.hasMoreElements()){
			key = en.nextElement();
			// 表数据
			itemVc = new Vector<Object>();
			itemVc.add(key);
			itemVc.add(dataHash.get(key)[0]);
			itemVc.add(dataHash.get(key)[1]);
			tData.add(itemVc);
			// 曲线数据
			inData.add((RegularTimePeriod)dataHash.get(key)[2], (Double)dataHash.get(key)[0]);
			outData.add((RegularTimePeriod)dataHash.get(key)[2], (Double)dataHash.get(key)[1]);
		}
		itemVc = new Vector<Object>();
		itemVc.add("合计");
		itemVc.add(inMoneySum);
		itemVc.add(outMoneySum);
		tData.add(itemVc);
	}
	
	/**
	 * 构造Hashtable 的KEY
	 * @param string 时间 yyyy-MM-dd HH:mm:ss
	 * @param char	统计时间单位（月、年）
	 * @param boolean 是否统计收入类型
	 * @return string 统计时间KEY 
	 * */
	private String createKey(String dateStr,String typeStr){
		try{
			Date curDate = df.parse(dateStr);
			if(curDate.before(vd.getBgCal())||curDate.after(vd.getEdCal())){
				return null;
			}
			cal.setTime(curDate);
			String key = "";
			// 按时间统计
			if("时间".equals(vd.getDtType())||'a'==type){
				if("年".equals(vd.getDpType())){
					key = cal.get(Calendar.YEAR)+"年";
				}else if("周".equals(vd.getDpType())){
					key = cal.get(Calendar.YEAR)+"年"+StringUtil.padStr(cal.get(Calendar.WEEK_OF_YEAR),2,'L','0')+"周";
				}else if("日".equals(vd.getDpType())){
					key = cal.get(Calendar.YEAR)+"年"+StringUtil.padStr((cal.get(Calendar.MONTH)+1),2,'L','0')+"月"+StringUtil.padStr(cal.get(Calendar.DAY_OF_MONTH),2,'L','0')+"日";
				}else{
					key = cal.get(Calendar.YEAR)+"年"+StringUtil.padStr((cal.get(Calendar.MONTH)+1),2,'L','0')+"月";
				}
			}
			// 按类型统计
			else if("类型".equals(vd.getDtType())){
				key = typeStr;
			}
			// 按时间和类型统计
			else{
				if("年".equals(vd.getDpType())){
					key = cal.get(Calendar.YEAR)+"年,"+typeStr;
				}else if("周".equals(vd.getDpType())){
					key = cal.get(Calendar.YEAR)+"年"+StringUtil.padStr(cal.get(Calendar.WEEK_OF_YEAR),2,'L','0')+"周,"+typeStr;
				}else if("日".equals(vd.getDpType())){
					key = cal.get(Calendar.YEAR)+"年"+StringUtil.padStr((cal.get(Calendar.MONTH)+1),2,'L','0')+"月"+StringUtil.padStr(cal.get(Calendar.DAY_OF_MONTH),2,'L','0')+"日,"+typeStr;
				}else{
					key = cal.get(Calendar.YEAR)+"年"+StringUtil.padStr((cal.get(Calendar.MONTH)+1),2,'L','0')+"月,"+typeStr;
				}
			}
			return key;
		}catch(Exception e){
			log.error(e);
		}
		return null;
	}
	
	/**
	 * 创建时间序列
	 * @param dateStr
	 * @return
	 */
	private RegularTimePeriod createTimePeriod(String dateStr) throws Exception{
		Date curDate = df.parse(dateStr);
		RegularTimePeriod timePeriod;
		if("年".equals(vd.getDpType())){
			timePeriod = new Year(curDate);
		}else if("周".equals(vd.getDpType())){
			timePeriod = new Week(curDate);
		}else if("日".equals(vd.getDpType())){
			timePeriod = new Day(curDate);
		}else{
			timePeriod = new Month(curDate);
		}
		return timePeriod;
	}

	public TimeSeries getInData() {
		return inData;
	}

	public void setInData(TimeSeries inData) {
		this.inData = inData;
	}

	public TimeSeries getOutData() {
		return outData;
	}

	public void setOutData(TimeSeries outData) {
		this.outData = outData;
	}
	public Vector<Vector> getTData() {
		return tData;
	}

	public void setTData(Vector<Vector> data) {
		tData = data;
	}

	public Vector<String> getTHead() {
		return tHead;
	}

	public void setTHead(Vector<String> head) {
		tHead = head;
	}

	public DefaultPieDataset getPieDataset() {
		return pieDataset;
	}

	public void setPieDataset(DefaultPieDataset pieDataset) {
		this.pieDataset = pieDataset;
	}
}
