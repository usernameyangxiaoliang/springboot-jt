package com.jt.util;
import com.jt.pojo.User;
// ThreadLoacal是线程安全的
public class UserThreadLocal {
	
	private static ThreadLocal<User> threadLocal = new ThreadLocal<>();
	
	//新增数据
	public static void set(User user) {
		threadLocal.set(user);
	}
	//获取数据 
	public static User get() {
		return threadLocal.get();
	}
	
	//使用threadLocal要关闭资源
	
	public static void remove() {
		threadLocal.remove();
	}
	
}
