package com.taotao.content.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public TaotaoResult addContent(TbContent content) {
        //补全pojo的属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入内容表
        contentMapper.insert(content);

        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContentByCid(long cid) {
        //先查缓存
        //添加缓存不能影响正常业务逻辑
        try {
            //查询缓存
            String json = jedisClient.hget(INDEX_CONTENT, cid + "");
            //解决缓存穿透，使用双重检测锁，synchronized
            if (StringUtils.isNoneBlank(json)) {
                //缓存数据不为空
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                System.out.println("缓存数据存在，直接返回list");
                return list;
            } else {
                //缓存中没有数据锁定当前用户，查数据库
                synchronized (this) {
                    //查询缓存
                    json = jedisClient.hget(INDEX_CONTENT, cid + "");
                    if (StringUtils.isNoneBlank(json)) {
                        List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                        System.out.println("锁，缓存数据存在，直接返回list");
                        return list;
                    }else{
                        System.out.println("缓存数据不存在，差数据库");
                        //缓存中没有查数据库
                        TbContentExample example = new TbContentExample();
                        TbContentExample.Criteria criteria = example.createCriteria();
                        //设置查询条件
                        criteria.andCategoryIdEqualTo(cid);
                        //执行查询
                        List<TbContent> list = contentMapper.selectByExample(example);
                        //把结果添加到缓存
                        jedisClient.hset(INDEX_CONTENT, cid + "", JsonUtils.objectToJson(list));
                        return list;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
