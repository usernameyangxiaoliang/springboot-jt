package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service(timeout=3000)
public class DubboCartServiceImpl implements DubboCartService {
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}
	
	/**
	 * 更新数据信息：num/updated
	 * 判断条件：where user_id and item_id
	 */
	@Override
	@Transactional
	public void updateCartNum(Cart cart) {
		Cart tempCart = new Cart();
		tempCart.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper =new UpdateWrapper<>();
		updateWrapper.eq("user_id", cart.getUserId()).eq("item_id", cart.getItemId());
		cartMapper.update(cart, updateWrapper);
		
	}
	/**
	 * 将对象中不为null的属性当做where条件
	 * 前提：保证cart只能有2个属性不为null
	 */
	@Override
	@Transactional
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);
		cartMapper.delete(queryWrapper);
	}
	/**
	 * 新增业务实现：
	 * 1.用户第一次新增可以直接入库
	 * 2.用户不是第一次入库，只做数量修改
	 * 根据：item_id和user_id 
	 */
	@Override
	@Transactional
	public void insertCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper =new QueryWrapper<>();
		queryWrapper.eq("user_id",cart.getUserId()).eq("item_id",cart.getItemId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if(cartDB==null) {
			//用户第一次购买商品，直接入库
			cart.setCreated(new Date()).setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			//用户多次添加购物车，只做数量修改
			int num = cart.getNum()+cartDB.getNum();
			cartDB.setNum(num).setUpdated(new Date());
			cartMapper.updateById(cartDB);
		}
		
		
	}
}
