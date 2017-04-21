package com.store.web.servlet;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.store.domain.Category;
import com.store.domain.Product;
import com.store.service.CategoryService;
import com.store.service.impl.CategoryServiceImpl;
import com.store.utils.BeanFactory;
import com.store.utils.UUIDUtils;


/***
 * 管理admin中的分类的servlet
 * @author 何长治
 *
 */
public class AdminCategoryServlet extends BaseServlet {
	private CategoryService cs = (CategoryService) BeanFactory.getBean("CategoryService");
	/***
	 * 查找所有分类
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//调用查询所有的方法
		List<Category> list = cs.findAll();
		//将list放入request域中
		request.setAttribute("list", list);
		return "/admin/category/list.jsp";
	}
	/***
	 * 跳转到添加界面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addUI(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "/admin/category/add.jsp";
	}
	/***
	 * 添加分类
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取输入的分类名称
		String cname = request.getParameter("cname");
		if(cname==null||cname=="")
		{
			request.setAttribute("msg", "分类输入有误");
			return "/jsp/msg.jsp";
		}
		//封装到bean对象中
		Category c = new Category();
		c.setCid(UUIDUtils.getId());
		c.setCname(cname);
		//调用service添加
		
		cs.add(c);
		response.sendRedirect(request.getContextPath()+"/adminCategoryServlet?method=findAll");
		return null;
	}
	
	/***
	 * 获取分类对象
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取输入的分类名称id
		String cid = request.getParameter("cid");
		System.out.println(cid);
		//调用service查询	
		Category c = cs.getById(cid);
		//将分类信息放入request域
		System.out.println(c.getCname());
		request.setAttribute("c", c);
		return "/admin/category/edit.jsp";
	}
	/***
	 * 编辑分类信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取分类id和名称
		String cid = request.getParameter("cid");
		String cname = request.getParameter("cname");
		//封装category
		Category c = new Category();
		c.setCid(cid);
		c.setCname(cname);
		//调用service查询	
		cs.update(c);
		//重定向到分类界面
		response.sendRedirect(request.getContextPath()+"/adminCategoryServlet?method=findAll");
		return null;
	}
	
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取分类id
		String cid = request.getParameter("cid");
		
		//调用service删除	
		cs.delete(cid);
		//重定向到分类界面
		response.sendRedirect(request.getContextPath()+"/adminCategoryServlet?method=findAll");
		return null;
	}
}
