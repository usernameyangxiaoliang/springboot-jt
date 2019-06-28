package com.jt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@RequestMapping("/getmanage")
	public String getMsg() {
		return "8091.你是坏人";
	}

}
