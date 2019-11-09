package com.eagle.action;

import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dom4j.*;

import com.eagle.common.URLManager;
import com.eagle.data.ConfBean;
import com.eagle.data.ConfigData;
import com.eagle.data.UserBean;
import com.eagle.prop.NameProp;
import com.eagle.util.CoderUtil;
import com.eagle.util.FileUtil;
import com.eagle.util.XMLUtil;
import com.eagle.xmlparse.XMLParser;

/**
 * 数据备份与恢复
 * 系统参数设置
 * */
public class DataBakAction {
	private Logger log = Logger.getLogger(DataBakAction.class);
	/**
	 * 数据备份
	 * */
	public boolean bakData(){
		// 1 收支数据
		File file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename(), false));
		File filebak = new File(ConfBean.backupPath+"/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"bak", false));
		FileUtil.copyFile(file, filebak);
		// 2 日记
		file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_diary", false));
		filebak = new File(ConfBean.backupPath+"/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_diarybak", false));
		FileUtil.copyFile(file, filebak);
		// 3 彩票
		file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode("lottery", false));
		filebak = new File(ConfBean.backupPath+"/"+CoderUtil.encode("lotterybak", false));
		FileUtil.copyFile(file, filebak);
		// ４　工作学习计划数据
		file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_plain", false));
		filebak = new File(ConfBean.backupPath+"/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_plainbak", false));
		FileUtil.copyFile(file, filebak);
		
		// 5　扩展数据
		file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_ext", false));
		filebak = new File(ConfBean.backupPath+"/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_extbak", false));
		FileUtil.copyFile(file, filebak);
		
		return true;
	}
	
	/**
	 * 数据恢复
	 * */
	public boolean restoreData(){
		// 1 收支数据
		File file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename(), false));
		File filebak = new File(ConfBean.backupPath+"/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"bak", false));
		FileUtil.copyFile(filebak, file);
		// 2 日记
		file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_diary", false));
		filebak = new File(ConfBean.backupPath+"/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_diarybak", false));
		FileUtil.copyFile(filebak, file);
		// 3 彩票
		file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode("lottery", false));
		filebak = new File(ConfBean.backupPath+"/"+CoderUtil.encode("lotterybak", false));
		FileUtil.copyFile(filebak, file);
		// 4 工作学习计划数据
		file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_plain", false));
		filebak = new File(ConfBean.backupPath+"/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_plainbak", false));
		FileUtil.copyFile(filebak, file);
		
		// 5 扩展数据
		file = new File(URLManager.getWorkDirPath()+"data/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_ext", false));
		filebak = new File(ConfBean.backupPath+"/"+CoderUtil.encode(ConfBean.loginUserInfo.getFilename()+"_extbak", false));
		FileUtil.copyFile(filebak, file);
		
		return true;
	}
	
	/**
	 * 系统参数设置
	 * */
	@SuppressWarnings("unchecked")
	public boolean systemConfig(ConfigData data){
		try {
			String extStr = XMLParser.getExtText();
			Document doc = XMLUtil.getDocument(extStr);
			
			Element cfgEle = doc.getRootElement().element("config");
			Iterator<Element> cfgIt = cfgEle.elementIterator();
			Element itemEle;
			while (cfgIt.hasNext()) {
				itemEle = cfgIt.next();
				Attribute attr = itemEle.attribute("id");
				if(attr!=null&&attr.getText().equals(data.getName())){
					itemEle.setText(data.getValue());
					FileUtil.save(ConfBean.extFilePath, CoderUtil.encode(doc.asXML(), false));
					return true;
				}
			}
			
			Document aDoc = DocumentHelper.createDocument();
			aDoc.addElement("item");
			Element aEle = aDoc.getRootElement();
			aEle.addAttribute("id", "id");
			aEle.attribute("id").setText(data.getName());
			aEle.setText(data.getValue());
			FileUtil.save(ConfBean.extFilePath, CoderUtil.encode(doc.asXML(), false));
			return true;
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
	
	/**
	 * 系统参数设置
	 * */
	public boolean systemConfig(String oldStr, String newStr){
		try{
			String filename = URLManager.getWorkDirPath()+"conf/sysconfig.properties";
			String content = FileUtil.getFileAsString(filename);
			if(content.indexOf(oldStr)==-1){
				content = content+newStr;
			}else{
				content = content.replace(oldStr, newStr);
			}
			FileUtil.save(filename, content);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 导入数据
	 * <Data><username/><password/><IOInfo/><PrivateData/></Data>
	 * @param filePath
	 * @return
	 */
	public String importData(String filePath, String password, boolean flag){
		try{
			String data = new String(CoderUtil.decode(NameProp.CODE_NAME, NameProp.CODE_PASS, FileUtil.getFileAsString(filePath)));
			Document doc = XMLUtil.getDocument(data);
			Element root = doc.getRootElement();
			String username = root.element("username").getTextTrim();
			String imppass = root.element("password").getTextTrim();
			if(!imppass.equals(password)){
				return "密码错误，无权导入.";
			}
			// 覆盖式导入
			if(flag){
				// 删除用户及用户所有收入、日记信息
				UserAction userAction = new UserAction();
				UserBean userBean = new UserBean();
				userBean.setUsername(username);
				userAction.deleteUser(userBean);
				
				// 添加用户
				userBean.setRealname(username);
				userBean.setPassword(password);
				userBean.setFilename(username);
				userBean.setRelation(username);
				userAction.addUser(userBean);
				
				ConfBean.loginUserInfo=userBean;	
				ConfBean.loadFilenames();
				// 1 添加收支信息
				FileUtil.save(ConfBean.ioDataFilePath, CoderUtil.encode(root.element("IOInfo").asXML(), false));
				// 2 添加日记数据
				FileUtil.save(ConfBean.diaryFilePath, CoderUtil.encode(root.element("PrivateData").asXML(), false));
				// 3 工作学习计划数据
				FileUtil.save(ConfBean.plainFilePath, CoderUtil.encode(root.element("PlainData").asXML(), false));
				// 4 扩展数据
				FileUtil.save(ConfBean.extFilePath, CoderUtil.encode(root.element("Extension").asXML(), false));
			}
			// 追加式导入
			else{
				
			}
		}catch(Exception e){
			e.printStackTrace();
			return "数据错误";
		}
		return "导入成功！用户密码即为导入密码。";
	}
	
	/**
	 * 导出数据
	 * <Data><username/><password/><IOInfo/><PrivateData/></Data>
	 * @param filePath
	 * @param password
	 * @return
	 */
	public boolean exportData(String filePath,String password){
		try{
			Document  doc = DocumentHelper.createDocument();
			doc.addElement("Data");
			Element root = doc.getRootElement();
			root.addElement("username");
			root.element("username").setText(ConfBean.loginUserInfo.getUsername());
			root.addElement("password");
			root.element("password").setText(password);
			
			// 1 收支数据
			String ioStr = XMLParser.getIOText();
			Document ioDoc = XMLUtil.getDocument(ioStr);
			root.add(ioDoc.getRootElement());
			// 2 日记数据
			String privateStr = XMLParser.getDiaryText();
			Document privateDoc = XMLUtil.getDocument(privateStr);
			root.add(privateDoc.getRootElement());
			// 3 工作计划数据
			String plainStr = XMLParser.getPlainText();
			Document plainDoc = XMLUtil.getDocument(plainStr);
			root.add(plainDoc.getRootElement());
			
			// 4 扩展数据
			String extStr = XMLParser.getExtText();
			Document extDoc = XMLUtil.getDocument(extStr);
			root.add(extDoc.getRootElement());
			
			FileUtil.save(filePath, CoderUtil.encode(XMLUtil.createXmlString(doc), false));
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 是否为粗体，斜体，下划线
	 * @param autoBak
	 * @return
	 */
	public boolean isBIU(String name, boolean isTrue){
		try{
			ConfigData data = new ConfigData();
			if(isTrue){
				data.setValue("false");
			}else{
				data.setValue("true");
			}
			data.setName(name);
			return systemConfig(data);
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 字体、颜色、大小
	 * @param autoBak
	 * @return
	 */
	public boolean setFSC(String name, String newValue){
		try{
			ConfigData data = new ConfigData();
			data.setName(name);
			data.setValue(newValue);
			return systemConfig(data);
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public Hashtable<String,String> getConfigHash(){
		Hashtable<String,String> tb = new Hashtable<String,String>();
		String extStr = XMLParser.getExtText();
		Document doc = XMLUtil.getDocument(extStr);
		
		Element cfgEle = doc.getRootElement().element("config");
		Iterator<Element> cfgIt = cfgEle.elementIterator();
		Element itemEle;
		while (cfgIt.hasNext()) {
			itemEle = cfgIt.next();
			Attribute attr = itemEle.attribute("id");
			if(attr!=null){
				tb.put(attr.getText(), itemEle.getTextTrim());
			}
		}
		return tb;
	}
}
