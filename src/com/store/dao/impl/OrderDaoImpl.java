package com.store.dao.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.store.dao.OrderDao;
import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.utils.DataSourceUtils;
/*
 * `oid` varchar(32) NOT NULL,
  `ordertime` datetime DEFAULT NULL,
  `total` double DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `address` varchar(30) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `uid` varchar(32) DEFAULT NULL,
 */
public class OrderDaoImpl implements OrderDao {
	QueryRunner qr = new QueryRunner();
	/***
	 * @param order 订单对象
	 * 向订单表中插入数据
	 */
	public void addOrder(Order order) {
		System.out.println("order中的oid"+order.getOid());
		String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
		try {
			qr.update(DataSourceUtils.getConnection(), sql, order.getOid(),new java.sql.Date(order.getOrderTime().getTime()),order.getTotal(),
					order.getState(),order.getAddress(),order.getName(),
					order.getTelephone(),order.getUser().getUid());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * `itemid` varchar(32) NOT NULL,
	  `count` int(11) DEFAULT NULL,
	  `subtotal` double DEFAULT NULL,
	  `pid` varchar(32) DEFAULT NULL,
	  `oid` varchar(32) DEFAULT NULL,
	 */
	public void addOrderItem(OrderItem oitem) {
		System.out.println("oitem中的oid"+oitem.getOrder().getOid());
		String sql = "insert into orderitem values(?,?,?,?,?)";
		try {
			qr.update(DataSourceUtils.getConnection(), sql, oitem.getItemId(),oitem.getCount(),oitem.getSubtotal(),
					oitem.getProduct().getPid(),oitem.getOrder().getOid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/***
	 * 找到此页所有订单
	 */
	public List<Order> findAllOrder(int currPage, int pageSize, String uid) {
		QueryRunner qr1 = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders where uid = ? order by ordertime desc limit ?,?";
		List<Order> list = new LinkedList<Order>();
		try {
			list = qr1.query(sql, new BeanListHandler<Order>(Order.class),uid,(currPage-1)*pageSize,pageSize);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	//根据订单id查找订单项
	public List<Map<String,Object>> findOrderItemsByOrderId(String oid) {
		QueryRunner qr1 = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orderitem oi,product p where oi.pid = p.pid and oid = ?";
		List<Map<String,Object>> map = new LinkedList<Map<String,Object>>();
		try {
			map = qr1.query(sql, new MapListHandler(),oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	//查询订单数量
	public int findOrderCount(String uid) {
		QueryRunner qr1 = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from orders where uid = ?";
		int count = 0;
		try {
			 count = ((Long)qr1.query(sql, new ScalarHandler(), uid)).intValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	public Order getOrder(String oid) {
		QueryRunner qr1 = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders where oid = ? limit 1";
		Order order = new Order();
		try {
			order = qr1.query(sql, new BeanHandler<Order>(Order.class), oid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return order;
	}
	
	public void update(Order order)
	{
		QueryRunner qr1 = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update orders set state=?,name=?,address=?,telephone=? where oid=?";
		System.out.println(order);
		System.out.println(order.getState());
		System.out.println(order.getName());
		System.out.println(order.getAddress());
		System.out.println(order.getTelephone());
		try {
			qr1.update(sql, order.getState(),order.getName(),order.getAddress(),order.getTelephone(),order.getOid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Order> findAllOrderByState(String str) throws Exception {
		QueryRunner qr1 = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = null;
		if(str==null)
		{
			sql = "select * from orders";
		}
		else
		{
			int state = Integer.parseInt(str);
			sql = "select * from orders where state = "+state;
		}
		List<Order> list = qr1.query(sql, new BeanListHandler<Order>(Order.class));
		return list;
	}

}
