package org.swiftboot.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.swiftboot.demo.model.Article;

import java.time.LocalDateTime;

@Repository
public interface ArticleRepository extends CrudRepository<Article, String>, ArticleCustomRepository {

    Page<Article> findAllBy(Pageable pageable);

    @Query("""
        select a from Article a where 1=1\s
                and (cast(:startTime as timestamp ) is null or cast(:startTime as timestamp ) < a.createTime)\s
                and (cast(:endTime as timestamp ) is null or cast(:endTime as timestamp ) > a.createTime)\s
        """)
    Page<Article> findAllByCreateTimeGreaterThanAndCreateTimeLessThan(LocalDateTime startTime, LocalDateTime endTime,
                                                                      Pageable pageable);
}
