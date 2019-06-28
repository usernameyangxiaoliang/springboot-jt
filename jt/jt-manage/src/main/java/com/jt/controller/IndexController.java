package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	/*
	 * /restFul:
	 *  1.要求参数必须使用/分隔
		2.参数位置要固定
		3.接收参数时必须使用特定的注解，并且名称保持一致
	 */
	  @RequestMapping("/page/{moduleName}") 
	  public String module(@PathVariable String moduleName) {
	  
	  return moduleName; }
	  
	 
}
