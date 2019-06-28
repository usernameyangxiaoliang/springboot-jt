package com.jt.annotion;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//定义一个查询注解
import com.jt.enu.KEY_ENUM;
@Target({ElementType.METHOD,ElementType.FIELD})//注解作用的范围
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache_Find {
	String key() default "";//接收用户key值
	KEY_ENUM keyType() default KEY_ENUM.AUTO;//定义key的类型
	int secondes() default 0;//表示永不失效
}
