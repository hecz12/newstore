package com.store.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSourceUtils {
	private static ComboPooledDataSource ds = new ComboPooledDataSource();
	private static ThreadLocal<Connection> tl=new ThreadLocal<Connection>();
	/***
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		//通过threadlocal存取connection，保存读取安全性
		Connection conn = tl.get();
		if(conn==null){
			conn=ds.getConnection();
			//如果ThreadLocal中还没有连接，则向其放入connection
			tl.set(conn);
		}
		return conn;
	}

	// 获取数据源
	public static DataSource getDataSource() {
		return ds;
	}

	// 关闭数据集与语句
	public static void closeResource( Statement st, ResultSet rs) {
		closeResultSet(rs);
		closeStatement(st);
	}
	
	// 关闭所有资源
	public static void closeResource(Connection conn, Statement st, ResultSet rs) {
		closeResource(st, rs);
		closeConn(conn);
	}

	// 关闭connection
	public static void closeConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				tl.remove();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}

	// 关闭Statement对象
	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			st = null;
		}
	}

	// 关闭数据集
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
	}
	
	
	//开启事物
	public static void startTransaction() throws SQLException{
		getConnection().setAutoCommit(false);
	}
	
	/***
	 * 提交事物
	 */
	public static void commitAndClose(){
		Connection conn = null;
		try {
			conn=getConnection();
			//
			conn.commit();
			conn.close();
			tl.remove();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 回滚事物
	 */
	public static void rollbackAndClose(){
		Connection conn = null;
		try {
			conn=getConnection();
			
			conn.rollback();
			
			conn.close();
			
			tl.remove();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
