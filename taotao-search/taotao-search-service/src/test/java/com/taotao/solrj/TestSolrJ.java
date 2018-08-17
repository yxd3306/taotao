package com.taotao.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @Auther: yxd
 * @Date: 2018-08-15 10:09:16
 * @Description:
 */
public class TestSolrJ {
    @Test
    public void solrAdd() throws Exception {
        //[1]获取连接
        // HttpSolrClient client= new HttpSolrClient.Builder("http://127.0.0.1:8080/solr/core1").build();
        String solrUrl = "http://132.232.33.227:8983/solr/collection2";
        //创建solrClient同时指定超时时间，不指定走默认配置
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        //[2]创建文档doc
        SolrInputDocument doc = new SolrInputDocument();
        //[3]添加内容
        doc.addField("id", "test002");
        doc.addField("item_title", "测试商品1");
        doc.addField("item_price", 1000);
        doc.addField("item_image", "http://132.232.33.227:9090/group1/M00/00/00/rBsAA1t1pTuAGCWjAAEGMzZiPpo589.jpg");
        doc.addField("item_category_name", "123");
        doc.addField("item_sell_point", "123");
        //[4]添加到client
        client.add(doc);
        //[5] 索引文档必须commit
        client.commit();
    }

    @Test
    public void deleteDocumentById()throws Exception{
        String solrUrl = "http://132.232.33.227:8983/solr/collection2";
        //创建solrClient同时指定超时时间，不指定走默认配置
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        client.deleteById("test002");
        client.commit();
    }

    @Test
    public void queryDocumentByTitle()throws Exception{
        String solrUrl = "http://132.232.33.227:8983/solr/collection2";
        //创建solrClient同时指定超时时间，不指定走默认配置
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        SolrQuery query = new SolrQuery();
        query.setQuery("手机");
        query.setStart(0);
        query.setRows(5);
        query.set("df","item_title");
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<div>");
        query.setHighlightSimplePost("</div>");
        QueryResponse response = client.query(query);
        SolrDocumentList solrDocumentList = response.getResults();
        long numFound = solrDocumentList.getNumFound();
        System.out.println(numFound);
        for(SolrDocument solrDocument:solrDocumentList){
            System.out.println(solrDocument.get("id"));
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get(solrDocument.get("item_title"));
            String itemTitle="";
            if(list!=null && list.size()>0){
                itemTitle=list.get(0);
            }else{
                itemTitle= (String) solrDocument.get("item_title");
            }
            System.out.println(solrDocument.get("item_title"));
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
            System.out.println(solrDocument.get("item_category_name"));
            System.out.println(solrDocument.get("item_des"));
        }
    }


}
