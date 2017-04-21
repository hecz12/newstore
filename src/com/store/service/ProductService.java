package com.store.service;

import java.util.LinkedList;
import java.util.List;

import com.store.domain.PageBean;
import com.store.domain.Product;

public interface ProductService {

	List<Product> findHotProduct() throws Exception;

	List<Product> findNewProduct() throws Exception;

	Product getProductById(String pid) throws Exception;

	PageBean<Product> findByPage(String cid, int currPage, int pageSize) throws Exception;

	List<Product> findHistory(LinkedList<String> list) throws Exception;

	List<Product> findAll() throws Exception;

	void add(Product p) throws Exception;

	Product findById(String pid) throws Exception;

	void update(Product p) throws Exception;

	String getCidByPid(String pid) throws Exception;

	void updateAll(Product p) throws Exception;

	List<Product> find(int flag) throws Exception;

	
}
