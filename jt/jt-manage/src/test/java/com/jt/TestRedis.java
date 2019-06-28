package com.jt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import redis.clients.jedis.Jedis;
public class TestRedis {
	
	
	//String类型操作方式
	//ip和端口
	@Test
	public void testString() {
		Jedis jedis = new Jedis("192.168.161.129",6379);
		jedis.set("1902", "1902班");
		jedis.get("1902");
		jedis.expire("1902", 10);
		System.out.println(jedis.get("1902"));
	}
	//设定数据的超时时间方法
	@Test
	public void testOut() throws InterruptedException {
		Jedis jedis = new Jedis("192.168.161.129",6379);
		jedis.setex("aa",2,"aa");
		System.out.println(jedis.get("aa"));
		Thread.sleep(3000);
		Long setnx = jedis.setnx("aa","bb");//加分布式锁
		System.out.println("获取输出的数据"+setnx+":"+jedis.get("aa"));
	}
	
	//对象转JSON
	/*
	 * 1.首先获取getXXX方法
	 * 2.将get去掉，首字母小写
	 */
	
	@Test
	public void objectToJSON() throws IOException {
		ItemDesc itemDesc = new ItemDesc(); 
		itemDesc.setItemId(100L).setItemDesc("测试数据");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(itemDesc);
		System.out.println(json);
		//JSON转对象
		/**
		 * 1.获取对象JSON串
		 * 2.解析json获取key
		 * 3.利用反射机制创建实例对象
		 * 4.根据key调用set方法为对象赋值
		 * 5.可以用@JsonIgnoreProperties(ignoreUnknown = true)忽略未匹配的属性
		 * @throws IOException
		 */
		ItemDesc ItemDesc2 = mapper.readValue(json, ItemDesc.class);
		System.out.println(ItemDesc2);
	}
	
	//实现list集合与json转化
	@SuppressWarnings("unchecked")
	@Test
	public void listJSON() throws IOException {
		ItemDesc itemDesc2 = new ItemDesc(); 
		itemDesc2.setItemId(100L).setItemDesc("测试数据");
		ItemDesc itemDesc3 = new ItemDesc(); 
		itemDesc3.setItemId(100L).setItemDesc("测试数据");
		List<ItemDesc> list = new ArrayList<>();
		list.add(itemDesc2);
		list.add(itemDesc3);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(list);
		System.out.println(json);
		//把json存入redis中
		Jedis jedis = new Jedis("192.168.161.129",6379);
		jedis.set("itemDescList", json);
		
		//从redis中获取数据
		String result = jedis.get("itemDescList");
		List<ItemDesc> descList = mapper.readValue(result, list.getClass());
		System.out.println(descList);
	}

	
	//利用redis保存业务数据 数据库数据：对象格式
	//String类型要求只能存储字符串类型  对象转化成JSON串
	public void testSetObject() {
		Item item = new Item();
		Jedis jedis = new Jedis("192.168.161.129",6379);
	}
	

}
