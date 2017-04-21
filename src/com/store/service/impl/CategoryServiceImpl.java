package com.store.service.impl;

import java.io.InputStream;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.store.dao.CategoryDao;
import com.store.dao.ProductDao;
import com.store.domain.Category;
import com.store.domain.Product;
import com.store.service.CategoryService;
import com.store.utils.BeanFactory;
import com.store.utils.DataSourceUtils;
import com.store.web.servlet.CategoryServlet;

public class CategoryServiceImpl implements CategoryService {
	private CategoryDao cDao = (CategoryDao) BeanFactory.getBean("CategoryDao");
	private ProductDao pd = (ProductDao) BeanFactory.getBean("ProductDao");
	public List<Category> findAll() throws Exception {
		//读取缓存配置文件
		InputStream in = CategoryServlet.class.getClassLoader().getResourceAsStream("ehcache.xml");		
		//通过配置文件创建缓存
		CacheManager manage = CacheManager.create(in);
		Cache cache = manage.getCache("categoryCache");
		//获取缓存中相应的数据
		Element element = cache.get("clist");
		List<Category> clist = null;
		//判断数据是否为空，若为空，则从数据库取，然后放入缓存
		if(element == null)
		{
			//向数据库中查询分类信息
			clist = cDao.findAllCategory();
			cache.put(new Element("clist", clist));
			System.out.println("缓存中没有数据");
		}
		else
		{
			clist = (List<Category>) element.getObjectValue(); 
			System.out.println("向缓存中取数据");
		}
		
		return clist;
	}
	public void add(Category c) throws Exception {
		//调用dao更新
		cDao.add(c);
		//清空缓存
		//读取缓存配置文件
		InputStream in = CategoryServlet.class.getClassLoader().getResourceAsStream("ehcache.xml");		
		//通过配置文件创建缓存
		CacheManager manage = CacheManager.create(in);
		Cache cache = manage.getCache("categoryCache");
		cache.remove("clist");
	}
	public Category getById(String cid) throws Exception {
		//调用dao查询
		Category c = cDao.getById(cid);
		return c;
	}
	public void update(Category c) throws Exception {
		//调用dao更新
		cDao.update(c);
		//清空缓存
		//读取缓存配置文件
		InputStream in = CategoryServlet.class.getClassLoader().getResourceAsStream("ehcache.xml");		
		//通过配置文件创建缓存
		CacheManager manage = CacheManager.create(in);
		Cache cache = manage.getCache("categoryCache");
		cache.remove("clist");
	}
	
	public void delete(String cid) throws Exception {
		try{
		//调用事务
		DataSourceUtils.startTransaction();
		//调用更新商品操作
		pd.updateByCid(cid);
		//调用删除分类操作
		cDao.delete(cid);
		//结束事物
		DataSourceUtils.commitAndClose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//回滚事物
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
		//清空缓存
		//读取缓存配置文件
		InputStream in = CategoryServlet.class.getClassLoader().getResourceAsStream("ehcache.xml");		
		//通过配置文件创建缓存
		CacheManager manage = CacheManager.create(in);
		Cache cache = manage.getCache("categoryCache");
		cache.remove("clist");
	}

}
