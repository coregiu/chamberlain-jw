package com.eagle.multiprocess;

import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import com.eagle.action.PlainAction;
import com.eagle.data.ClockData;
import com.eagle.data.ConfBean;
import com.eagle.ui.remind.ClockRemind;
import com.eagle.ui.remind.RemindPopMessage;
import com.eagle.util.StringUtil;

public class ClockProcess extends Thread {
	private Logger log = Logger.getLogger(ClockProcess.class);
	private ClockData clkData;
	public ClockProcess(ClockData clkData){
		this.clkData = clkData;
	}
	//private int hours;

	private int miu;
	
	private int second;
	
	private boolean runFlag = true;
	
	private ClockRemind clkRemind=null;

	@SuppressWarnings("deprecation")
	public void run() {
		while (runFlag) {
			Calendar t = Calendar.getInstance();
			//hours = t.get(Calendar.HOUR_OF_DAY);
			miu = t.get(Calendar.MINUTE);
			second = t.get(Calendar.SECOND);
			String keyStr = new SimpleDateFormat("HHmm").format(t.getTime());
			//log.info(keyStr);
			
			String remindStr = "";
			String clockStr = "";

			// 加载闹钟提醒
			if((this.clkData.getClkDataList()==null||this.clkData.getClkDataList().size()==0)&&(!"0".equals(ConfBean.autoRemind))){
				break;
			}
			
			ClockData aClock = this.clkData.getClkDataList().get(keyStr);
			if(aClock!=null){
				if("闹钟式".equals(aClock.getRemindType())){
					clockStr = aClock.getRemindContent();
				}else{
					remindStr = aClock.getRemindContent();
				}
			}
			
			// 偶数整点提醒工作学习计划执行情况
			if(miu==0&&"0".equals(ConfBean.autoRemind)){
			//if(second%10==0){
				PlainAction plAction = new PlainAction();
				remindStr = plAction.getFinishItem(new Date());
			}
			
			boolean isRemind = false;
			// 闹钟提醒
			if(StringUtil.isNotEmpty(clockStr)){
				isRemind = true;
				if(clkRemind==null){
					new ClockRemind(clockStr);
				}else{
					clkRemind.start();
				}
			}
			
			// 右下角便签提醒
			if(StringUtil.isNotEmpty(remindStr)){
				isRemind = true;
				new RemindPopMessage(remindStr);
			}
			
			if(isRemind){
				try {
					sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					log.error(e);
				}
			}
			try {
				if(second==0){
					sleep(60000);
				}else{
					sleep(1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
	}

	public ClockData getClkData() {
		return clkData;
	}

	public void setClkData(ClockData clkData) {
		this.clkData = clkData;
	}

	public boolean isRunFlag() {
		return runFlag;
	}

	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}
}