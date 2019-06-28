package com.jt.service;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public Boolean checkUser(String param, Integer type) {
		String column = type==1?"username":(type==2?"phone":"email");
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(column, param);
		Integer count = userMapper.selectCount(queryWrapper);
		return count==0?false:true;
	}
	@Transactional
	@Override
	public void doRegister(User user) {
		user.setEmail(user.getPhone())
		.setCreated(new Date())
		.setUpdated(user.getCreated());
	userMapper.insert(user);
	}
	
	
	
}
