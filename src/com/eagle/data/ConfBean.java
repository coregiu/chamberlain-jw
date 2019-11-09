package com.eagle.data;

import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;

import com.eagle.action.DataBakAction;
import com.eagle.common.URLManager;
import com.eagle.prop.NameProp;
import com.eagle.util.*;

public class ConfBean {
	private static Logger log = Logger.getLogger(ConfBean.class);
	public static String VERSION = "1.02";
	
	public static String type;// 登录类型（0-本人，1-小红）
	
	public static String lastUser = "zhangsai";// 上次使用者
	
	public static String splashTime = "6000"; // 设置闪屏时间
	
	public static String userConfigFile = "conf/userconfig.xml";// 系统用户信息配置
	
	public static UserBean loginUserInfo;// 登录用户信息
	
	public static String userInfoStr;// 系统所有用户
	
	public static String ioDataStr;// 收支信息
	
	public static String autobakup;// 是否自动备份数据
	
	public static String autologin;// 是否重新登录
	
	public static String autoRemind;// 是否自动提醒工作进度
	
	public static String diaryStr;// 日记文档路径
	
	public static String plainStr;// 工作学习计划文档路径
	
	public static String extFilePath;// 闹钟等扩展文件路径
	
	public static String mediaPath;// 音乐路径
	
	public static String HELP;// 帮助文档路径
	
	public static String backupPath;//
	
	private static String path;
	public static String propertyFilePath;
	public static String userConfigFilePath;
	public static String ioDataFilePath;
	public static String diaryFilePath;
	public static String plainFilePath;
	public static String lotteryFilePath;
	
	static{
		// 加载数据 
		path = URLManager.getWorkDirPath();
		propertyFilePath = path+"conf/sysconfig.properties";
		userConfigFilePath = path+"conf/userconfig.xml";
		backupPath = path+"backup";
		HELP = path+"help.chm";
		mediaPath = path+"media/";
		checkFolder();
		loadProperties();
		loadUsers();
	}
	
	public static void reLoad(){
		loadUsers();
		loadIOInfo();
		loadDiaryInfo();
		loadPlainInfo();
		loadClockInfo();
		loadLotteryInfo();
		loadCfgInfo();
	}
	
	public static void loadInfo(){
		loadIOInfo();
		loadDiaryInfo();
		loadPlainInfo();
		loadClockInfo();
		loadLotteryInfo();
		loadCfgInfo();
	}
	/**
	 * 检查数据配置文件夹是否存在
	 * */
	public static void checkFolder(){
		File confDir = new File(path+"conf");
		if(!confDir.exists()){
			confDir.mkdirs();
		}
		File dataDir = new File(path+"data");
		if(!dataDir.exists()){
			dataDir.mkdirs();
		}
		//备份路径
		File backupDir = new File(path+"backup");
		if(!backupDir.exists()){
			backupDir.mkdirs();
		}
	}
	
	/**
	 * 从property文件获取登录类型和上次使用者等基本信息
	 * */
	public static void loadProperties() {
		try{
			log.info("系统当前工作目录是--"+path);
			Properties prop = PropertiesUtil.getResourceAsProperties(propertyFilePath);
			type = System.getProperty("type");
			type = StringUtil.isEmpty(type)?"0":type;
			lastUser = (String)prop.get("lastUser");
			splashTime = (String)prop.get("splashTime");
			userConfigFile = (String)prop.get("userConfigFile");
		}catch(Exception e){
			log.error(e);
		}
	}
	
