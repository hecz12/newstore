package com.store.dao.impl;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import com.store.dao.CategoryDao;
import com.store.domain.Category;
import com.store.utils.DataSourceUtils;
import com.sun.mail.util.BEncoderStream;

public class CategoryDaoImpl implements CategoryDao {
	QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
	public List<Category> findAllCategory() throws Exception {
		String sql = "select * from category";
		List<Category> clist = qr.query(sql, new BeanListHandler<Category>(Category.class));
		return clist;
	}
	public void add(Category c) throws Exception {
		String sql = "insert into category values(?,?)";
		qr.update(sql, c.getCid(),c.getCname());
	}
	public Category getById(String cid)
	{
		String sql = "select * from category where cid = ? limit 1";
		Category c = null;
		try {
			c = qr.query(sql, new BeanHandler<Category>(Category.class), cid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	public void update(Category c) throws Exception {
		String sql = "update category set cname=? where cid=?";
		qr.update(sql, c.getCname(),c.getCid());
	}
	public void delete(String cid) throws Exception {
		QueryRunner qr1 = new QueryRunner();
		String sql = "delete from category where cid = ?";
		qr1.update(DataSourceUtils.getConnection(), sql, cid);
	}
}
