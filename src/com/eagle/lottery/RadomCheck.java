package com.eagle.lottery;

/**
 * 取随机数
 * 
 * @author eagle
 *
 */
public class RadomCheck {
	
	/**
	 * 取不重的随机数组
	 * 
	 * @param values 定长数组
	 * @param maxNo 上限数字
	 * @param order 是否排序
	 * @return
	 */
	public static int[] radomNumber(int[] values, int maxNo, boolean order){
		for(int i=0;i<values.length;i++){
			values[i] = radomNumber(values, maxNo);
		}
		if(order){
			int temp;
			for(int i=0;i<values.length;i++){
				for(int j=0;j<values.length-i-1;j++){
					if(values[j]>values[j+1]){
						temp = values[j];
						values[j] = values[j+1];
						values[j+1] = temp;
					}
				}
			}
		}
		return values;
	}
	
	/**
	 * 取数组内没有的随机数
	 * 
	 * @param values
	 * @param maxNo
	 * @return
	 */
	public static int radomNumber(int[] values, int maxNo){
		if(values.length>maxNo){return -1;}
		boolean scFlag=true;
		int rdNo = -1;
		while(scFlag){
			rdNo = radomNumber(maxNo);
			for(int i=0;i<values.length;i++){
				if(values[i]==rdNo){
					break;
				}
				if(values[i]!=rdNo&&i==values.length-1){
					scFlag=false;
					break;
				}
			}
		}
		return rdNo;
	}
	
	/**
	 * 获取随机数
	 * @param maxNo
	 * @return
	 */
	public static int radomNumber(int maxNo){
		return ((int)(Math.random()*(maxNo)))+1;
	}
}
