package com.eagle.lottery;

import java.util.*;

import com.eagle.util.StringUtil;

/**
 * 取彩票随机数
 * 
 * @author eagle
 *
 */
public class LotteryInterface {
	// 数字组数
	private int groupNo;
	
	// 每组数字最大上限
	private int[] maxNos;
	
	// 每组数据字个数
	private int[] countNos;
	
	// 是否排序
	private boolean isOrder;
	
	// 每组数字存储
	private Hashtable<Integer,int[]> valuesTb = new Hashtable<Integer,int[]>();
	
	/**
	 * 构造函数赋值
	 * @param groupNo
	 * @param maxNos
	 * @param countNos
	 * @param isOrder
	 */
	public LotteryInterface(int groupNo, int[] maxNos, int[] countNos, boolean isOrder){
		this.groupNo = groupNo;
		this.countNos = countNos;
		this.maxNos = maxNos;
		this.isOrder = isOrder;
		setValues();
	}
	
	/**
	 * 初始化赋值
	 *
	 */
	public void setValues(){
		int[] values;
		for(int i=0;i<groupNo;i++){
			values = new int[countNos[i]];
			values = RadomCheck.radomNumber(values, maxNos[i], isOrder);
			valuesTb.put(i, values);
		}
	}
	
	/**
	 * 以hashtable对象返回
	 * @return
	 */
	public Hashtable getValuesAsHash(){
		return this.valuesTb;
	}
	
	/**
	 * 以1,2,3,4,5,6,7格式返回
	 * 
	 * @return
	 */
	public String getValuesAsStr(){
		StringBuffer vBuffer = new StringBuffer();
		int[] values;
		for(int i=0;i<valuesTb.size();i++){
			values  = valuesTb.get(i);
			for(int aVl:values){
				vBuffer.append(aVl);
				vBuffer.append(",");
			}
		}
		String value = vBuffer.toString();
		value = value.substring(0,value.length()-1);
		return value;
	}
	
	/**
	 * 以{1,2,3,4,5,6,7}格式返回
	 * 
	 * @return
	 */
	public int[] getValuesAsArr(){
		String arr[] = getValuesAsStr().split(",");
		int valuesArr[] = new int[arr.length];
		for(int i=0;i<arr.length;i++){
			valuesArr[i]=Integer.parseInt(arr[i]);
		}
		return valuesArr;
	}
	
	/**
	 * 以1 2 3 4 5 6|7格式返回
	 * 
	 * @return
	 */
	public String getValuesAsStrFmt(){
		StringBuffer vBuffer = new StringBuffer();
		int[] values;
		for(int i=0;i<valuesTb.size();i++){
			values  = valuesTb.get(i);
			for(int j=0;j<values.length;j++){
				vBuffer.append(StringUtil.padStr(values[j], 2, 'L', '0'));
				if(j==values.length-1){
					vBuffer.append("|");
				}else{
					vBuffer.append(" ");
				}
			}
		}
		String value = vBuffer.toString();
		value = value.substring(0,value.length()-1);
		return value;
	}
}
