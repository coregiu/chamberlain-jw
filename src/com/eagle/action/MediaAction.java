package com.eagle.action;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.eagle.data.ConfBean;
import com.eagle.lottery.RadomCheck;
import com.eagle.multiprocess.MediaPlayProcess;
import com.eagle.ui.MainFrame;
import com.eagle.util.FileUtil;
import com.eagle.util.StringUtil;

public class MediaAction {
	private AudioClip aau;
	private Logger log = Logger.getLogger(MediaAction.class);
	public MediaPlayProcess readPprocess;
	private boolean isPlay = true;
	
	/**
	 * 同步播放音乐
	 * @param mediaName
	 */
	@SuppressWarnings("deprecation")
	public void play(String mediaName){
		try {
			File mediaFile = null;
			URL cb = null;
			if(StringUtil.isNotEmpty(mediaName)){
				cb = FileUtil.getFileAsURL(MainFrame.class, "logo/media/"+mediaName);
			}else{
				File mediaPath = new File(ConfBean.mediaPath);
				if(mediaPath.exists()&&mediaPath.isDirectory()){
					File listFile[] = mediaPath.listFiles();
					int i = RadomCheck.radomNumber(listFile.length)-1;
					mediaFile = listFile[i];
				}
			}

			if(cb == null&&mediaFile != null&&mediaFile.exists()){
				cb = mediaFile.toURL();
			}
			aau = Applet.newAudioClip(cb);
			aau.play();
		} catch (MalformedURLException e) {
			log.error(e);
		}
	}
	
	/**
	 * 结束播放
	 *
	 */
	public void stop(){
		try{
			if(aau!=null){
				aau.stop();
			}
		}catch(Exception e){
			log.error(e);
		}
	}
	
	/**
	 * 播放音乐
	 * @param mediaName
	 * @param adp
	 */
	@SuppressWarnings("deprecation")
	public void play(String mediaName, AudioClip adp){
		try {
			File mediaFile = null;
			URL cb = FileUtil.getFileAsURL(MainFrame.class, "logo/media/"+mediaName);

			if(cb == null&&mediaFile != null&&mediaFile.exists()){
				cb = mediaFile.toURL();
			}
			adp = Applet.newAudioClip(cb);
			adp.play();
		} catch (MalformedURLException e) {
			log.error("文件"+mediaName+"不存在！");
		}
	}
	