	/**
	 * 加载用户信息
	 * */
	public static void loadUsers(){
		File userconfigFile = new File(userConfigFilePath);
		
		// 如果用户配置文件不存在，新生成一个用户信息配置文件
		if(!userconfigFile.exists()){
			creatNewUserConfigFile();
		}
		
		// 读取所有用户信息
		userInfoStr = FileUtil.getFileAsString(userConfigFilePath);
		userInfoStr = new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, userInfoStr));
		//log.info(userInfoStr);		
	}

	/**
	 * 加载收支信息
	 * */
	public static void loadIOInfo(){
		ioDataFilePath = path+"data/"+CoderUtil.encode(loginUserInfo.getFilename(), false);
		try{
			File f = new File(ioDataFilePath);
			if(!f.exists()){
				f.createNewFile();
				String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
										"<IOInfo>"+
										"<ins/>"+
										"<outs/>"+
										"</IOInfo>";
				FileUtil.save(ioDataFilePath,CoderUtil.encode(content, false));
			}
			ioDataStr = new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, FileUtil.getFileAsString(ioDataFilePath)));
			//log.info(ioDataStr);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 新建用户信息配置文件
	 * */
	private static void creatNewUserConfigFile(){
		String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
									"<CONFIG>"+
										"<user>"+
											"<username>administrator</username>"+
											"<realname>管理员</realname>"+
											"<password>administrator</password>"+
											"<filename>administrator</filename>"+
											"<relation></relation>"+
										"</user>"+
									"</CONFIG>";
		FileUtil.save(userConfigFilePath,CoderUtil.encode(fileContent, false));
	}
	
	/**
	 * 加载日记信息
	 *
	 */
	private static void loadDiaryInfo(){
		diaryFilePath = path+"data/"+CoderUtil.encode(loginUserInfo.getFilename()+"_diary", false);
		try{
			File f = new File(diaryFilePath);
			if(!f.exists()){
				f.createNewFile();
				String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
										"<PrivateData><Diary/><Archives/></PrivateData>";
				FileUtil.save(diaryFilePath,CoderUtil.encode(content, false));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载双色球信息
	 *
	 */
	private static void loadLotteryInfo(){
		lotteryFilePath = path+"data/"+CoderUtil.encode("lottery", false);
		try{
			File f = new File(lotteryFilePath);
			if(!f.exists()){
				f.createNewFile();
				String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
										"<Lottery><ssq/><zc/><tc/><dlt/></Lottery>";
				FileUtil.save(lotteryFilePath, CoderUtil.encode(content, false));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载工作计划信息
	 *
	 */
	private static void loadPlainInfo(){
		plainFilePath = path+"data/"+CoderUtil.encode(loginUserInfo.getFilename()+"_plain", false);
		try{
			File f = new File(plainFilePath);
			if(!f.exists()){
				f.createNewFile();
				String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
										"<PlainData></PlainData>";
				FileUtil.save(plainFilePath, CoderUtil.encode(content, false));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载工作计划信息
	 *
	 */
	private static void loadClockInfo(){
		extFilePath = path+"data/"+CoderUtil.encode(loginUserInfo.getFilename()+"_ext", false);
		try{
			File f = new File(extFilePath);
			if(!f.exists()){
				f.createNewFile();
				String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
										"<Extension>" +
										"<clock/>" +
										"<mmrDay/>" +
										"<config>" +
											"<item id=\"autobakup\">0</item>" +
											"<item id=\"autologin\">0</item>" +
											"<item id=\"autoRemind\">0</item>" +
											"<item id=\"bolder\">false</item>" +
											"<item id=\"italic\">false</item>" +
											"<item id=\"underline\">false</item>" +
											"<item id=\"color\">-16777216</item>" +
											"<item id=\"font\">13</item>" +
											"<item id=\"size\">13</item>" +
										"</config>" +
										"</Extension>";
				FileUtil.save(extFilePath, CoderUtil.encode(content, false));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void loadFilenames(){
		ioDataFilePath = path+"data/"+CoderUtil.encode(loginUserInfo.getFilename(), false);
		diaryFilePath = path+"data/"+CoderUtil.encode(loginUserInfo.getFilename()+"_diary", false);
		plainFilePath = path+"data/"+CoderUtil.encode(loginUserInfo.getFilename()+"_plain", false);
		extFilePath = path+"data/"+CoderUtil.encode(loginUserInfo.getFilename()+"_ext", false);
	}
	
	public static void loadCfgInfo(){
		DataBakAction dbAction = new DataBakAction();
		Hashtable<String, String> data = dbAction.getConfigHash();
		ConfBean.autobakup = data.get("autobakup");
		ConfBean.autologin = data.get("autologin");
		ConfBean.autoRemind = data.get("autoRemind");
	}
}
