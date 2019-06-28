package com.jt.config;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

//表示redis配置类
@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	/*单个redis
	 * @Value("${jedis.host}") private String host;
	 * 
	 * @Value("${jedis.port}") private Integer port;
	 * 
	 * @Bean 
	 * public Jedis jedis() { 
	 * return new Jedis(host, port);
	 * 
	 * }
	 */
	
	//redis分片
	/*
	 * @Value("${redis.nodes}") 
	 * private String redisNodes;
	 * 
	 * @Bean 
	 * public ShardedJedis shardedJedis() { 
	 * List<JedisShardInfo> shards = newArrayList<>(); 
	 * String[] nodes = redisNodes.split(","); 
	 * for (String node :nodes) { 
	 * String host = node.split(":")[0]; 
	 * int port =Integer.parseInt(node.split(":")[1]); 
	 * JedisShardInfo info = newJedisShardInfo(host,port); 
	 * shards.add(info);
	 *  } 
	 *  return newShardedJedis(shards); 
	 *  }
	 */
	//通过哨兵机制实现redis操作
	/*
	 * @Value("${redis.sentinels}") private String jedisSentinelNode;
	 * 
	 * @Value("${redis.sentinel.masterName}") private String masterName;
	 * 
	 * @Bean public JedisSentinelPool jedisSentinelPool() { Set<String> sentinels =
	 * new HashSet<>(); sentinels.add(jedisSentinelNode); return new
	 * JedisSentinelPool(masterName, sentinels); }
	 */
	@Value("${redis.nodes}")
	private String redisNodeString;
	@Bean
	public JedisCluster jedisCluster() {
		//按照，拆分
		Set<HostAndPort> nodes = new HashSet<>();
		String[] redisNodes = redisNodeString.split(",");
		for (String node : redisNodes) {
			String host=node.split(":")[0];
			int port =Integer.parseInt(node.split(":")[1]); 
			nodes.add(new HostAndPort(host, port));
		}
		return new JedisCluster(nodes);
	}

}
