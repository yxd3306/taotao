package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value = "id",defaultValue = "0")Long parentId){
        List<EasyUITreeNode> list = contentCategoryService.getContentCategoryList(parentId);
        return list;
    }

    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addContentCategory(Long parentId,String name){
        TaotaoResult result = contentCategoryService.addContentCategory(parentId, name);
        return result;
    }

    @RequestMapping(value = "/content/category/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateContentCategory(Long id,String name){
        contentCategoryService.updateContentCategory(id,name);
    }

    @RequestMapping(value = "/content/category/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteContentCategory(Long id){
        contentCategoryService.deleteContentCategory(id);
    }
}
