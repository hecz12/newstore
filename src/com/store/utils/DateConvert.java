package com.store.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.org.apache.commons.beanutils.Converter;

public class DateConvert implements Converter {

	/***
	 * 第一个参数Class要转成的类型
	 * 第二个参数Object页面上传入的值
	 */
	public Object convert(Class arg0, Object arg1) {
		//将object转成date
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		try {
			Date date =  format.parse((String)arg1);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
