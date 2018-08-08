package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbItem;

public interface ItemService {

    //根据商品id查询商品
    TbItem getItemById(long itemId);
    //分页查询商品
    EasyUIDataGridResult getItemList(int page,int rows);
}
