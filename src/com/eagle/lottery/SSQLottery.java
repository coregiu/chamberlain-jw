package com.eagle.lottery;

/**
 * 双色球取随机数
 * 
 * @author eagle
 *
 */
public class SSQLottery extends LotteryInterface {
	
	public SSQLottery(){
		super(2, new int[]{33,16}, new int[]{6,1}, true);
	}
	
	public static void main(String[] args) {
		SSQLottery ssq;
		/*boolean flag=true;
		String ssqStr;
		int i=0;
		while(flag){
			ssq = new SSQLottery();
			ssqStr = ssq.getValuesAsStrFmt();
			System.out.println(i+"--"+ssqStr);
			i++;
			if("05 06 15 23 27 30|12".equals(ssqStr)){
				flag = false;
			}
		}
		System.out.println(i);*/
		
		int i=1;
		while(i<=126452){
			ssq = new SSQLottery();
			System.out.println(i+"--"+ssq.getValuesAsStrFmt());
			i++;
		}
	}
}
