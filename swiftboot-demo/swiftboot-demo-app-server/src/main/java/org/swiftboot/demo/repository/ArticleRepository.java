package org.swiftboot.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.swiftboot.demo.model.Article;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Integer>, ArticleCustomRepository {

    Page<Article> findAllBy(Pageable pageable);
}
