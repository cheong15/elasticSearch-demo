package com.cheong.esdemo.repository;

import com.cheong.esdemo.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface ArticleRepository extends ElasticsearchRepository<Article,String> {

    Article queryArticleById(String id);


}
