package com.store.utils;

import javax.servlet.http.Cookie;

public class CookUtils {
	/***
	 * 
	 * @param name
	 * @param cookies
	 * @return
	 */
	public static Cookie getCookieByName(String name, Cookie[] cookies) {
		if(cookies!=null){
			for (Cookie c : cookies) {
				//判断与需要寻找的cookie名符合的cookie
				if(name.equals(c.getName())){
					return c;
				}
			}
		}
		return null;
	}
}
