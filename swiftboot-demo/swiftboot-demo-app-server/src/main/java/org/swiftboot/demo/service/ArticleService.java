package org.swiftboot.demo.service;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.swiftboot.demo.dto.ArticleDto;
import org.swiftboot.demo.model.Article;
import org.swiftboot.demo.repository.ArticleRepository;
import org.swiftboot.demo.request.ArticleRequest;

import java.util.Optional;

@Service
public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Resource
    private ArticleRepository articleRepository;

    @Transactional
    public ArticleDto create(@RequestBody ArticleRequest request) {
        Article entity = request.createEntity();
        Article saved = articleRepository.save(entity);
        log.debug("create time: %s".formatted(saved.getCreateTime()));
        ArticleDto dto = new ArticleDto();
        dto.populateByEntity(saved);
        return dto;
    }

    @Transactional
    public ArticleDto update(String id, @RequestBody ArticleRequest request) {
        Optional<Article> byId = articleRepository.findById(id);
        if (byId.isPresent()) {
            Article article = byId.get();
            request.populateEntity(article);
            Article saved = articleRepository.save(article);
            log.debug("update time: %s".formatted(saved.getUpdateTime()));
            ArticleDto dto = new ArticleDto();
            dto.populateByEntity(saved);
            return dto;
        }
        else {
            return null;
        }
    }
}
