package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class TestSentinel {
	@Test
	public  void text01() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.161.129:26379");

		//测试哨兵get/set操作 
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = sentinelPool.getResource();
		jedis.set("www", "DSB不能毕业");
		System.out.println(jedis.get("ww"));
		jedis.close();
	}
	
}
