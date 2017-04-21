package com.store.service.impl;

import org.apache.commons.beanutils.BeanUtils;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.store.dao.OrderDao;
import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.domain.Product;
import com.store.domain.User;
import com.store.service.OrderService;
import com.store.utils.BeanFactory;
import com.store.utils.DataSourceUtils;

public class OrderServiceImpl implements OrderService {
	private OrderDao od = (OrderDao) BeanFactory.getBean("OrderDao");
	public void add(Order order) throws Exception {
		try{
			//1.开启事务，因为订单和订单项添加到表必然是同步操作
			DataSourceUtils.startTransaction();
			//2.将订单存入订单表
			od.addOrder(order);
			//3.遍历将订单项存入订单项表
			for(OrderItem oitem:order.getItems())
			{
				od.addOrderItem(oitem);
			}
			//4.提交并释放连接
			DataSourceUtils.commitAndClose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//回滚事物并释放连接
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
	}
	/***
	 * 获取订单项的数量
	 * @exception Exception
	 */
	public int findOrderCount(String uid) throws Exception {
		int count = od.findOrderCount(uid);
		return count;
	}
	
	/***
	 * 获一页中的所有订单以及其中的订单项
	 * @param currPage
	 * @param pageSize
	 * @param user
	 */
	public List<Order> findAllOrder(int currPage, int pageSize, User user)
			throws Exception {
		//找到所有订单表
		List<Order> orders = od.findAllOrder(currPage,pageSize,user.getUid());
		//找到所有的订单项
		for (Order order : orders) {
			List<Map<String,Object>> map = od.findOrderItemsByOrderId(order.getOid());
			//遍历查找到的orderitem和product的map数组
			for (Map<String, Object> item : map) {
				//生成product对象，方便装入orderitem中
				Product p = new Product();
				BeanUtils.populate(p,item);
				//生成oitem，可以不用装入order，但要装入product
				OrderItem oi = new OrderItem();
				BeanUtils.populate(oi, item);
				oi.setProduct(p);
				//将生成的oitem装入order的list中
				order.getItems().add(oi);
			}
			order.setUser(user);
		}
		return orders;
	}
	public Order getById(String oid) throws Exception {
		//获取单个order对象
		Order order = od.getOrder(oid);
		//根据oid获取所有的orderitem加map的集合
		List<Map<String,Object>> list = od.findOrderItemsByOrderId(oid);
		//便利map
		for (Map<String, Object> map : list) {
			//生成product对象，方便装入orderitem中
			Product p = new Product();
			BeanUtils.populate(p, map);
			//生成oitem，可以不用装入order，但要装入product
			OrderItem oi = new OrderItem();
			BeanUtils.populate(oi, map);
			oi.setProduct(p);
			//将生成的oitem装入order的list中
			order.getItems().add(oi);
		}
		return order;
	}
	public void updateOrder(Order order) throws Exception {
		//更新order
		od.update(order);
	}
	public List<Order> findAllOrderByState(String str) throws Exception {
		List<Order> list = od.findAllOrderByState(str);
		return list;
	}

}
