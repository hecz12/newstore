package com.store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.store.domain.Category;
import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.service.OrderService;
import com.store.service.impl.OrderServiceImpl;
import com.store.utils.JsonUtil;



public class AdminOrderServlet extends BaseServlet {
	private OrderService os = new OrderServiceImpl();
	/***
	 * 根据状态查找订单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findAllByState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String str = request.getParameter("state");
		//调用service
		List<Order> list = os.findAllOrderByState(str);
		//将返回的list存入域中
		request.setAttribute("list", list);
		return "/admin/order/list.jsp";
	}
	/***
	 * 改变订单状态
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//接受订单id和订单状态
		String oid = request.getParameter("oid");
		int state = Integer.parseInt(request.getParameter("state"));
		//获取订单
		Order order = os.getById(oid);
		//封装订单
		order.setState(state);
		//改变订单状态
		os.updateOrder(order);
		response.sendRedirect(request.getContextPath()+"/adminOrderServlet?method=findAllByState&state="+state);
		return null;
	}
	
	public String getDetailByOid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//接受订单id和订单状态
		String oid = request.getParameter("oid");
		//获取订单
		Order order = os.getById(oid);
		//取得订单项
		List<OrderItem> list = order.getItems();
		//改变json封装
		JsonConfig config = JsonUtil.configJson(new String[]{"order","class","itemid"});
		JSONArray array = JSONArray.fromObject(list, config);
		System.out.println(array);
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println(array);
		return null;
	}
}
