package com.jt.service;
import com.jt.pojo.User;

//定义中立的接口
public interface DubboUserService {

	void doRegister(User user);

	String findUserByUP(User user);
}
