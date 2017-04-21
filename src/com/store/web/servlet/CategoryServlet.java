package com.store.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.store.domain.Category;
import com.store.service.CategoryService;
import com.store.service.impl.CategoryServiceImpl;
import com.store.utils.BeanFactory;
import com.store.utils.JsonUtil;

public class CategoryServlet extends BaseServlet {
    CategoryService cs = (CategoryService) BeanFactory.getBean("CategoryService");
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			//向数据库中查询分类信息
			List<Category> clist = null;
			try {
				clist = cs.findAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//将分类信息转json
			String json = JsonUtil.list2json(clist);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().print(json);
			
			//去数据库查询最新商品和最热商品，将他们放入request域中，请求转发
			return null;
		}
	}

