package com.store.service;

import com.store.domain.User;

public interface UserService {
	public void register(User user) throws Exception;

	public User active(String code) throws Exception;

	public User login(String username, String password) throws Exception;

	public User checkUsernameIsExist(String username) throws Exception;
}
