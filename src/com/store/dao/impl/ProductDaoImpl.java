package com.store.dao.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import sun.util.resources.ParallelListResourceBundle;

import com.store.dao.ProductDao;
import com.store.domain.Category;
import com.store.domain.Product;
import com.store.utils.DataSourceUtils;

public class ProductDaoImpl implements ProductDao {
	QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
	public List<Product> findHotProduct() throws Exception {
		String sql = "select * from product where is_hot=1 order by pdate desc limit 9";
		List<Product> plist = qr.query(sql, new BeanListHandler<Product>(Product.class));
		return plist;
	}

	public List<Product> findNewProduct() throws Exception {
		String sql = "select * from product order by pdate desc limit 9";
		List<Product> plist = qr.query(sql, new BeanListHandler<Product>(Product.class));
		return plist;
	}

	public Product getProductById(String pid) throws Exception {
		String sql = "select * from product where pid = ? limit 1";
		return qr.query(sql, new BeanHandler<Product>(Product.class),pid);
	}

	public int getTotalCount(String cid) throws Exception {
		String sql = "select count(*) from product where cid = ?";
		int totalCount = ((Long)qr.query(sql, new ScalarHandler(),cid)).intValue();
		return totalCount;
	}

	public List<Product> findByPage(int pageSize, int currPage, String cid)
			throws Exception {
		String sql = "select * from product where cid = ? limit ?,?";
		List<Product> list = qr.query(sql,new BeanListHandler<Product>(Product.class) ,cid,(currPage-1)*pageSize,pageSize);
		return list;
	}

	public List<Product> getHistory(LinkedList<String> list) throws Exception {
		String sql="select * from product where pid=?";
		String[][] params = new String[list.size()][];
		LinkedList<Product> pList = new LinkedList<Product>();
		for(int i = 0;i<list.size();i++)
		{
			Product pro = qr.query(sql, new BeanHandler<Product>(Product.class),list.get(i));
			pList.addLast(pro);
		}
		return pList;
	}

	public void updateByCid(String cid) throws Exception {
		String sql = "update product set cid = null where cid = ?";
		QueryRunner qr1 = new QueryRunner();
		qr1.update(DataSourceUtils.getConnection(), sql, cid);
	}

	public List<Product> findAll() throws Exception {
		String sql = "select * from product order by pdate desc";
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class));
		return list;
	}

	public void add(Product p) throws Exception {
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
		qr.update(sql, p.getPid(),p.getPname(),p.getMarket_price(),p.getShop_price()
				,p.getPimage(),new java.sql.Date(p.getPdate().getTime()),p.getIs_hot(),p.getPdesc(),
				p.getPflag(),p.getCategory().getCid());
	}

	public Product findById(String pid) {
		String sql = "select * from product where pid = ? limit 1"; 
		Product p = null;
		try {
			p = qr.query(sql, new BeanHandler<Product>(Product.class), pid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	/***
	 * private String pname;
	private Double market_price;
	private Double shop_price;
	private String pimage;
	private Date pdate;
	private Integer is_hot;//判断是否热门,1为热门，0为不热门
	private String pdesc;//商品描述
	private Integer pflag=0;//是否下架，1为下架，0为不下架
	private Category category;
	 */
	
	public void update(Product p) {
		String sql = "update product set pname=?,market_price=?,shop_price=?," +
			"pimage=?,pdate=?,is_hot=?,pdesc=?,pflag=? where pid=?";
		try {
			qr.update(sql, p.getPname(),p.getMarket_price(),p.getShop_price(),
					p.getPimage(),new java.util.Date(p.getPdate().getTime()),
					p.getIs_hot(),p.getPdesc(),
					p.getPflag(),p.getPid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCidByPid(String pid) throws Exception {
		String sql = "select cid from product where pid = ?";
		String cid = (String)qr.query(sql, new ScalarHandler(1), pid);
		return cid;
	}

	public void updateAll(Product p) throws Exception {
		String sql = "update product set pname=?,market_price=?,shop_price=?," +
				"pimage=?,pdate=?,is_hot=?,pdesc=?,pflag=?,cid=? where pid=?";
		qr.update(sql, p.getPname(),p.getMarket_price(),p.getShop_price(),
				p.getPimage(),new java.sql.Date(p.getPdate().getTime()),
				p.getIs_hot(),p.getPdesc(),p.getPflag(),p.getCategory().getCid(),p.getPid());
	}

	public List<Product> find(int flag) throws Exception {
		String sql = "select * from product where pflag = ?  order by pdate desc";
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class),flag);
		return list;
	}


	
}
