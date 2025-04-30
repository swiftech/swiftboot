package org.swiftboot.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.demo.model.Article;


public interface ArticleRepository extends CrudRepository<Article, Integer> , ArticleCustomRepository{
}
