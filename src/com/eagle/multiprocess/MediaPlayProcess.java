package com.eagle.multiprocess;

import java.applet.AudioClip;
import java.util.*;

import org.apache.log4j.Logger;

import com.eagle.action.MediaAction;

/**
 * 计算器读数线程
 * @author szhang
 *
 */
public class MediaPlayProcess extends Thread implements Runnable{
	
	private Logger log = Logger.getLogger(MediaPlayProcess.class);
	private boolean runFlag = true;
	private Vector<String> data;
	private MediaAction ma;
	private AudioClip adp;
	
	public Vector<String> getData() {
		return data;
	}

	public void setData(Vector<String> data) {
		this.data = data;
	}

	public boolean isRunFlag() {
		return runFlag;
	}

	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}

	public MediaPlayProcess(MediaAction ma, Vector<String> vec){
		this.data = vec;
		this.ma = ma;
	}
	
	public MediaPlayProcess(MediaAction ma, Vector<String> vec, AudioClip adp){
		this.data = vec;
		this.ma = ma;
		this.adp = adp;
	}
	
	public void run(){
		int i=0;
		String mediaName;
		while(runFlag&&i<data.size()){
			try{
				mediaName = data.get(i++);
				ma.playProcess(mediaName, adp);
				Thread.sleep(700);
			}catch(Exception e){
				log.error(e);
			}
		}
	}
}
