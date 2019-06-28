package com.jt.interceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;
@Component
public class UserInterceptor implements HandlerInterceptor{
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 在spring4版本中要求必须重写3个方法，不管是否需要
	 * 在spring5版本中在接口中添加default属性，则省略不写
	 * true:拦截放行
	 * false:请求拦截,重定向到登录页面
	 * 业务逻辑：
	 * 1.获取cookie数据
	 * 2.从cookie中获取登录凭证token（TICKET）
	 * 3.判断redis缓存服务器中是否有数据，
	 */
	
	@Override
	 //业务逻辑执行之前拦截
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Cookie[] cookies = request.getCookies();
		String token=null;
		for (Cookie cookie : cookies) {
			if("JT_TICKET".equals(cookie.getName())) {
				token = cookie.getValue();
				break;
			}
		}
		//判断token是否有数据
		if(!StringUtils.isEmpty(token)) {
			String userJSON = jedisCluster.get(token);
			if(!StringUtils.isEmpty(userJSON)) {
				//redis中有用户数据 
				//将userJSON转成对象
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				//将数据保存到resquest域中
				//request.setAttribute("JT_USER", user);
				//包数据保存到ThreadLoacl线程
				UserThreadLocal.set(user);
				return true;
			}
		}
		//重定向到用户登录页面
		response.sendRedirect("/user/login.html");
		// 表示拦截
		return false;
	}
	
	@Override
	//进行试图渲染后进行拦截,一般用于数据存在session中对session进行消亡
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//request.getSession().removeAttribute(name);
		UserThreadLocal.remove();
	}
	
	
	
	
	
	
	
}
