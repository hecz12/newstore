package com.store.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
/***
 * 
 * @author 49540
 *
 */
public class EncodingFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		//1.强转
		HttpServletRequest request=(HttpServletRequest) req;
		HttpServletResponse response=(HttpServletResponse) resp;
		
		//2.调用包装类实现静态代理，然后放行
		chain.doFilter(new MyRequest(request), response);
	}

	public void destroy() {

	}

}
//包装类，改造request的getParameter、getParameterMap、getParameterValues，主要为getParameterMap
//其他两个方法调用他们
class MyRequest extends HttpServletRequestWrapper{
	private HttpServletRequest request;
	private boolean flag=true;
	
	
	public MyRequest(HttpServletRequest request) {
		super(request);
		this.request=request;
	}
	
	@Override
	public String getParameter(String name) {  
		if(name==null || name.trim().length()==0){
			return null;
		}
		String[] values = getParameterValues(name);
		if(values==null || values.length==0){
			return null;
		}
		
		return values[0];
	}
	
	@Override
	/**
	 * hobby=[eat,drink]
	 */
	public String[] getParameterValues(String name) {
		if(name==null || name.trim().length()==0){
			return null;
		}
		Map<String, String[]> map = getParameterMap();
		if(map==null || map.size()==0){
			return null;
		}
		
		return map.get(name);
	}
	
	@Override
	/**
	 * 1.获取方法名称
	 * 2.如果为post请求，直接setCharacterEncoding
	 * 3.为get，则先用ISO-8859-1解码，再用UTF-8编码
	 */
	public Map<String,String[]> getParameterMap() {  
		
		/***
		 * 
		 */
		String method = request.getMethod();
		if("post".equalsIgnoreCase(method)){
			try {
				request.setCharacterEncoding("utf-8");
				return request.getParameterMap();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if("get".equalsIgnoreCase(method)){
			Map<String,String[]> map = request.getParameterMap();
			if(flag){
				for (String key:map.keySet()) {
					String[] arr = map.get(key);
					//对每一个参数中文编码
					for(int i=0;i<arr.length;i++){
						try {
							arr[i]=new String(arr[i].getBytes("iso8859-1"),"utf-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
				flag=false;
			}
			
			
			return map;
		}
		
		return super.getParameterMap();
	}
	
}