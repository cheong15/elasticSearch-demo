package com.cheong.esdemo.controller;


import com.cheong.esdemo.entity.Article;
import com.cheong.esdemo.repository.ArticleRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.search.MultiMatchQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/es")
public class ElasticSearchController {


    @Autowired
    private ArticleRepository articleRepository;

    @RequestMapping("/query")
    public Object query() {
//        Article article = articleRepository.queryArticleById("44");


        String queryString = "JENKINS";


        QueryBuilder builder = new QueryStringQueryBuilder(queryString);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(builder).withHighlightFields(
                new HighlightBuilder.Field("title")
        ).build();
        Iterable<Article> articles = articleRepository.search(searchQuery);
        Iterator<Article> iterator = articles.iterator();
        while (iterator.hasNext()) {

            System.out.println(iterator.next());

        }



        return null;
    }



    @RequestMapping("/query2")
    public Object query2() throws UnknownHostException {
//        Article article = articleRepository.queryArticleById("44");


        String queryString = "JENKINS";

        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
          .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.26.77"), 9300));

        QueryBuilder matchQuery = new QueryStringQueryBuilder(queryString);
        HighlightBuilder hiBuilder=new HighlightBuilder();
        hiBuilder.preTags("<font color='red'>");
        hiBuilder.postTags("</font>");
        hiBuilder.field("title");
        // 搜索数据
        SearchResponse response = client.prepareSearch("article")
                .setQuery(matchQuery)
                .highlighter(hiBuilder)
                .execute().actionGet();
        for (SearchHit searchHit : response.getHits()) {
            Map<String, Object> source = searchHit.getSource();
            System.out.println(source);
        }



        return null;
    }

}
