package com.jt.service;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUIData;
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	@Override
	public EasyUIData findItemByPage(Integer page, Integer rows) {
		Integer selectCount = itemMapper.selectCount(null);
		int start=(page-1)*rows;
		List<Item> itemList = itemMapper.findItemByPage(start,rows);
		return new EasyUIData(selectCount,itemList);
	}
	//添加商品
	@Transactional//aop注解添加事务控制
	@Override
	public void saveItem(Item item,ItemDesc itemDesc) {
		item.setStatus(1).setCreated(new Date()).setUpdated(item.getCreated());
		itemMapper.insert(item);
		itemDesc.setItemId(item.getId()).setCreated(item.getCreated()).setUpdated(item.getUpdated());
		itemDescMapper.insert(itemDesc);
	}
	//根据id更新
	/**
	 * propagation 事务传播属性 
	 * 默认值REQUIRED  必须添加事务对个事务胡合并
	 * REQUIRES_NEW  必须新建一个事务
	 * SUPPORTS      表示事务支持的
	 * rollbackFor="包名.类名.class"(异常类型)  按照指定的异常回滚事务   
	 * readOnly=true 不允许修改数据库
	 */
	/*
	 * Spring中默认的事务控制策略
	 * 1.检查异常/编译异常     不负责事务控制
	 * 2.运行时异常/error 回滚事务
	 * 
	 */
	//@Transactional(propagation=Propagation.REQUIRED)
	@Transactional
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		item.setCreated(new Date());
		itemMapper.updateById(item);
		itemDesc.setItemId(item.getId()).setCreated(item.getCreated());
		itemDescMapper.updateById(itemDesc);
	}
	@Override
	public void deleteItem(Long[] ids) {
		//1.手动删除需要手写sql
		//itemMapper.deleteItem(ids);
		//2.自动删除
		List<Long> idList = Arrays.asList(ids);
		/*
		 * ArrayList<Long> idList = new ArrayList<Long>(); for (Long id : ids) {
		 * idList.add(id); }
		 */
		itemMapper.deleteBatchIds(idList);
		itemDescMapper.deleteBatchIds(idList);
	}
	/**
	 * sql: update tb_item set status=#{status},updated=#{updated}where id in(100,...)
	 */
	@Override
	public void updateStatus(Long[] ids, int status) {
		Item item = new Item();
		item.setStatus(status).setUpdated(new Date());
		List<Long> longIds = Arrays.asList(ids);
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<Item>();
		updateWrapper.in("id", longIds);
		itemMapper.update(item, updateWrapper);
	}
	
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		
		return itemDescMapper.selectById(itemId);
	}
	
	@Override
	public Item findItemById(Long id) {
		return itemMapper.selectById(id);
	}
	
	
	
	
	
	
	
	
	
}
