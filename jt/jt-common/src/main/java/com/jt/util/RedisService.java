package com.jt.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
@Service
public class RedisService {
	@Autowired(required=false)//调用时才注入
	private JedisSentinelPool sentinelPool;
	
	//封装get方法
	public String get(String key) {
		Jedis jedis = sentinelPool.getResource();
		String result = jedis.get(key);
		jedis.close();
		return result;
	}
	//封装set方法
	public void set(String key,String value) {
		Jedis jedis = sentinelPool.getResource();
		jedis.set(key, value);
		jedis.close();
	}
	//封装setex方法
	public void setex(String key,String value,int secondes) {
		Jedis jedis = sentinelPool.getResource();
		jedis.setex(key,secondes, value);
		jedis.close();
	}
}
