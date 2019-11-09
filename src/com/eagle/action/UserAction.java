package com.eagle.action;

import java.io.File;
import java.util.*;
import org.apache.commons.logging.*;
import org.dom4j.*;

import com.eagle.common.URLManager;
import com.eagle.data.*;
import com.eagle.util.*;
import com.eagle.xmlparse.XMLParser;

/**
 * 用户信息操作
 * */
public class UserAction {
	private Hashtable<String, UserBean> userInfoList;
	
	private static Log log = LogFactory.getLog(com.eagle.action.UserAction.class);
	private static Document doc;
	public UserAction(){
		doc = XMLUtil.getDocument(ConfBean.userInfoStr);
		userInfoList = XMLParser.getUserInfoList(doc);
	}
	
	/**
	 * 用户登录
	 * */
	public char login(String username, String password){
		UserBean loginUserBean = userInfoList.get(username);
		if(loginUserBean == null){
			log.info("登录用户信息不存在，请检查配置文件！");			
			return 'u';
		}else if(!loginUserBean.getPassword().equals(password)){			
			return 'p';
		}else{
			ConfBean.loginUserInfo = loginUserBean;
			return 'o';
		}
	}
	
	/**
	 * 添加用户
	 * */
	public boolean addUser(UserBean userBean){
		Element rootEle = doc.getRootElement();
		try{
			// 检查用户是否已注册
			Iterator it = rootEle.elementIterator();
			while(it.hasNext()){
				Element aUser = (Element)it.next();
				if(aUser.element("username").getTextTrim().equals(userBean.getUsername())||aUser.element("username").getTextTrim().equals(userBean.getUsername()+"bak")){
					return false;
				}
			}
			Document userDoc = DocumentHelper.createDocument();
			userDoc.addElement("user");
			Element userEle = userDoc.getRootElement();
			
			userEle.addElement("username");
			userEle.element("username").setText(userBean.getUsername());			
			userEle.addElement("realname");
			userEle.element("realname").setText(userBean.getRealname());			
			userEle.addElement("password");
			userEle.element("password").setText(userBean.getPassword());			
			userEle.addElement("filename");
			userEle.element("filename").setText(userBean.getFilename());			
			userEle.addElement("relation");
			userEle.element("relation").setText("");
			
			rootEle.add(userDoc.getRootElement());
			String userInfoStr = XMLUtil.createXmlString(doc);
			ConfBean.userInfoStr = userInfoStr;
			
			FileUtil.save(ConfBean.userConfigFilePath,CoderUtil.encode(userInfoStr, false));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 修改用户
	 * */
	public boolean updateUser(UserBean userBean){
		Element rootEle = doc.getRootElement();
		try{
			// 先删除该用户信息
			Iterator it = rootEle.elementIterator();
			while(it.hasNext()){
				Element aUser = (Element)it.next();
				if(aUser.element("username").getTextTrim().equals(userBean.getUsername())){
					rootEle.remove(aUser);
				}
			}
			// 再添加该用户信息
			Document userDoc = DocumentHelper.createDocument();
			userDoc.addElement("user");
			Element userEle = userDoc.getRootElement();
			
			userEle.addElement("username");
			userEle.element("username").setText(userBean.getUsername());			
			userEle.addElement("realname");
			userEle.element("realname").setText(userBean.getRealname());			
			userEle.addElement("password");
			userEle.element("password").setText(userBean.getPassword());			
			userEle.addElement("filename");
			userEle.element("filename").setText(userBean.getFilename());			
			userEle.addElement("relation");
			userEle.element("relation").setText(userBean.getRelation());
			
			rootEle.add(userDoc.getRootElement());
			String userInfoStr = XMLUtil.createXmlString(doc);
			ConfBean.userInfoStr = userInfoStr;
			
			FileUtil.save(ConfBean.userConfigFilePath,CoderUtil.encode(userInfoStr, false));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 删除用户
	 * */
	public boolean deleteUser(UserBean userBean){
		Element rootEle = doc.getRootElement();
		try{
			Iterator it = rootEle.elementIterator();
			while(it.hasNext()){
				Element aUser = (Element)it.next();
				if(aUser.element("username").getTextTrim().equals(userBean.getUsername())){
					// 删除用户配置信息中数据
					rootEle.remove(aUser);
					// 删除收支信息文件
					File file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode(aUser.element("filename").getTextTrim(), false));
					File filebak = new File(URLManager.getWorkDirPath()+"backup/"+CoderUtil.encode(aUser.element("filename").getTextTrim()+"bak", false));
					if(file.exists()){
						file.delete();
					}
					if(filebak.exists()){
						filebak.delete();
					}
					// 删除日记数据
					file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode(aUser.element("filename").getTextTrim()+"_diary", false));
					filebak = new File(URLManager.getWorkDirPath()+"backup/"+CoderUtil.encode(aUser.element("filename").getTextTrim()+"_diarybak", false));
					if(file.exists()){
						file.delete();
					}
					if(filebak.exists()){
						filebak.delete();
					}
				}
			}
			String userInfoStr = XMLUtil.createXmlString(doc);
			ConfBean.userInfoStr = userInfoStr;
			
			FileUtil.save(ConfBean.userConfigFilePath,CoderUtil.encode(userInfoStr, false));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 查询用户密码
	 * @param username
	 * @return
	 */
	public String viewPass(String username){
		return "";
	}
}
