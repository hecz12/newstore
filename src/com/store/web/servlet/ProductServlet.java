package com.store.web.servlet;

import java.io.IOException;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.Request;

import com.store.domain.PageBean;
import com.store.domain.Product;
import com.store.service.ProductService;
import com.store.service.impl.ProductServiceImpl;
import com.store.utils.BeanFactory;
import com.store.utils.CookUtils;

public class ProductServlet extends BaseServlet {
	ProductService ps = (ProductService) BeanFactory.getBean("ProductService");
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String getProductById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//=获取商品编号
		String pid = request.getParameter("pid");
		Product p = null;
		//从数据库中查出商品信息
		try {
			p = ps.getProductById(pid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("p", p);
		//进行cookie判断
		Cookie cookie = CookUtils.getCookieByName("history", request.getCookies());
		cookie = setCookieHistory(cookie,p,request);
		response.addCookie(cookie);
		return "/jsp/product_info.jsp";
	}
	/***
	 * 进行Cookie操作
	 * @param cookie
	 * @param p
	 * @param request
	 */
	private Cookie setCookieHistory(Cookie cookie, Product p, HttpServletRequest request) {
		if(cookie == null)
		{
			cookie = new Cookie("history",p.getPid());
		}
		else
		{
			String values = cookie.getValue();
			System.out.println("cookie中的值："+values);
			LinkedList<String> list =new LinkedList<String>();
			for(String i:values.split(","))
			{
				list.addLast(i);
			}
			System.out.println("取出的list"+list);
			//判断list是否包含此商品
			if(list.contains(p.getPid()))
			{
				list.remove(p.getPid());
				list.addFirst(p.getPid());
			}
			else
			{
				//判断list是否大于等于3
				if(list.size()>=3)
				{
					list.removeLast();
					list.addFirst(p.getPid());
				}
				else{
					list.addFirst(p.getPid());
				}
			}
			StringBuilder sb = new StringBuilder(); 
			//将list转String
			for(int i = 0;i<list.size();i++)
			{
				sb.append(list.get(i));
				if(i+1!=list.size())
					sb.append(",");
			}
			System.out.println("转换成的list"+sb.toString());
			cookie = new Cookie("history",sb.toString());
			System.out.println("存入的cookie"+cookie.getValue());
		}
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath(request.getContextPath()+"/");
		return cookie;
	}

	public String findByPage(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//判断cookie
		Cookie cookie = CookUtils.getCookieByName("history", request.getCookies());
		String[] str = cookie.getValue().split(",");
		LinkedList<String> list = new  LinkedList<String>();
		for(int i = 0;i<str.length;i++)
		{
			list.addLast(str[i]);
		}
		
		//调用service寻找浏览历史
		List<Product> plist = ps.findHistory(list);
		//1.获取cid,currPage

		String cid = request.getParameter("cid");
		int currPage = Integer.parseInt(request.getParameter("currPage"));
		//调用service中方法，获取返回的pageBean
		PageBean<Product> bean = null;
		int pageSize = 12;
		try {
			bean = ps.findByPage(cid,currPage,pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//将pageBean放入request中
		request.setAttribute("pb", bean);
		request.setAttribute("plist", plist);
		System.out.println(bean);
		return "/jsp/product_list.jsp";
	}
}
