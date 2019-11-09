package com.eagle.util;

public class StringUtil {
	/**
	 * 按一定长度给特定字符串补齐
	 * 
	 * @param str 原始字符串
	 * @param len 补齐长度
	 * @param local 补齐位置 R-右侧; L-左侧
	 * @param pad 填补的字符
	 * @return
	 */
	public static String padStr(String str, int len, char local, char pad){
		if(str.length()>=len){
			return str;
		}
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<len-str.length();i++){
			buffer.append(pad);
		}
		switch(local){
			case 'R':{
				str = str+buffer.toString();
				break;
			}
			default:{
				str = buffer.toString()+str;
			}
		}
		return str;
	}
	
	public static String padStr(int str, int len, char local, char pad){
		return padStr(String.valueOf(str), len, local, pad);
	}
	
	public static boolean isEmpty(String str){
		if(str==null||"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
}
