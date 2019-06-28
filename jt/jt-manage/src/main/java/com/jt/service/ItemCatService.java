package com.jt.service;

import java.util.List;

import com.jt.vo.EasyUITree;

public interface ItemCatService {

	String findById(Long itemCatId);

	List<EasyUITree> finItemCatByParentId(Long parentId);

	//List<EasyUITree> finItemCatByCache(Long parentId);

}
