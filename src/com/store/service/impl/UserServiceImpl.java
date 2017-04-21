package com.store.service.impl;

import com.store.dao.UserDao;
import com.store.domain.User;
import com.store.service.UserService;
import com.store.utils.BeanFactory;
import com.store.utils.MailUtils;

public class UserServiceImpl implements UserService {
	private UserDao userDao = (UserDao) BeanFactory.getBean("UserDao");
	/***
	 * 注册帐号
	 */
	public void register(User user) throws Exception {
		userDao.addUser(user);
		//发送邮件
		String emailMsg = "欢迎您注册我们网站，请点击超链接或者直接复制超链接中文字<a href='http://localhost:8080/store/user?method=active&code="+user.getCode()+"'>http://localhost:8080/store/user?method=active&code="+user.getCode()+"<a>";
		MailUtils.sendMail(user.getEmail(), emailMsg);
	}
	/**
	 * 激活帐号
	 * @throws Exception 
	 */
	public User active(String code) throws Exception {
		//根据code查找帐号
		User user = userDao.getUserByCode(code);
		//判断帐号是否查找成功,如果成功，更新状态，返回user，不成功，返回null
		if(user==null){
			return null;
		}
		user.setState(1);
		userDao.updateUser(user);
		return user;
		
	}
	public User login(String username, String password) throws Exception {
		User user = userDao.getUserByUsernameAndPwd(username,password);
		
		return user;
	}
	
	public User checkUsernameIsExist(String username) throws Exception {
		return userDao.checkUsernameIsExist(username);
	}

}
