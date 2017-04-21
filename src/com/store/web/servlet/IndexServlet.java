package com.store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.store.domain.Category;
import com.store.domain.Product;
import com.store.service.CategoryService;
import com.store.service.ProductService;
import com.store.service.impl.CategoryServiceImpl;
import com.store.service.impl.ProductServiceImpl;
import com.store.utils.BeanFactory;


public class IndexServlet extends BaseServlet {

	ProductService ps = (ProductService) BeanFactory.getBean("ProductService");
	public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//从数据库中查询热门商品和最新商品
		List<Product> hotlist = ps.findHotProduct();
		List<Product> newlist = ps.findNewProduct();
		System.out.println(hotlist);
		System.out.println(newlist);
		//将最新商品和热门商品放入request域中
		request.setAttribute("hotlist", hotlist);
		request.setAttribute("newlist",newlist);
		return "/jsp/index.jsp";
	}
   

}
