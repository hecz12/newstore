package com.store.web.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.store.domain.Category;
import com.store.domain.Product;
import com.store.service.CategoryService;
import com.store.service.ProductService;
import com.store.utils.BeanFactory;
import com.store.utils.UUIDUtils;
import com.store.utils.UploadUtils;



public class AdminProductServlet extends BaseServlet {
	private ProductService ps = (ProductService) BeanFactory.getBean("ProductService"); 
	private CategoryService cs = (CategoryService) BeanFactory.getBean("CategoryService");
	/***
	 * 查找所有商品
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
	//调用sevice查找返回list
	List<Product> list = ps.findAll();
	//将list放入域中
	request.setAttribute("list", list);
	return "/admin/product/list.jsp";
	}
	
	/***
	 * 转到添加商品页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addUI(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		getCategoryList(request);
		return "/admin/product/add.jsp";
	}

	private void getCategoryList(HttpServletRequest request) throws Exception {
		//获取所有分类
		List<Category> list = cs.findAll(); 
		//将分类列表加入request域中
		request.setAttribute("list", list);
	}
	/***
	 * 添加一个商品
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		Product p = getProductFrom(request);
		//调用service存储
		ps.add(p);
		//重定向到list.jsp
		response.sendRedirect(request.getContextPath()+"/adminProductServlet?method=findAll");
		return null;
	}

	
	/***
	 * 下架商品
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		//获取商品id
		String pid = request.getParameter("pid");
		//查找该商品
		Product p = ps.findById(pid);
		p.setPflag(1);
		//直接让商品下架
		ps.update(p);
		//重定向到list界面
		response.sendRedirect(request.getContextPath()+"/adminProductServlet?method=find&flag=0");
		return null;
	}
	
	public String updateUI(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String pid = request.getParameter("pid");
		System.out.println(pid);
		//获取商品信息
		Product p = ps.getProductById(pid);
		//获取分类信息,获取cid，在获得product同时回去cid
		String cid = ps.getCidByPid(pid);
		Category c = new Category();
		c.setCid(cid);
		System.out.println(p.getPname());
		p.setCategory(c);
		
		getCategoryList(request);
		request.setAttribute("p", p);
		return "/admin/product/edit.jsp";
	}
	
	/***
	 * 获取表单中所有的数据
	 * @param request
	 * @return
	 * @throws FileUploadException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private Product getProductFrom(HttpServletRequest request)
			throws Exception {
		//创建map集合
		HashMap<String, String> map = new HashMap<String, String>();
		//获取表单，开始封装
		//获取磁盘工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//获取upload组件
		ServletFileUpload upload = new ServletFileUpload(factory);
		//解析表单
		List<FileItem> list = upload.parseRequest(request);
		//遍历，对文件和表单分别封装
		for (FileItem fileItem : list) {
			//如果是表单数据
			if(fileItem.isFormField())
			{
				//获取表单name和value属性,封装到map
				map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
			}
			else
			{
				//获取文件真实名称和随机名称
				String name = fileItem.getName();
				String realName = UploadUtils.getRealName(name);
				String uuidName = UploadUtils.getUUIDName(realName);
				//获取文件路径
				String path = this.getServletContext().getRealPath("/products/1");
				//获取输出流
				File file = new File(path, uuidName);
				System.out.println(file.getPath());
				FileOutputStream os = new FileOutputStream(file);
				InputStream in = fileItem.getInputStream();
				//拷贝
				IOUtils.copy(in, os);
				//关流
				os.close();
				in.close();
				//删除临时文件
				fileItem.delete();
				//将map放入数组中
				map.put(fileItem.getFieldName(), "products/1/"+uuidName);
			}
		}
		//使用beanutils
		Product p = new Product();
		//封装pid
		p.setPid(UUIDUtils.getId());
		//封装日期
		p.setPdate(new Date());
		//封装分类
		Category c = new Category();
		c.setCid(map.get("cid"));
		p.setCategory(c);
		BeanUtils.populate(p, map);
		return p;
	}
	
	
	public String update(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String pid = request.getParameter("pid");
		Product p = getProductFrom(request);
		p.setPid(pid);
		p.getCategory().setCid(p.getCategory().getCid());
		//调用service更新
		ps.updateAll(p);
		System.out.println(p.getPid());
		//重定向到list.jsp
		response.sendRedirect(request.getContextPath()+"/adminProductServlet?method=findAll");
		return null;
	}
	public String find(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		int flag = Integer.parseInt(request.getParameter("flag"));
		//调用sevice查找返回list
		List<Product> list = ps.find(flag);
		//将list放入域中
		request.setAttribute("list", list);
		request.setAttribute("flag", flag);
		return "/admin/product/list.jsp";
	}
	
	
	public String under(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		//获取商品id
		String pid = request.getParameter("pid");
		//查找该商品
		Product p = ps.findById(pid);
		p.setPflag(0);
		//直接让商品下架
		ps.update(p);
		//重定向到list界面
		response.sendRedirect(request.getContextPath()+"/adminProductServlet?method=find&flag=1");
		return null;
	}
}
