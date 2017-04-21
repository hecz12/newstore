package com.store.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.Request;

import com.store.domain.Cart;
import com.store.domain.CartItem;
import com.store.domain.Product;
import com.store.service.ProductService;
import com.store.service.impl.ProductServiceImpl;
import com.store.utils.BeanFactory;


/***
 * 购物车相关servlet
 * @author 何长治
 *
 */
public class CartServlet extends BaseServlet {
	ProductService ps = (ProductService) BeanFactory.getBean("ProductService");
	
	/***
	 *添加商品到购物车中 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		System.out.println(request.getParameterMap());
		//获取商品id和数量
		String pid = request.getParameter("pid");
		int count = Integer.parseInt(request.getParameter("count"));
		
		//调用service得到该方法
		Product product = ps.getProductById(pid);
		CartItem item = new CartItem(product, count);
		//调用getCart方法，获取
		Cart cart = getCart(request);
		//将cartItem加入到cart的map中
		cart.add2Cart(item);
		//将cartItem放入session
		request.getSession().setAttribute("cart", cart);
		//重定向到cart中
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		return null;
	}
	/***
	 * 删除一个购物车项
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//1.获取商品编号
		String pid = request.getParameter("pid");
		//2.获取cart对象
		Cart cart = getCart(request);
		//3.调用移除方法
		cart.remove2Cart(pid);
		//重定向到此页面
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		
		return null;
	}
	/***
	 * 清空购物车
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String clear(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		//1.获取cart对象
		Cart cart = getCart(request);
		//3.调用清空方法
		cart.clearCart();
		//重定向到此页面
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		
		return null;
	}
	
	/***
	 * 从session中获得cart，add方法的工具类
	 * @param request
	 * @return
	 */
	private Cart getCart(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart==null)
		{
			//创建一个cart
			cart = new Cart();
			//添加到session中
			session.setAttribute("cart", cart);
		}
		return cart;
	}
	
	
	public String cartUI(HttpServletRequest request, HttpServletResponse response)
	{
		return "/jsp/cart.jsp";
	}
}
