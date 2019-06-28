package com.jt;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHttpClient {
	//@Autowired
	//private CloseableHttpClient client;
	
	/*
	 * 1.实例化httpClient对象
	 * 2.定义http请求路径url
	 * 3.定义请求方式get/post
	 * 4.利用API发起http请求
	 * 5.获取返回值以后判断状态信息200
	 * 6.获取响应的数据
	 * 
	 */
	/*
	 * @Test public void testGet() throws ClientProtocolException, IOException {
	 * //CloseableHttpClient client = HttpClients.createDefault(); String url =
	 * "http://www.baidu.com"; HttpGet httpGet = new HttpGet(url);
	 * CloseableHttpResponse response = client.execute(httpGet);
	 * 
	 * if(response.getStatusLine().getStatusCode()==200) {
	 * System.out.println("DSB变成BSD"); HttpEntity httpEntity = response.getEntity();
	 * String result = EntityUtils.toString(httpEntity); System.out.println(result);
	 * }else { throw new RuntimeException(); } }
	 */
	@Autowired
	private HttpClientService httpClientService;
	//测试工具API
	@Test
	public void test() {
		String url = "https://www.baidu.com";
		String result = httpClientService.doGet(url);
		//String result = httpClientService.doPost(url);
		System.out.println("获取结果"+result);
	}
	
	
	

}
