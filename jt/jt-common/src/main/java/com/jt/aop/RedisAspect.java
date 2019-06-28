package com.jt.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.jt.annotion.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.RedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;
@Component//交给spring容器管理
@Aspect//标识切面
public class RedisAspect {
	//容器初始化时不需要实例化该对象，只有用户使用时才初始化。一般工具类中添加该注解
	//private Jedis jedis;
	//private ShardedJedis shards;
	@Autowired(required=false)
	//private RedisService shards;
	private JedisCluster shards;
	@SuppressWarnings("unchecked")
	@Around("@annotation(cache_Find)")//使用该方法可直接获取注解对象
	public Object around(ProceedingJoinPoint jp,Cache_Find cache_Find) {
		//1.获取key的值
		String key = getKey(jp,cache_Find);
		//2.根据key查缓存
		String result = shards.get(key);
		Object data =null;
			try {
				if(StringUtils.isEmpty(result)) {
					//执行业务方法
				data=jp.proceed();
				//将数据转化为json串
				String json = ObjectMapperUtil.toJSON(data);
				//判断用户是否设定超时时间
				if(cache_Find.secondes()==0)
					//表示不要超时
					shards.set(key,json);
					else 
						shards.setex(key,cache_Find.secondes(),json);
				System.out.println("从数据查数据");
			}else {
				//缓存中有数据，则从缓存中取，将json转成对象
				Class targetClass= getClass(jp);
				data=ObjectMapperUtil.toObject(result, targetClass);
				System.out.println("从缓存中查 ");
			} 
			}catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		return data;
		
	}
	private Class getClass(ProceedingJoinPoint jp) {
		MethodSignature signature = (MethodSignature) jp.getSignature();
		return signature.getReturnType();
	}
	private String getKey(ProceedingJoinPoint jp, Cache_Find cache_Find) {
		//1.判断用户key的类型,获取key的类型
		KEY_ENUM key_ENUM = cache_Find.keyType();
		//2.判断key类型
		if(key_ENUM.equals(key_ENUM.EMPTY)) {
			//表示使用用户自己的key
			return cache_Find.key();
		}
		//表示用户的key需要拼接key+“_”+第一个参数
		String  strArgString = String.valueOf(jp.getArgs()[0]);
		String key = cache_Find.key()+"_"+strArgString;
		return key;
	}

}
