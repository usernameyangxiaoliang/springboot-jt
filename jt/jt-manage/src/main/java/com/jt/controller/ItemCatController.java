package com.jt.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.annotion.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	@Autowired
	private ItemCatService itemcatservice;
	// 根据id查询商品分类信息
	@RequestMapping("/queryItemName")
	public String findById(Long itemCatId) {
		return itemcatservice.findById(itemCatId);
	}
	//查询全部数据的商品分类信息
	//需要获取任意名称的参数，为指定的参数赋值@RequestParam
	//@RequestParam(value="id",defaultValue="0")Long ParentId
	//value="id"传进来的参数，并把id赋值给ParentId，如果没有参数传进来defaultValue="0"默认为0，代表一级分类
	@RequestMapping("/list")
	@Cache_Find(key="ITEM_CAT",keyType=KEY_ENUM.AUTO)
	public List<EasyUITree> findItemCatByParentId(@RequestParam(value="id",defaultValue="0")Long ParentId) {
		return itemcatservice.finItemCatByParentId(ParentId);
		//return itemcatservice.finItemCatByCache(ParentId);
	}

}
