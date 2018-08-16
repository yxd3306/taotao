package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.search.service.SearchItemService;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private HttpSolrClient httpSolrClient;

    @Override
    public TaotaoResult importItemToIndex() {

        try {
            List<SearchItem> itemList = searchItemMapper.getItemList();
            for(SearchItem searchItem:itemList){
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id",searchItem.getId());
                document.addField("item_title",searchItem.getTitle());
                document.addField("item_sell_point",searchItem.getSell_point());
                document.addField("item_price",searchItem.getPrice());
                document.addField("item_image",searchItem.getImage());
                document.addField("item_category_name",searchItem.getCategory_name());
                document.addField("item_des",searchItem.getItem_desc());
                httpSolrClient.add(document);
            }
            httpSolrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            TaotaoResult.build(500,"数据导入失败");
        }
        return TaotaoResult.ok();
    }
}
