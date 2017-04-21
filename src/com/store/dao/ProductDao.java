package com.store.dao;

import java.util.LinkedList;
import java.util.List;

import com.store.domain.Product;

public interface ProductDao {

	List<Product> findHotProduct() throws Exception;

	List<Product> findNewProduct() throws Exception;

	Product getProductById(String pid) throws Exception;

	int getTotalCount(String cid) throws Exception;

	List<Product> findByPage(int pageSize, int currPage, String cid) throws Exception;

	List<Product> getHistory(LinkedList<String> list) throws Exception;
	
	void updateByCid(String cid) throws Exception;

	List<Product> findAll() throws Exception;

	void add(Product p) throws Exception;

	Product findById(String pid) throws Exception;

	void update(Product p) throws Exception;

	String getCidByPid(String pid) throws Exception;

	void updateAll(Product p) throws Exception;

	List<Product> find(int flag) throws Exception;
}
