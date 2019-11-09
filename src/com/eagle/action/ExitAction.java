package com.eagle.action;

import com.eagle.data.ConfBean;

/**
 * 系统退出时执行的操作
 * */
public class ExitAction {
	/**
	 * 系统退出时执行的操作
	 * */
	public void regist(){
		if(ConfBean.loginUserInfo == null){
			return ;
		}
		// 自动备份数据
		if("0".equals(ConfBean.autobakup)){
			DataBakAction action = new DataBakAction();
			action.bakData();
		}
		// 记录本次使用者
		updateLastUser();
	}
	
	/**
	 * 修改上次使用者
	 * */
	public void updateLastUser(){
		String oldStr = "lastUser = "+ConfBean.lastUser;
		String newStr = "lastUser = "+ConfBean.loginUserInfo.getUsername();
		DataBakAction backAction = new DataBakAction();
		backAction.systemConfig(oldStr, newStr);
	}
}
