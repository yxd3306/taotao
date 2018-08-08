package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;

/**
 * 
 * @describe 商品类目管理接口
 * @author yxd
 * @data 2018-08-08 15:44:01
 *
 */
public interface ItemCatService {

    List<EasyUITreeNode> getItemCatList(long parentId);
}
