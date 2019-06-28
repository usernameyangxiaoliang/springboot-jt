package com.jt.controller.web;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.User;
@RestController
public class JSONPController {
	/*
	 * @RequestMapping("/web/testJSONP") public String testJSOP(String callback) {
	 * 
	 * ItemDesc itemDesc = new ItemDesc(); itemDesc.setItemDesc("452525");
	 * itemDesc.setCreated(new Date()); String json =
	 * ObjectMapperUtil.toJSON(itemDesc); return callback+"("+json+")";
	 * 
	 * }
	 */
	@RequestMapping("/web/testJSONP")
	public JSONPObject jsonpObject(String callback) {
		User user = new User();
		user.setId(1212323).setName("董Sb");
		
		JSONPObject jsonpObject = new JSONPObject(callback, user);
		return jsonpObject;
	}
	
	
	
	
	
	
	
	
}
