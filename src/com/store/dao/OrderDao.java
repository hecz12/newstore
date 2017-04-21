package com.store.dao;

import java.util.List;
import java.util.Map;

import com.store.domain.Order;
import com.store.domain.OrderItem;

public interface OrderDao {

	void addOrder(Order order);

	void addOrderItem(OrderItem oitem);

	List<Order> findAllOrder(int currPage, int pageSize, String uid);

	List<Map<String, Object>> findOrderItemsByOrderId(String oid);

	int findOrderCount(String uid);

	Order getOrder(String oid);

	void update(Order order);

	List<Order> findAllOrderByState(String str) throws Exception;

}
