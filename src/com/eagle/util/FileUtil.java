package com.eagle.util;

import java.awt.Desktop;
import java.io.*;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * @author szhang
 * 文件操作
 * */
public class FileUtil {
	private static Logger log = Logger.getLogger(FileUtil.class);
	/**
	 * 文件拷贝
	 * @param File 
	 * @param File
	 * @return boolean
	 * */
	public static boolean copyFile(File fFile,File tFile){
		try{
			if(!fFile.exists()){
				return false;
			}
			if(!tFile.exists()){
				tFile.createNewFile();
			}
			InputStream is=new FileInputStream(fFile);
			InputStreamReader reader=new InputStreamReader(is);
			BufferedReader bReader=new BufferedReader(reader);
			
			OutputStream os=new FileOutputStream(tFile);
			OutputStreamWriter writer=new OutputStreamWriter(os);
			BufferedWriter bWriter=new BufferedWriter(writer);
			
			String data="";
			while((data=bReader.readLine())!=null){
				bWriter.write(data+'\n');
			}
			bReader.close();
			bWriter.close();
			return true;
		}catch(Exception e){
			log.error(e);
		}
		return false;
	}
	
	/**
	 * 文件拷贝
	 * @param String 
	 * @param String 
	 * @return boolean
	 * */
	public static boolean copyFile(String fFileStr,String tFileStr){
		File fFile=new File(fFileStr);
		File tFile=new File(tFileStr);
		return copyFile(fFile,tFile);
	}	
	
	/**
	 * 根据文件相对路径获取文件内容，以InputStream对象返回
	 * @param String 文件相对路径
	 * @return InputStream 文件内容 
	 * */
	public static InputStream getFileAsStream(Class claz, String filename) {
		try {
			InputStream f = claz.getResource(filename).openStream();
			return f;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return null;
	}
	
	/**
	 * 根据文件相对路径获取文件内容，以InputStream对象返回
	 * @param String 文件相对路径
	 * @return InputStream 文件内容 
	 * */
	public static URL getFileAsURL(Class claz, String filename) {
		try {
			URL url = claz.getResource(filename);
			return url;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return null;
	}
	
	/**
	 * 根据文件绝对路径获取文件内容，以InputStream对象返回
	 * @param String 文件绝对路径
	 * @return InputStream 文件内容 
	 * */
	@SuppressWarnings("hiding")
	public static InputStream getFileAsStream(String filename) {
		try {
			InputStream f = new FileInputStream(filename);
			return f;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return null;
	}
	
	/**
	 * 根据文件绝对路径获取文件内容，以String类型返回
	 * @param String 文件相对路径
	 * @return String 文件内容 
	 * */
	public static String getFileAsString(Class claz, String filename) {
		BufferedReader br;
		try {
			InputStream f = getFileAsStream(claz, filename);
			br = new BufferedReader(new InputStreamReader(f));
			StringBuffer xmlStr = new StringBuffer();
			String data = "";
			while ((data = br.readLine()) != null) {
				xmlStr.append(data+'\n');
			}
			f.close();
			return xmlStr.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return null;
	}
	
	/**
	 * 根据文件绝对路径获取文件内容，以String类型返回
	 * @param String 文件绝对路径
	 * @return String 文件内容 
	 * */
	public static String getFileAsString(String filename) {
		BufferedReader br;
		try {
			InputStream f = getFileAsStream(filename);
			br = new BufferedReader(new InputStreamReader(f));
			StringBuffer xmlStr = new StringBuffer();
			String data = "";
			while ((data = br.readLine()) != null) {
				xmlStr.append(data+'\n');
			}
			f.close();
			return xmlStr.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return null;
	}
	
	/**
	 * 根据文件绝对路径获取文件内容，以byte类型返回
	 * @param String 文件相对路径
	 * @return byte 文件内容 
	 * */
	public static byte[] getFileAsByte(Class claz, String filename){
		try {
			InputStream iStrm = getFileAsStream(claz, filename);
		    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		    int ch;
		    while ((ch = iStrm.read()) != -1){
		       bytestream.write(ch);
		    }
		    byte imgdata[]=bytestream.toByteArray();
		    bytestream.close();
		    iStrm.close();
		    return imgdata;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return null;
	}
	
	/**
	 * 根据文件绝对路径获取文件内容，以byte类型返回
	 * @param String 文件绝对路径
	 * @return byte 文件内容 
	 * */
	public static byte[] getFileAsByte(String filename){
		try {
			InputStream iStrm = getFileAsStream(filename);
		    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		    int ch;
		    while ((ch = iStrm.read()) != -1){
		       bytestream.write(ch);
		    }
		    byte imgdata[]=bytestream.toByteArray();
		    bytestream.close();
		    iStrm.close();
		    return imgdata;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return null;
	}
	
	/**
	 * 文件保存
	 * @param 文件绝对路径
	 * @param 文件存储内容
	 * @return 文件存储是否成功
	 * */
	@SuppressWarnings("deprecation")
	public static boolean save(String filePath, String fileContent){
		try{
			File file = new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}

			InputStream is=new StringBufferInputStream(fileContent);
			InputStreamReader reader=new InputStreamReader(is);
			BufferedReader bReader=new BufferedReader(reader);
			
			OutputStream os=new FileOutputStream(file);
			OutputStreamWriter writer=new OutputStreamWriter(os);
			BufferedWriter bWriter=new BufferedWriter(writer);
			
			String data="";
			while((data=bReader.readLine())!=null){
				bWriter.write(data+'\n');
			}
			bReader.close();
			bWriter.close();
			return true;
		}catch(Exception e){
			log.error(e);
		}
		return false;
	}
	
	/**
	 * 文件保存
	 * @param 文件绝对路径
	 * @param 文件存储内容
	 * @return 文件存储是否成功
	 * */
	@SuppressWarnings("deprecation")
	public static boolean save(String filePath, String fileContent, String charset){
		try{
			File file = new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
			bWriter.write(fileContent);
			bWriter.flush();
			
			return true;
		}catch(Exception e){
			log.error(e);
		}
		return false;
	}

	
	/**
	 * 打开文件
	 * @param fileStr
	 */
	public static void editFile(String fileStr){
		/*try{
			Process process = Runtime.getRuntime().exec("cmd   /c   start   "+fileStr);
			InputStream in=process.getInputStream();
			int c;
			while((c=in.read())!=-1){
				System.out.print(c);
			}
			in.close();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				//	 TODO Auto-generated catch block
				log.error(e);
			}			
		}catch(Exception e){
			System.out.println(e.toString());			
		}finally{}		*/
		try{
			if(fileStr.indexOf("file:/")==0){
				fileStr = fileStr.substring(fileStr.length()-6, fileStr.length());
			}
			File file=new File(fileStr);
			Desktop.getDesktop().open(file);
		}catch(Exception e){
			log.error(e);
		}
	}
	
	/**
	 * 打开文件
	 * @param fileStr
	 */
	public static void openFile(String fileStr){
		try{
			Process process = Runtime.getRuntime().exec("cmd /c  start "+fileStr);
			InputStream in=process.getInputStream();
			int c;
			while((c=in.read())!=-1){
				System.out.print(c);
			}
			in.close();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				//	 TODO Auto-generated catch block
				log.error(e);
			}			
		}catch(Exception e){
			System.out.println(e.toString());			
		}finally{}		
	}
}
