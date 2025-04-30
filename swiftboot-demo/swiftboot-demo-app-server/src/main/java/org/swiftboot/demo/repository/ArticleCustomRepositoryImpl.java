package org.swiftboot.demo.repository;

import org.springframework.stereotype.Repository;
import org.swiftboot.demo.model.Article;

@Repository
public class ArticleCustomRepositoryImpl implements ArticleCustomRepository {

    @Override
    public void createArticle(Article article) {
        System.out.println("doSomething");
    }
}
