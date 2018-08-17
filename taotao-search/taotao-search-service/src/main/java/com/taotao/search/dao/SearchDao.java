package com.taotao.search.dao;


import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDao {

    @Autowired
    private HttpSolrClient httpSolrClient;

    public SearchResult search(SolrQuery query)throws Exception{
        QueryResponse response = httpSolrClient.query(query);
        SolrDocumentList solrDocumentList = response.getResults();
        long numFound = solrDocumentList.getNumFound();
        SearchResult result = new SearchResult();
        result.setRecordCount(numFound);
        List<SearchItem> itemList = new ArrayList<>();
        for(SolrDocument solrDocument:solrDocumentList){
            SearchItem item = new SearchItem();
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            item.setId((String) solrDocument.get("id"));
            //取一张图片
            String image = (String) solrDocument.get("item_image");
            if(StringUtils.isNoneBlank(image)){
                image=image.split(",")[0];
            }
            item.setImage(image);
            item.setPrice((Long) solrDocument.get("item_price"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title="";
            if(list!=null){
                title=list.get(0);
            }else{
                title= (String) solrDocument.get("item_title");
            }
            item.setTitle(title);
            itemList.add(item);
        }
        result.setItemList(itemList);
        return result;
    }

}
