package com.jt.controller;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private JedisCluster jedis;
	//导入dubbo的用户接口
		@Reference(timeout=3000,check=false)
		private DubboUserService userService;
	@RequestMapping("/{modeleName}")
	public String index(@PathVariable String modeleName) {
		
		return modeleName;
	}
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult doRegister(User user) {
		try {
			userService.doRegister(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
		//实现用户登录
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult login(User user,HttpServletResponse response) {
		try {
			//调用sso系统获取秘钥
			String token = 
					userService.findUserByUP(user);
			//如果数据不为null时,将数据保存到Cookie中
			//cookie中的key固定值 JT_TICKET
			if(!StringUtils.isEmpty(token)) {
				Cookie cookie = 
						new Cookie("JT_TICKET", token);
				cookie.setMaxAge(7*24*3600);//生命周期
				cookie.setDomain("jt.com"); //实现数据共享
				cookie.setPath("/");
				response.addCookie(cookie);
				return SysResult.ok();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SysResult.fail();
	}
		/**
		 * 实现用户的登出操作
		 *  先删除redis,request对象cookie中的keyJT_TICKET
		 * @param req
		 * @param res
		 * @return
		 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest req,HttpServletResponse res) {
		
		Cookie[] cookies = req.getCookies();
		if(cookies.length!=0) {
			String token = null;
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
			//判断token数据是否为空,有值得话删除redis和cookie
			if(!StringUtils.isEmpty(token)) {
				jedis.del(token);
				Cookie cookie = new Cookie("JT_TICKET","");
				cookie.setMaxAge(0);
				cookie.setPath("/");
				cookie.setDomain("jt.com");
				res.addCookie(cookie);
			}
		}
		//当用户登出时，页面重定向到系统首页
		return "redirect:/";
		
	}
}
