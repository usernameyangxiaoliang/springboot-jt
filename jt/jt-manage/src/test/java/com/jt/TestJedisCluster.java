package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class TestJedisCluster {
	
	@Test
	public void test02() {
		
		Set<HostAndPort> setsAndPorts = new HashSet<>();
		setsAndPorts.add(new HostAndPort("192.168.161.129", 7000));
		setsAndPorts.add(new HostAndPort("192.168.161.129", 7001));
		setsAndPorts.add(new HostAndPort("192.168.161.129", 7002));
		setsAndPorts.add(new HostAndPort("192.168.161.129", 7003));
		setsAndPorts.add(new HostAndPort("192.168.161.129", 7004));
		setsAndPorts.add(new HostAndPort("192.168.161.129", 7005));
		@SuppressWarnings("resource")
		JedisCluster  cluster = new JedisCluster(setsAndPorts);
		cluster.set("1902", "DSB是坏人");
		System.out.println("获取集群数据："+cluster.get("1902"));
	}

}
