package com.store.service.impl;

import java.util.LinkedList;
import java.util.List;

import com.store.dao.ProductDao;
import com.store.dao.impl.ProductDaoImpl;
import com.store.domain.PageBean;
import com.store.domain.Product;
import com.store.service.ProductService;
import com.store.utils.BeanFactory;

public class ProductServiceImpl implements ProductService {
	ProductDao pd = (ProductDao) BeanFactory.getBean("ProductDao");
	public List<Product> findHotProduct() throws Exception {
		List<Product> plist = pd.findHotProduct();
		return plist;
	}

	public List<Product> findNewProduct() throws Exception {
		List<Product> plist = pd.findNewProduct();
		return plist;
	}

	public Product getProductById(String pid) throws Exception {
		return pd.getProductById(pid);
	}

	public PageBean<Product> findByPage(String cid, int currPage,int PageSize)
			throws Exception {
		//获取totalcoun
		int totalCount = pd.getTotalCount(cid);
		//获取当前页面中的list列表
		List<Product> list = pd.findByPage(PageSize,currPage,cid);
		//返回PageBean
		return new PageBean<Product>(PageSize, currPage, totalCount, list);
	}

	public List<Product> findHistory(LinkedList<String> list) throws Exception {
		List<Product> pList = pd.getHistory(list);
		return pList;
	}

	public List<Product> findAll() throws Exception {
		List<Product> list = pd.findAll();
		return list;
	}

	public void add(Product p) throws Exception {
		pd.add(p);
	}

	public Product findById(String pid) throws Exception {
		Product p = pd.findById(pid);
		return p;
	}

	public void update(Product p) throws Exception {
		pd.update(p);
	}

	public String getCidByPid(String pid) throws Exception {
		String cid = pd.getCidByPid(pid);
		return cid;
	}

	public void updateAll(Product p) throws Exception {
		pd.updateAll(p);
	}

	public List<Product> find(int flag) throws Exception {
		List<Product> list = pd.find(flag);
		return list;
	}

	

}
