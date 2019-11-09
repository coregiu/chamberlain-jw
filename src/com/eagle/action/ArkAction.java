package com.eagle.action;

import java.util.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;
import org.dom4j.*;

import com.eagle.data.ConfBean;
import com.eagle.prop.NameProp;
import com.eagle.util.CoderUtil;
import com.eagle.util.FileUtil;
import com.eagle.util.StringUtil;
import com.eagle.util.XMLUtil;
import com.eagle.xmlparse.XMLParser;

/**
 * 档案资料
 * 数据格式如下：
 * <Archives>
 * 		<item id="吉林移动资料">
 * 			<content>...</content>
 * 			<item id="WPS">
 * 				<content>...</content>
 * 				<item id="测试环境">
 * 					<content>...</content>
 *				</item>
 *				<item id="生产环境">
 * 					<content>...</content>
 *				</item>
 * 			</item>
 * 		</item>
 * </Archives>
 * 
 * @author szhang
 *
 */
public class ArkAction {
	private Logger log = Logger.getLogger(ArkAction.class);
	public ArkAction(){
		
	}
	
	/**
	 * 创建档案
	 * 
	 * @param arkData
	 * @return
	 */
	public boolean createArkMenu(DefaultMutableTreeNode currentNode, String itemStr){
		try {
			// 获取创建路径
			TreeNode path[] = currentNode.getPath();
			// 获取档案内容
			String prvData = XMLParser.getDiaryText();
			Document doc = XMLUtil.getDocument(prvData);
			Element arkEle = doc.getRootElement().element(NameProp.ARKITEM);
			Element subEle = arkEle;
			for(int i=1;i<path.length;i++){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)path[i];
				Iterator it = subEle.elementIterator();
				while(it.hasNext()){
					subEle = (Element)it.next();
					Attribute attr = subEle.attribute("id");
					if(attr!=null&&attr.getText().equals(node.getUserObject())){
						break;
					}
				}
			}
			if(subEle!=null){
				Document aItemDoc = DocumentHelper.createDocument();
				aItemDoc.addElement("item");
				Element aItemEle = aItemDoc.getRootElement();
				aItemEle.addAttribute("id", "id");
				aItemEle.attribute("id").setText(itemStr);
				aItemEle.addElement("content");
				
				subEle.add(aItemEle);
				
				String diaryStr = XMLUtil.createXmlString(doc);
				FileUtil.save(ConfBean.diaryFilePath, CoderUtil.encode(diaryStr, false));
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}
	
	/**
	 * 档案重命名
	 * 
	 * @param arkData
	 * @return
	 */
	public boolean renameArkMenu(DefaultMutableTreeNode currentNode, String itemStr){
		try {
			// 获取创建路径
			TreeNode path[] = currentNode.getPath();
			// 获取档案内容
			String prvData = XMLParser.getDiaryText();
			Document doc = XMLUtil.getDocument(prvData);
			Element arkEle = doc.getRootElement().element(NameProp.ARKITEM);
			Element subEle = arkEle;
			for(int i=1;i<path.length;i++){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)path[i];
				Iterator it = subEle.elementIterator();
				while(it.hasNext()){
					subEle = (Element)it.next();
					Attribute attr = subEle.attribute("id");
					if(attr!=null&&attr.getText().equals(node.getUserObject())){
						break;
					}
				}
			}
			
			if(subEle!=null){
				subEle.attribute("id").setText(itemStr);
				
				String diaryStr = XMLUtil.createXmlString(doc);
				FileUtil.save(ConfBean.diaryFilePath, CoderUtil.encode(diaryStr, false));
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}
	
	/**
	 * 保存档案
	 * 
	 * @param arkData
	 * @return
	 */
	public boolean saveArkMenu(DefaultMutableTreeNode currentNode, String value){
		try {
			// 获取创建路径
			TreeNode path[] = currentNode.getPath();
			// 获取档案内容
			String prvData = XMLParser.getDiaryText();
			Document doc = XMLUtil.getDocument(prvData);
			Element arkEle = doc.getRootElement().element(NameProp.ARKITEM);
			Element subEle = arkEle;
			for(int i=1;i<path.length;i++){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)path[i];
				Iterator it = subEle.elementIterator();
				while(it.hasNext()){
					subEle = (Element)it.next();
					Attribute attr = subEle.attribute("id");
					if(attr!=null&&attr.getText().equals(node.getUserObject())){
						break;
					}
				}
			}
			
			if(subEle!=null){
				Element cntEle = subEle.element("content");
				if(cntEle==null){
					subEle.addElement("content");
					cntEle = subEle.element("content");
				}
				if(StringUtil.isNotEmpty(value)){
					value = value.replaceAll("\n", linFlag);
				}
				cntEle.setText(value);
				
				String diaryStr = XMLUtil.createXmlString(doc);
				FileUtil.save(ConfBean.diaryFilePath, CoderUtil.encode(diaryStr, false));
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}
	
	/**
	 * 删除档案
	 * 
	 * @param arkData
	 * @return
	 */
	public boolean deleteArkMenu(DefaultMutableTreeNode currentNode){
		try {
			// 获取创建路径
			TreeNode path[] = currentNode.getPath();
			// 获取档案内容
			String prvData = XMLParser.getDiaryText();
			Document doc = XMLUtil.getDocument(prvData);
			Element arkEle = doc.getRootElement().element(NameProp.ARKITEM);
			Element subEle = arkEle;
			for(int i=1;i<path.length;i++){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)path[i];
				Iterator it = subEle.elementIterator();
				while(it.hasNext()){
					subEle = (Element)it.next();
					Attribute attr = subEle.attribute("id");
					if(attr!=null&&attr.getText().equals(node.getUserObject())){
						break;
					}
				}
			}
			
			if(subEle!=null){
				subEle.getParent().remove(subEle);
				
				String diaryStr = XMLUtil.createXmlString(doc);
				FileUtil.save(ConfBean.diaryFilePath, CoderUtil.encode(diaryStr, false));
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}
	
	/**
	 * 获取档案目录
	 * 
	 * @param arkData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DefaultMutableTreeNode getArkMenu(DefaultMutableTreeNode curNode, DefaultMutableTreeNode arkRoot){
		DefaultMutableTreeNode extNode = arkRoot;
		try{
			TreeNode curPath[] = curNode.getPath();
	
			for(int i=0;i<curPath.length;i++){
				extNode = getOwnNode(extNode, curPath[i]);
			}
		}catch(Exception e){
			log.error(e);
		}
		return extNode==null?arkRoot:extNode;
	}
	
	/**
	 * 
	 * @param curNode
	 * @param arkRoot
	 * @return
	 * @throws Exception
	 */
	private DefaultMutableTreeNode getOwnNode(DefaultMutableTreeNode curNode, TreeNode arkRoot)
			throws Exception {
		DefaultMutableTreeNode tmpNode = null;
		Object tmpUsrObj = "";
		Object curTmpUsrObj = "";
		
		Enumeration en = curNode.children();
		while(en.hasMoreElements()){
			tmpNode = (DefaultMutableTreeNode)en.nextElement();
			tmpUsrObj = tmpNode.getUserObject();
			curTmpUsrObj = ((DefaultMutableTreeNode)arkRoot).getUserObject();
			if(curTmpUsrObj.equals(tmpUsrObj)){
				return tmpNode;
			}
		}
		return null;
	}
	
	/**
	 * 取得添加后的节点
	 * 
	 * @param parentNode
	 * @param chldNodeName
	 * @return
	 */
	public DefaultMutableTreeNode setAddedNode(DefaultMutableTreeNode parentNode, String chldNodeName){
		DefaultMutableTreeNode chldNode = new DefaultMutableTreeNode(chldNodeName);
		parentNode.add(chldNode);
		return chldNode;
	}
	
	/**
	 * 取得修改后的节点
	 * 
	 * @param parentNode
	 * @param chldNodeName
	 * @return
	 */
	public DefaultMutableTreeNode setUpdatedNode(DefaultMutableTreeNode parentNode, String chldNodeName){
		DefaultMutableTreeNode chldNode = new DefaultMutableTreeNode(chldNodeName);
		((DefaultMutableTreeNode)parentNode.getParent()).add(chldNode);
		return chldNode;
	}
	
	/**
	 * 获取档案内容
	 * 
	 * @param arkData
	 * @return
	 */
	public String getArkContent(DefaultMutableTreeNode currentNode){
		try {
			if(currentNode == null){
				return "";
			}
			// 获取创建路径
			TreeNode path[] = currentNode.getPath();
			// 获取档案内容
			String prvData = XMLParser.getDiaryText();
			Document doc = XMLUtil.getDocument(prvData);
			Element arkEle = doc.getRootElement().element(NameProp.ARKITEM);
			Element subEle = arkEle;
			for(int i=1;i<path.length;i++){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)path[i];
				Iterator it = subEle.elementIterator();
				while(it.hasNext()){
					subEle = (Element)it.next();
					Attribute attr = subEle.attribute("id");
					if(attr!=null&&attr.getText().equals(node.getUserObject())){
						break;
					}
				}
			}
			
			if(subEle!=null){
				Element cntEle = subEle.element("content");
				if(cntEle !=null){
					String content = cntEle.getTextTrim();
					if(StringUtil.isNotEmpty(content)){
						content = content.replaceAll(linFlag, "\n");
					}
					return content;
				}else{
					return "";
				}
				
			}else{
				return "";
			}
		} catch (Exception e) {
			log.error(e);
			return "";
		}
	}
	
	/**
	 * 判断当前节点档案子目录是否已存在
	 * 
	 * @param arkData
	 * @return
	 */
	public boolean isRepeat(DefaultMutableTreeNode currentNode, String arkName){
		Enumeration en = currentNode.children();
		String nodeName = ""; 
		DefaultMutableTreeNode chldNode;
		while(en.hasMoreElements()){
			chldNode = (DefaultMutableTreeNode)en.nextElement();
			nodeName = (String)chldNode.getUserObject();
			if(nodeName.equals(arkName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断当前节点档案同一目录是否已存在
	 * 
	 * @param arkData
	 * @return
	 */
	public boolean isRepeatL(DefaultMutableTreeNode currentNode, String arkName){
		Enumeration en = currentNode.getParent().children();
		String nodeName = ""; 
		DefaultMutableTreeNode chldNode;
		while(en.hasMoreElements()){
			chldNode = (DefaultMutableTreeNode)en.nextElement();
			nodeName = (String)chldNode.getUserObject();
			if(nodeName.equals(arkName)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 加载档案资料树
	 * 
	 * @param currentNode
	 */
	public void setArchivesMenu(DefaultMutableTreeNode arkNode){
		try{
			String prvData = XMLParser.getDiaryText();
			Document doc = XMLUtil.getDocument(prvData);
			Element arkEle = doc.getRootElement().element(NameProp.ARKITEM);
			setChildenNodes(arkNode, arkEle);
		}catch(Exception e){
			log.error(e);
		}
	}
	
	/**
	 * 递归加载档案资料树
	 * @param arkNode
	 * @param arkEle
	 */
	@SuppressWarnings("unchecked")
	private void setChildenNodes(DefaultMutableTreeNode arkNode, Element arkEle){
		try {
			Attribute idAtr = arkEle.attribute("id");
			String nodeName = "";
			DefaultMutableTreeNode curNode;
			if(idAtr != null){
				nodeName = idAtr.getText();
				curNode = new DefaultMutableTreeNode(nodeName);
				arkNode.add(curNode);
			}else{
				curNode = arkNode;
			}
		
			Iterator<Element> nodeIt = arkEle.elementIterator();
			String pubName;
			while(nodeIt!=null&&nodeIt.hasNext()){
				Element pubNode = nodeIt.next();
				pubName = pubNode.getName();
				if(!"content".equals(pubName)){
					setChildenNodes(curNode, pubNode);
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	private static String linFlag = "&nbsp;";
}
