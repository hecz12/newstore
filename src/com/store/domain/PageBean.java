package com.store.domain;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable {
	private int totalPage;
	private int pageSize;
	private int currPage;
	private int totalCount;
	private List<T> list;
	
	public PageBean(int pageSize, int currPage, int totalCount, List<T> list) {
		super();
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalCount = totalCount;
		this.list = list;
		this.totalPage = (int) Math.ceil(totalCount*1.0/pageSize);
	}
	public int getTotalPage() {
		return totalPage;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
	@Override
	public String toString() {
		return "PageBean [totalPage=" + totalPage + ", pageSize=" + pageSize
				+ ", currPage=" + currPage + ", totalCount=" + totalCount
				+ ", list=" + list + "]";
	}
}
