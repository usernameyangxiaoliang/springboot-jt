package com.jt.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.startup.Catalina;
import org.apache.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
@Controller
@RequestMapping("/cart")
public class CartController {
	@Reference(timeout=3000,check=false)
	private DubboCartService cartService;
	//购物车列表
	@RequestMapping("/show")
	public String  findCartList(Model model) {
		//User user = (User) request.getAttribute("JT_USER");
		Long userId = UserThreadLocal.get().getId();
		List<Cart> cartList= cartService.findCartListByUserId(userId);
		model.addAttribute("cartList",cartList);
		return "cart";
	}
	/**
	 * 实现购物车数量的修改
	 * 如果url参数中使用restFul风格获取数据时，接收参数是对象并且属性匹配则可以使用对象接收
	 * @param cart
	 * @return
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {
		try {
			//User user = (User) request.getAttribute("JT_USER");
			Long userId =UserThreadLocal.get().getId();
			cart.setUserId(userId);
			cartService.updateCartNum(cart);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
		
	}
	//实现购物车的删除 delete/562379.html
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart,HttpServletRequest request) {
			//User user = (User) request.getAttribute("JT_USER");
		    Long userId =UserThreadLocal.get().getId();
			cart.setUserId(userId);
			cartService.deleteCart(cart);
			//重定向到购物车列表页面
			return "redirect:/cart/show.html";
		}
	//新增商品到购物车  页面发起post请求
	@RequestMapping("/add/{itemId}")
	public String insertCart(Cart cart,HttpServletRequest request) {
		//User user = (User) request.getAttribute("JT_USER");
		Long userId =UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.insertCart(cart);
		//重定向到购物车列表页面,重定向可以显示更新后购物车商品列表信息
		//转发只能跳转到购物车信息列表不能更新数据，并显示
		return "redirect:/cart/show.html";
		
	}
		

	
	
	
	
	
	
	
	
	
	

	
}
