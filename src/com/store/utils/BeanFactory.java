package com.store.utils;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * 利用反射实现解隅和
 * @author 何长治
 *
 */
public class BeanFactory {
	public static Object getBean(String id)
	{
		try {
			//获取xml文档对象
			Document doc = new SAXReader().read(BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml"));
			//获取指定id的元素
			Element element = (Element) doc.selectSingleNode("//bean[@id='"+id+"']");
			//获取对应class属性的值
			String value = element.attributeValue("class");
			System.out.println(value);
			//利用反射得到此对象
			final Object obj = Class.forName(value).newInstance();
			//对反射对象进行代理
			if(id.endsWith("Service"))
			{
				Object proxyInstance = Proxy.newProxyInstance(obj.getClass().getClassLoader(),obj.getClass().getInterfaces() 
						, new InvocationHandler() {
							public Object invoke(Object proxy, Method method, Object[] args)
									throws Throwable {
								//进行判断
								if("add".equals(method.getName())||"register".equals(method.getName()))
								{
									System.out.println("您执行了添加操作"+args[0].getClass().getName());
									return method.invoke(obj, args);
								}
								return method.invoke(obj, args);
							}
						});
				return proxyInstance;
			}
			return obj;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
