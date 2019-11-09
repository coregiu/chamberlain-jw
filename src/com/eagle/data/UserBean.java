package com.eagle.data;

/**
 * 用户信息
 * */
public class UserBean {
	private String username;// 用户名
	
	private String realname;// 用户姓名
	
	private String password;// 密码
	
	private String filename;// 收支信息文件
	
	private String relation;// 与本人关系

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}	
}
