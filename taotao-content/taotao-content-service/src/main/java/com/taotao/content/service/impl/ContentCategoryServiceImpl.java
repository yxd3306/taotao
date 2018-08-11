package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		//根据parentId查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for(TbContentCategory tbContentCategory:list){
		    if(tbContentCategory.getStatus()==1) {
                EasyUITreeNode node = new EasyUITreeNode();
                node.setId(tbContentCategory.getId());
                node.setText(tbContentCategory.getName());
                node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
                resultList.add(node);
            }
		}
		return resultList;
	}

    @Override
    public TaotaoResult addContentCategory(long parentId, String name) {
        //创建一个pojo对象
        TbContentCategory contentCategory = new TbContentCategory();
        //补全对象属性
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        //状态。可选值：1、正常 2、删除
        contentCategory.setStatus(1);
        //排序，默认为1
        contentCategory.setSortOrder(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //插入到数据库
        contentCategoryMapper.insert(contentCategory);
        //判断父节点状态
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            //如果父节点为叶子节点应该改为父节点
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果
        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public void updateContentCategory(long id, String name) {
        //根据id查询内容类别
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        if(contentCategory!=null){
            contentCategory.setName(name);
            //更新时间
            contentCategory.setUpdated(new Date());
            //执行更新
            contentCategoryMapper.updateByPrimaryKey(contentCategory);
        }
    }

    @Override
    public void deleteContentCategory(long id) {
        //根据id查询内容类别
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        if(!contentCategory.getIsParent()){
            //当类目不为父目录时直接删除
            contentCategory.setStatus(2);
            contentCategory.setUpdated(new Date());
            contentCategoryMapper.updateByPrimaryKey(contentCategory);
        }else{
            //当类目为父目录时
            //根据parentId查询所属子类目并删除子类目
            List<TbContentCategory> childs = contentCategoryMapper.selectByParentId(contentCategory.getId());
            if(childs!=null){
                for(TbContentCategory child:childs ){
                    child.setStatus(2);
                    child.setUpdated(new Date());
                    contentCategoryMapper.updateByPrimaryKey(child);
                }
            }
            //最后删除父类目
            contentCategory.setStatus(2);
            contentCategory.setUpdated(new Date());
            contentCategoryMapper.updateByPrimaryKey(contentCategory);
        }
    }

}
