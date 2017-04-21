package com.store.domain;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
public class Cart {
	private Map<String,CartItem> cartMap = new LinkedHashMap<String,CartItem>();
	private Double total = 0.0;
	
	
	public Collection<CartItem> getCartItem()
	{
		return cartMap.values();
	}
	//添加商品
	/***
	 * 添加商品
	 * @param item
	 */
	public void add2Cart(CartItem item)
	{
		String pid = item.getProduct().getPid();
		//判断购物车是否包含此商品
		if(cartMap.containsKey(pid))
		{
			CartItem temp = cartMap.get(pid);
			temp.setCount(item.getCount()+temp.getCount());
		}
		else
		{
			cartMap.put(item.getProduct().getPid(),item);
		}
		//改变总价
		total+=item.getSubtotal();
	}
	/**
	 * 删除一个购物车项
	 * @author 何长治
	 * @param pid商品编号
	 */
	public void remove2Cart(String pid)
	{
		//删除该项
		CartItem item = cartMap.remove(pid);
		//改变总价
		total -= item.getSubtotal(); 
	}
	
	public void clearCart()
	{
		//情空map集合
		cartMap.clear();
		//总价清零
		total = 0.0;
	}
	
	public Map<String, CartItem> getCartMap() {
		return cartMap;
	}
	public void setCartMap(Map<String, CartItem> cartMap) {
		this.cartMap = cartMap;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
	
}
