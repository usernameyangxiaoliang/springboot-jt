package com.jt.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.annotion.RedisAspect;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;
@Service
public class ItemCatServiceImpl implements ItemCatService{
	@Autowired
	private ItemCatMapper itemcatmapper;
	//@Autowired
	//private Jedis jedis;

	@Override
	public String findById(Long itemCatId) {
		
		return itemcatmapper.selectById(itemCatId).getName();
	}

	//@RedisAspect
	@Override
	public List<EasyUITree> finItemCatByParentId(Long parentId) {
		List<ItemCat> ItemCatList = findItemCatList(parentId);
		List<EasyUITree> treeList = new ArrayList<>();
		for (ItemCat ItemList : ItemCatList) {
			EasyUITree uiTree = new EasyUITree();
			uiTree.setId(ItemList.getId());
			uiTree.setText(ItemList.getName());
			String state=ItemList.getIsParent()?"closed":"open";
			uiTree.setState(state);
			treeList.add(uiTree);
		}
		return treeList;
	}
	
	
	public List<ItemCat> findItemCatList(Long parentId){
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		List<ItemCat> catList = itemcatmapper.selectList(queryWrapper);
		return catList;
		
	}

	/*
	 * @Override public List<EasyUITree> finItemCatByCache(Long parentId) { String
	 * key = "ITEM_CAT_"+parentId; String result = jedis.get(key); List<EasyUITree>
	 * treeList = new ArrayList<>(); if(StringUtils.isEmpty(result)) {
	 * //如果为null查询数据库 treeList = finItemCatByParentId(parentId); //将数据转化为json String
	 * json = ObjectMapperUtil.toJSON(treeList); jedis.setex(key, 7*24*3600, json);
	 * System.out.println("业务查询数据库"); }else {
	 * treeList=ObjectMapperUtil.toObject(result,treeList.getClass()); } return
	 * treeList; }
	 */

	
}
