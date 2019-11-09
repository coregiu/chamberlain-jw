/*
 * Created on 2009-4-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.eagle.util;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author szhang
 */
public class PropertiesUtil {

	private static ClassLoader defaultClassLoader;

	public static ClassLoader getDefaultClassLoader() {
		return defaultClassLoader;
	}

	public static void setDefaultClassLoader(ClassLoader classloader) {
		defaultClassLoader = classloader;
	}

	public static URL getResourceURL(String resource) throws IOException {
		return getResourceURL(getClassLoader(), resource);
	}

	public static URL getResourceURL(ClassLoader loader, String resource)
			throws IOException {
		URL url = null;
		if (loader != null) {
			url = loader.getResource(resource);
		}
		if (url == null) {
			url = ClassLoader.getSystemResource(resource);
		}
		if (url == null) {
			throw new IOException("Could not find resource " + resource);
		} else {
			return url;
		}
	}

	/**
	 * 获取字节流
	 * 
	 * @param �ļ����·��
	 * @return InputSteam
	 */
	public static InputStream getResourceAsStream(String resource)
			throws IOException {
		return getResourceAsStream(getClassLoader(), resource);
	}

	/**
	 * ��ȡPROPERTIES�ļ�����
	 * 
	 * @param ClassLoader
	 * @param �ļ����·��
	 * @return InputSteam
	 */
	@SuppressWarnings("deprecation")
	public static InputStream getResourceAsStream(ClassLoader loader,
			String resource) throws IOException {
		InputStream in = null;
		if (loader != null) {
			in = loader.getResourceAsStream(resource);
		}
		if (in == null) {
			in = ClassLoader.getSystemResourceAsStream(resource);
		}
		if (in == null) {
			String sourceStr = FileUtil.getFileAsString(resource);
			in = new StringBufferInputStream(sourceStr);
			return in;
		} else {
			return in;
		}
	}

	public static Properties getResourceAsProperties(String resource)
			throws IOException {
		Properties props = new Properties();
		InputStream in = null;
		String propfile = resource;
		in = getResourceAsStream(propfile);
		props.load(in);
		in.close();
		return props;
	}

	public static Properties getResourceAsProperties(InputStream in)
			throws IOException {
		Properties props = new Properties();
		props.load(in);
		in.close();
		return props;
	}

	public static Properties getResourceAsProperties(ClassLoader loader,
			String resource) throws IOException {
		Properties props = new Properties();
		InputStream in = null;
		String propfile = resource;
		in = getResourceAsStream(loader, propfile);
		props.load(in);
		in.close();
		return props;
	}

	public static Reader getResourceAsReader(String resource)
			throws IOException {
		return new InputStreamReader(getResourceAsStream(resource));
	}

	public static Reader getResourceAsReader(ClassLoader loader, String resource)
			throws IOException {
		return new InputStreamReader(getResourceAsStream(loader, resource));
	}

	public static File getResourceAsFile(String resource) throws IOException {
		return new File(getResourceURL(resource).getFile());
	}

	public static File getResourceAsFile(ClassLoader loader, String resource)
			throws IOException {
		return new File(getResourceURL(loader, resource).getFile());
	}

	public static InputStream getUrlAsStream(String urlString)
			throws IOException {
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		return conn.getInputStream();
	}

	public static Reader getUrlAsReader(String urlString) throws IOException {
		return new InputStreamReader(getUrlAsStream(urlString));
	}

	public static Properties getUrlAsProperties(String urlString)
			throws IOException {
		Properties props = new Properties();
		InputStream in = null;
		String propfile = urlString;
		in = getUrlAsStream(propfile);
		props.load(in);
		in.close();
		return props;
	}

	public static Class classForName(String className)
			throws ClassNotFoundException {
		Class clazz = null;
		try {
			clazz = getClassLoader().loadClass(className);
		} catch (Exception exception) {
		}
		if (clazz == null) {
			clazz = Class.forName(className);
		}
		return clazz;
	}

	public static Object instantiate(String className)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		return instantiate(classForName(className));
	}

	public static Object instantiate(Class clazz)
			throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}

	private static ClassLoader getClassLoader() {
		if (defaultClassLoader != null) {
			return defaultClassLoader;
		} else {
			return Thread.currentThread().getContextClassLoader();
		}
	}
}