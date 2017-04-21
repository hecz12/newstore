package com.store.service;

import java.util.List;

import com.store.domain.Order;
import com.store.domain.User;

public interface OrderService {

	void add(Order order) throws Exception;

	int findOrderCount(String uid) throws Exception;

	List<Order> findAllOrder(int currPage, int pageSize, User user) throws Exception;

	Order getById(String oid) throws Exception;

	void updateOrder(Order order) throws Exception;

	List<Order> findAllOrderByState(String str) throws Exception;

}
