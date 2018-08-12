package com.taotao.content.service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {


    //添加类目内容
    TaotaoResult addContent(TbContent content);
    //获取商城首页大广告图片
    List<TbContent> getContentByCid(long cid);
}
