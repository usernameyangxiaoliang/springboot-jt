package com.jt;

import org.apache.tomcat.jni.User;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestRedis1 {
	@Test
	public void testHash() {
		Jedis jedis = new Jedis("192.168.161.129");
		jedis.hset("user", "id", "2000");
		jedis.hset("user", "name", "tomcat");
		jedis.hset("user", "age", "20");
		String hget = jedis.hget("user", "name");
		System.out.println(hget);
	}
	//编辑list集合
	@Test
	public void list() {
		Jedis jedis = new Jedis("192.168.161.129",6379);
		jedis.lpush("list", "1","2","3","4","5");
		String rpop = jedis.rpop("list");
		System.out.println(rpop);
	}
	
	//事务控制
	@Test
	public void testshiwu() {
		Jedis jedis = new Jedis("192.168.161.129",6379);
		Transaction ts = jedis.multi();
		try {
			ts.set("aa", "aa");
			ts.set("bb", "bb");
			ts.exec();
		} catch (Exception e) {
			ts.discard();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	

}
