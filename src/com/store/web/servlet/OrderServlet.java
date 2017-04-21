package com.store.web.servlet;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.store.domain.Cart;
import com.store.domain.CartItem;
import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.domain.PageBean;
import com.store.domain.Product;
import com.store.domain.User;
import com.store.service.OrderService;
import com.store.service.impl.OrderServiceImpl;
import com.store.utils.BeanFactory;
import com.store.utils.PaymentUtils;
import com.store.utils.UUIDUtils;



public class OrderServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	OrderService os = (OrderService) BeanFactory.getBean("OrderService");
	
	/***
	 * 添加订单
	 * @param request
	 * @param response
	 * @return
	 */
	public String add(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		//1.判断用户是否登录
		User user = (User) session.getAttribute("user");
		if(user==null)
		{
			request.setAttribute("msg", "快去登录吧");
			return "/jsp/msg.jsp";
		}
		//2.获取购物车对象
		Cart cart = (Cart) session.getAttribute("cart");
		//3.封装订单数据
		Order order = new Order();
		//3.1获取随机id
		order.setOid(UUIDUtils.getId());
		//3.2获取当前时间
		order.setOrderTime(new Date());
		//3.3根据购物车获取总价
		order.setTotal(cart.getTotal());
		//3.4根据user获取用户
		order.setUser(user);
		//4.封装订单项数据
		//4.1对cart进行遍历
		for(CartItem cartItem:cart.getCartItem())
		{
			OrderItem orderItem = new OrderItem();
			//设置商品
			orderItem.setProduct(cartItem.getProduct());
			//设置商品数量
			orderItem.setCount(cartItem.getCount());
			//设置小计
			orderItem.setSubtotal(cartItem.getSubtotal());
			//设置单项id
			orderItem.setItemId(UUIDUtils.getId());
			//设置属于哪个订单
			orderItem.setOrder(order);
			//将购物项添加到订单内
			order.getItems().add(orderItem);
		}
		//5.调用service方法存入订单和订单项
		try {
			os.add(order);
		} catch (Exception e) {
			request.setAttribute("msg", "订单提交出现异常");
			return "/jsp/msg.jsp";
		}
		//6.将订单放入request域中
		request.setAttribute("order", order);
		return "/jsp/order_info.jsp";
	}
	
	/***
	 * 根据分页所有order
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findAllOrder(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		int currPage = 1;
		//获取当前页码
		try{
			currPage= Integer.parseInt(request.getParameter("currPage"));
		}
		catch(NumberFormatException e)
		{
			currPage=1;
		}
		User user = (User) request.getSession().getAttribute("user");
		//判断用户是否登陆
		if(user==null)
		{
			request.setAttribute("msg", "请先登录");
			return "/jsp/msg.jsp";
		}
		//设置pageSize
		int pageSize = 3;
		//查询订单总条数
		int totalCount = os.findOrderCount(user.getUid());
		//查询一页里的所有订单(包括订单中的订单项)
		List<Order> orders = os.findAllOrder(currPage,pageSize,user);
		System.out.println("订单数量"+orders.size());
		//获取PageBean对象
		PageBean<Order> pageBean = new PageBean<Order>(pageSize, currPage, totalCount, orders);
		//向request域中放入pagebean
		request.setAttribute("pb", pageBean);
		return "/jsp/order_list.jsp";
	}
	
	/***
	 * 根据id获取订单对象
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getById(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		//获取oid
		String oid = request.getParameter("oid");
		//根据oid获取单个Order对象
		Order order = os.getById(oid);
		System.out.println(order);
		//将订单放入request域中
		request.setAttribute("order", order);
		System.out.println(order.getTotal());
		return "/jsp/order_info.jsp";
	}
	
	
	public String pay(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String address=request.getParameter("address");
		String name=request.getParameter("name");
		String telephone=request.getParameter("telephone");
		String oid=request.getParameter("oid");
		Order order = os.getById(oid);
		order.setAddress(address);
		order.setName(name);
		order.setTelephone(telephone);
		order.setState(1);
		System.out.println(order);
		//更新order
		os.updateOrder(order);
		request.setAttribute("msg","支付成功" );
		return "/jsp/msg.jsp";
	}
	/***
	 * 获取所有订单数据以及银行数据，更新order，并发送给易宝()没有易宝账号，哭死）
	 * @param request
	 * @param respone
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String pay1(HttpServletRequest request,HttpServletResponse respone) throws Exception{
		//接受表单参数
		String address=request.getParameter("address");
		String name=request.getParameter("name");
		String telephone=request.getParameter("telephone");
		String oid=request.getParameter("oid");
		System.out.println(address);
		System.out.println(name);
		System.out.println(telephone);
		

		Order order = os.getById(oid);
		
		order.setAddress(address);
		order.setName(name);
		order.setTelephone(telephone);
		System.out.println(order);
		//更新order
		os.updateOrder(order);
		

		// 组织发送支付公司需要哪些数据
		String pd_FrpId = request.getParameter("pd_FrpId");
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = oid;
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("responseURL");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");
		String hmac = PaymentUtils.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
	
		
		//发送给第三方
		StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
		sb.append("p0_Cmd=").append(p0_Cmd).append("&");
		sb.append("p1_MerId=").append(p1_MerId).append("&");
		sb.append("p2_Order=").append(p2_Order).append("&");
		sb.append("p3_Amt=").append(p3_Amt).append("&");
		sb.append("p4_Cur=").append(p4_Cur).append("&");
		sb.append("p5_Pid=").append(p5_Pid).append("&");
		sb.append("p6_Pcat=").append(p6_Pcat).append("&");
		sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
		sb.append("p8_Url=").append(p8_Url).append("&");
		sb.append("p9_SAF=").append(p9_SAF).append("&");
		sb.append("pa_MP=").append(pa_MP).append("&");
		sb.append("pd_FrpId=").append(pd_FrpId).append("&");
		sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
		sb.append("hmac=").append(hmac);
		
		respone.sendRedirect(sb.toString());
		
		return null;
	}
	/***
	 * 支付后的回调函数
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String callback(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rb_BankId = request.getParameter("rb_BankId");
		String ro_BankOrderId = request.getParameter("ro_BankOrderId");
		String rp_PayDate = request.getParameter("rp_PayDate");
		String rq_CardNo = request.getParameter("rq_CardNo");
		String ru_Trxtime = request.getParameter("ru_Trxtime");
		// 身份校验 --- 判断是不是支付公司通知你
		String hmac = request.getParameter("hmac");
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
				"keyValue");

		// 自己对上面数据进行加密 --- 比较支付公司发过来hamc
		boolean isValid = PaymentUtils.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		if (isValid) {
			// 响应数据有效
			if (r9_BType.equals("1")) {
				// 浏览器重定向
				System.out.println("111");
				request.setAttribute("msg", "您的订单号为:"+r6_Order+",金额为:"+r3_Amt+"已经支付成功,等待发货~~");
				
			} else if (r9_BType.equals("2")) {
				// 服务器点对点 --- 支付公司通知你
				System.out.println("付款成功！222");
				// 修改订单状态 为已付款
				// 回复支付公司
				response.getWriter().print("success");
			}
			
			//修改订单状态
			OrderService s=(OrderService) BeanFactory.getBean("OrderService");
			Order order = s.getById(r6_Order);
			order.setState(1);
			
			s.updateOrder(order);
			
		} else {
			// 数据无效
			System.out.println("数据被篡改！");
		}
		return "/jsp/msg.jsp";
	}
	
	public String updateState(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//接受订单id和订单状态
		String oid = request.getParameter("oid");
		int state = Integer.parseInt(request.getParameter("state"));
		//获取订单
		Order order = os.getById(oid);
		//封装订单
		order.setState(state);
		//改变订单状态
		os.updateOrder(order);
		request.setAttribute("order", order);
		return "/jsp/order_info.jsp";
	}
	
}
