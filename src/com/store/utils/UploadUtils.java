package com.store.utils;

import java.util.UUID;

public class UploadUtils {
	/***
	 * 
	 * @param realName
	 * @return
	 */
	public static String getUUIDName(String realName){
		
		int index = realName.lastIndexOf(".");
		if(index==-1){
			return UUID.randomUUID().toString().replace("-", "").toUpperCase();
		}else{
			return UUID.randomUUID().toString().replace("-", "").toUpperCase()+realName.substring(index);
		}
	}
	
/***
 * @param name
 * @return
 */
	public static String getRealName(String name){
		// c:/upload/1.jpg    1.jpg
		//鑾峰彇鏈�悗涓�釜"/"
		int index = name.lastIndexOf("\\");
		return name.substring(index+1);
	}
	
	/***
	 * 
	 * @param name
	 * @return
	 */
	public static String getDir(String name){
		int i = name.hashCode();
		String hex = Integer.toHexString(i);
		int j=hex.length();
		for(int k=0;k<8-j;k++){
			hex="0"+hex;
		}
		return "/"+hex.charAt(0)+"/"+hex.charAt(1);
	}
	
	public static void main(String[] args) {
		String s="1.jgp";
		String realName = getRealName(s);
		//System.out.println(realName);
		
		String uuidName = getUUIDName(realName);
		//System.out.println(uuidName);
		
		String dir = getDir(realName);
		System.out.println(dir);
		
		
	}
}
