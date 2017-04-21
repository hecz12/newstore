package com.store.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseServlet extends HttpServlet {
    
    
	private static final long serialVersionUID = 1L;
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	try {
	    		//获取子类
	       	 Class<? extends BaseServlet> clazz = this.getClass();
	       	//获取请求方法
	       	String m = request.getParameter("method");
	       	if(m==null)
	       	{
	       		m="index";
	       	}
	       	System.out.println(m);
	       	//获取子类对应的方法对象
			Method method = clazz.getMethod(m,HttpServletRequest.class,HttpServletResponse.class);
			//调用方法，返回值为转发路径
			String path = (String) method.invoke(this,request,response);
			//判断s是否为空，为空，则不传
			if(path!=null)
			{
				request.getRequestDispatcher(path).forward(request, response);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
    }
	    public String index(HttpServletRequest request,HttpServletResponse response) throws Exception
	    {
	    	return null;
	    }

}
