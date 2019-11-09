package com.eagle.util;

import com.eagle.lottery.RadomCheck;

/**
 * 正常字符串或文件对象以编码机制的字符串间转换
 */
public class CoderUtil {
	
	/**
	 * 加密
	 * @param username
	 * @param password
	 * @param info
	 * @return
	 */
	public static String encode(String info, boolean isRandom) {
		try {
			if (StringUtil.isEmpty(info)||info.length()<2){
				return info;
			}
			/*if (!("eagle".equals(username) && "209".equals(password))) {
				return "不可引用！";
			}*/
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			// 加密
			String encodeStr = encoder.encode(info.getBytes());
			
			char eChar = 'E';
			if(isRandom){
				int radomI = RadomCheck.radomNumber(127);
				eChar = (char) radomI;
			}
			String c0 = encodeStr.substring(0,1);			
			encodeStr = c0+eChar+encodeStr.substring(1,encodeStr.length());
			return encodeStr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 解密
	 * @param username
	 * @param password
	 * @param info
	 * @return
	 */
	public static String decode(String username, String password, String info) {
		try {
			if (StringUtil.isEmpty(info)||info.length()<2){
				return info;
			}
			if (!("eagle".equals(username) && "209".equals(password))) {
				return "不可引用！";
			}
			// 解密
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			String c0 = info.substring(0,1);
			info = c0+info.substring(2,info.length());
			String decodeStr = new String(decoder.decodeBuffer(info));
			
			return decodeStr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
