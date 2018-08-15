package com.taotao.solrj;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

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


}
