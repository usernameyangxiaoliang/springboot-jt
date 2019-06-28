package com.jt.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;
import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 业务说明
	 * http://sso.jt.com/user/check/fghfghj/1?r=0.59189715002
	 * 返回值：SysResult
	 * 由于跨域请求所以返回值必须特殊处理callback(json)
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,@PathVariable Integer type,String callback) {
		
		JSONPObject object = null;
		try {
			Boolean flag= userService.checkUser(param,type);
			object = new JSONPObject(callback, SysResult.ok(flag));
		} catch (Exception e) {
			e.printStackTrace();
			object = new JSONPObject(callback, SysResult.fail());
		}
		return object;
	}
	
	@RequestMapping("/register")
	public SysResult doRegister(String json) {
		try {
			User user = ObjectMapperUtil.toObject(json, User.class);
			userService.doRegister(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	//利用跨域实现用户信息回显
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,String callback) {
		
		String userJSON = jedisCluster.get(ticket);
		System.out.println(userJSON);
		if(StringUtils.isEmpty(userJSON))
			//回传数据需要经过200判断 SysResult对象
			return new JSONPObject(callback, SysResult.fail());
		else 
			return new JSONPObject(callback, SysResult.ok(userJSON));
	}

}
