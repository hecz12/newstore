package com.store.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.store.constant.Constant;
import com.store.domain.User;
import com.store.service.UserService;
import com.store.service.impl.UserServiceImpl;
import com.store.utils.BeanFactory;
import com.store.utils.CookUtils;
import com.store.utils.DateConvert;
import com.store.utils.MD5Utils;
import com.store.utils.UUIDUtils;
import com.sun.org.apache.commons.beanutils.BeanUtils;
import com.sun.org.apache.commons.beanutils.ConvertUtils;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

/***
 * 和用户相关的servlet
 * @author 49540
 *
 */
public class UserServlet extends BaseServlet {
	private UserService s = (UserService) BeanFactory.getBean("UserService");
	private static final long serialVersionUID = 1L;

	public String add(HttpServletRequest request, HttpServletResponse response)
	{
		System.out.println("userServlet的add方法执行了");
		return null;
	}
	/***
	 * 跳转到注册界面
	 * @param req
	 * @param resp
	 * @return String
	 */
	public String registerUI(HttpServletRequest request, HttpServletResponse response)
	{
		return "/jsp/register.jsp";
	}
	/***
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public String register(HttpServletRequest request, HttpServletResponse response)
	{
		String ImageCode = (String) request.getSession().getAttribute("ImageCode");
		System.out.println(ImageCode);
		System.out.println(request.getParameter("ImageCode"));
		if(ImageCode == null)
		{
			request.setAttribute("msg","出错了。。。");
			return "/jsp/msg.jsp";
		}
		else if(!ImageCode.equalsIgnoreCase(request.getParameter("ImageCode")))
		{
			request.setAttribute("msg","您的验证码有问题");
			return "/jsp/msg.jsp";
		}
		try {
			//1.封装数据
			User user = new User();
			ConvertUtils.register(new DateConvert(), Date.class);
			System.out.println(request.getParameterMap());
			BeanUtils.populate(user, request.getParameterMap());
			user.setUid(UUIDUtils.getId());
			user.setCode(UUIDUtils.getCode());
			user.setPassword(MD5Utils.md5(user.getPassword()));
			//2.调用service完成注册
			s.register(user);
			//3.完成页面请求转发
			request.setAttribute("msg","用户注册已成功，赶快去邮箱激活吧");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "/jsp/msg.jsp";
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String active(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String code = request.getParameter("code");
		User user = s.active(code);
		if(user==null)
		{
			request.setAttribute("msg", "请重新激活");
		}else{
			request.setAttribute("msg", "邮箱已激活成功");
		}
		
		return "/jsp/msg.jsp";
	}
	/**
	 * 跳转到登录页面
	 * @param request
	 * @param response
	 * @return
	 */
	public String loginUI(HttpServletRequest request, HttpServletResponse response)
	{
		return "/jsp/login.jsp";
	}
	
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String login(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		String ImageCode = (String) request.getSession().getAttribute("ImageCode");
		System.out.println(ImageCode);
		System.out.println(request.getParameter("ImageCode"));
		if(ImageCode == null)
		{
			request.setAttribute("msg","出错了。。。");
			return "/jsp/login.jsp";
		}
		else if(!ImageCode.equalsIgnoreCase(request.getParameter("ImageCode")))
		{
			request.setAttribute("msg", "验证码错误");
			return "/jsp/login.jsp";
		}
		else{
		//获取用户名和密码
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println(username+","+password);
		//将用户名和用md5加密的密码与与数据库进行对比
		User user = s.login(username,MD5Utils.md5(password));
		System.out.println(user);
		//如果为空，不跳转
		if(user == null)
		{
			request.setAttribute("msg", "用户名和密码错误");
			return "/jsp/login.jsp";
		}
		if(Constant.USER_IS_ACTIVE != user.getState())
		{
			request.setAttribute("msg", "未激活，赶快去激活吧");
			return "/jsp/login.jsp";
		}
		request.getSession().setAttribute("user", user);
		response.sendRedirect(request.getContextPath()+"/");
		
		return null;
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String logOut(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		response.sendRedirect(request.getContextPath()+"/");
		return null;
	}
	
	public String checkUsernameIsExist(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String username = request.getParameter("username");
		User user = s.checkUsernameIsExist(username);
		PrintWriter writer = response.getWriter();
		if(user == null)
		{
			writer.print(0);
		}
		else
		{
			writer.print(1);
		}
		
		return null;
	}
}
