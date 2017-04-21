package com.store.dao;

import com.store.domain.User;

public interface UserDao {

	void addUser(User user) throws Exception;

	void updateUser(User user) throws Exception;

	User getUserByCode(String code) throws Exception;

	User getUserByUsernameAndPwd(String username, String password) throws Exception;

	User checkUsernameIsExist(String username) throws Exception;
}

