package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;


    @Override
    public SearchResult search(String queryString, int page, int rows) throws Exception {
        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);
        if(page<1)page=1;
        query.setStart(page);
        if(rows<0)rows=10;
        query.setRows(rows);
        query.set("df","item_title");
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");
        SearchResult searchResult = searchDao.search(query);
        long recordCount = searchResult.getRecordCount();
        long pages = recordCount/rows;
        if(recordCount%rows>0){
            pages++;
        }
        searchResult.setTotalPages(pages);
        return searchResult;
    }
}