	/**
	 * 多线程播放音乐
	 * 
	 * @param mediaName
	 * @param adp
	 * @param ma
	 */
	public void play(String mediaName, AudioClip adp, MediaAction ma){
		try {
			Vector<String> vec = new Vector<String>();
			vec.add(mediaName);
			// 启动读数线程，读数据
			if(readPprocess!=null){
				readPprocess.setRunFlag(false);
			}
			readPprocess = new MediaPlayProcess(ma, vec, adp);
			readPprocess.start();
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	/**
	 * 停止播放
	 * 
	 * @param adp
	 */
	public void stop(AudioClip adp){
		try{
			if(adp!=null){
				adp.stop();
			}
		}catch(Exception e){
			log.error(e);
		}
	}
	
	/**
	 * 多线程播放入口
	 * 
	 * @param mediaName
	 * @param adp
	 */
	public void playCal(String mediaName, AudioClip adp){
		if(!isPlay){
			return;
		}
		try{
			play(mediaName, adp, this);
		}catch(Exception e){
			log.error("文件"+mediaName+"不存在！");
		}
	}
	
	/**
	 * 多线程中播放音乐使用的方法
	 * 
	 * @param mediaName
	 * @param adp
	 */
	public void playProcess(String mediaName, AudioClip adp){
		if(!isPlay){
			return;
		}
		try{
			stop(adp);
			play(mediaName, adp);
		}catch(Exception e){
			log.error("文件"+mediaName+"不存在！");
		}
	}
	
	/**
	 * 计算器等于及数字播放
	 * 
	 * @param value
	 */
	public void playEqual(String value){
		if(!isPlay){
			return;
		}
		try{
			String ten = "ten.wav";// 十
			String hun = "hun.wav";//百
			String thou = "thou.wav";//千
			String mill = "mill.wav";//万
			String bill = "bill.wav";//亿
			String un = "un.wav";//负数
			String dot = "dot.wav";//点
			String equal = "equal.wav";//等于
			String mArr[] = {"a0.wav","a1.wav","a2.wav","a3.wav","a4.wav","a5.wav","a6.wav","a7.wav","a8.wav","a9.wav"};
			Vector<String> vec = new Vector<String>();
			
			double db = Double.parseDouble(value);
			if(db>999999999999.0){
				return;
			}
			DecimalFormat df = new DecimalFormat("##.##");
			value = df.format(db);
			
			String intStr = "";
			String dotStr = "";
			int dotInt = value.indexOf(".");
			if(dotInt==-1){
				intStr = value;
			}else{
				intStr = value.substring(0, dotInt);
				dotStr = value.substring(dotInt+1, value.length());
			}
			// 构造整数部分
			String intArr[][] = new String[intStr.length()][2];
			String aIntArr[];
			String aTmpChr;
			for(int i=intStr.length();i>0;i--){
				aIntArr = new String[2];
				aTmpChr = intStr.substring(i-1, i);
				if("-".equals(aTmpChr)){
					aIntArr[0] = un;
				}else{
					int tmpInt = Integer.parseInt(aTmpChr);
					switch(intStr.length()-i){
					case 0:
						aIntArr[0]=mArr[tmpInt];
						break;
					case 1:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
							aIntArr[1]=ten;
						}
						break;
					case 2:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
							aIntArr[1]=hun;
						}
						break;
					case 3:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
							aIntArr[1]=thou;
						}
						break;	
					case 4:
						aIntArr[0]=mArr[tmpInt];
						aIntArr[1]=mill;
						break;
					case 5:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
							aIntArr[1]=ten;
						}
						break;
					case 6:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
							aIntArr[1]=hun;
						}
						break;
					case 7:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
							aIntArr[1]=thou;
						}
						break;
					case 8:
						aIntArr[0]=mArr[tmpInt];
						aIntArr[1]=bill;
						break;
					case 9:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
							aIntArr[1]=ten;
						}
						break;
					case 10:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
							aIntArr[1]=hun;
						}
						break;
					case 11:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
							aIntArr[1]=thou;
						}
						break;
					case 12:
						aIntArr[0]=mArr[tmpInt];
						aIntArr[1]=bill;
						break;
					case 13:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
							aIntArr[1]=ten;
						}
						break;
					case 14:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
							aIntArr[1]=hun;
						}
						break;
					case 15:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
						aIntArr[1]=thou;
						}
						break;
					case 16:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
						aIntArr[1]=bill;
						}
						break;	
					case 17:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
						aIntArr[1]=ten;
						}
						break;
					case 18:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
						aIntArr[1]=hun;
						}
						break;
					case 19:
						aIntArr[0]=mArr[tmpInt];
						if(tmpInt!=0){
						aIntArr[1]=thou;
						}
						break;
					}
				}
				
				intArr[i-1] = aIntArr;
			}
			// 构造小数部分
			String dotArr[] = null;
			if(StringUtil.isNotEmpty(dotStr)&&Integer.parseInt(dotStr)>0){
				dotArr = new String[dotStr.length()];
				for(int i=0;i<dotStr.length();i++){
					dotArr[i] = mArr[Integer.parseInt(dotStr.substring(i,i+1))];
				}
			}
			
			// 构造语音
			// 添加整数部分
			String aArr[];
			for(int i=0;i<intArr.length;i++){
				aArr = intArr[i];
				vec.add(aArr[0]);
				if(StringUtil.isNotEmpty(aArr[1])){
					vec.add(aArr[1]);
				}
			}
			// 去重复的零
			int i=0;
			while(i<vec.size()-1){
				if(vec.get(i).equals(mArr[0])&&vec.get(i+1).equals(mArr[0])){
					vec.remove(i);
				}else{
					i++;
				}
			}
			// 去万以下末尾的零
			String aItem;
			i=vec.size()-1;
			while(i>=0){
				aItem = vec.get(i);
				if(aItem.equals(mArr[0])){
					vec.remove(i);
					i--;
				}else{
					break;
				}
			}
			
			// 去亿以下末尾的零
			i=vec.size()-1;
			boolean flag = false;
			while(i>=0){
				aItem = vec.get(i);
				if(!flag){
					if(aItem.equals(mill)){
						flag = true;
					}
					i--;
					continue;
				}
				
				if(aItem.equals(mArr[0])){
					vec.remove(i);
					i--;
				}else{
					break;
				}
			}
			
			// 去亿末尾的零
			i=vec.size()-1;
			flag = false;
			while(i>=0){
				aItem = vec.get(i);
				if(!flag){
					if(aItem.equals(bill)){
						flag = true;
					}
					i--;
					continue;
				}
				
				if(aItem.equals(mArr[0])){
					vec.remove(i);
					i--;
				}else{
					break;
				}
			}
			
			// 小数部分
			if(dotArr!=null&&dotArr.length>0){
				vec.add(dot);
				for(String aMi:dotArr){
					vec.add(aMi);
				}
			}
			
			// 添加等于号读音
			vec.insertElementAt(equal, 0);
			
			for(String aVec:vec){
				log.info(aVec);
			}
			
			// 启动读数线程，读数据
			if(readPprocess!=null){
				readPprocess.setRunFlag(false);
			}
			readPprocess = new MediaPlayProcess(this, vec);
			readPprocess.start();
		}catch(Exception e){ 
			log.error("数值太长");
			log.error(e);
		}
	}

	public boolean isPlay() {
		return isPlay;
	}

	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}
	
	public static void main(String[] args) {
		MediaAction ma = new MediaAction();
		ma.playEqual("-560079800200.09872498");
	}
}
