package com.store.domain;

import java.io.Serializable;
import java.util.Date;
/***
 * 用户实体
 * @author 何长治
 *
 */
public class User implements Serializable {
	/**
	 * create table user (
	uid varchar(32) not null,
	username varchar(20) default null,
	password varchar(20) default null,
	name varchar(20) default null,
	email varchar(30) default null,
	telephone varchar(20) default null,
	birthday date default null,
	sex varchar(10) default null,
	state int(11) default null,
	code varchar(64) default null,
	primary key(uid)
) engine=innodb default charset=utf8;
	 */
	private String uid;
	private String username;
	private String password;
	private String name;
	private String email;
	private String telephone;
	private Date birthday;
	private String sex;
	private Integer state = 0;//激活状态，值为1，算激活，值为0，未激活
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	private String code;
	
	
}
