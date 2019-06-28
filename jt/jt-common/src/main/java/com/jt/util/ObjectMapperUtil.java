package com.jt.util;
import com.fasterxml.jackson.databind.ObjectMapper;
//编辑工具类实现对象与json转化
public class ObjectMapperUtil {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	//对象转json
	public static String toJSON(Object target) {
		try {
			return MAPPER.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			//将检查异常转化为运行异常
			throw new RuntimeException();
		}
	}
	//json转对象
	public static <T> T toObject(String json,Class<T> cls) {
		T data=null;
		try {
			data=MAPPER.readValue(json,cls);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			//将检查异常转化为运行异常
			throw new RuntimeException();
		}
	}

}
