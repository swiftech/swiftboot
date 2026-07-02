package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.demo.dto.ArticleDto;
import org.swiftboot.demo.dto.ArticlePageDto;
import org.swiftboot.demo.model.Article;
import org.swiftboot.demo.repository.ArticleRepository;
import org.swiftboot.demo.request.ArticleRequest;
import org.swiftboot.web.response.Response;

import java.util.List;

@Tag(name = "Article")
@Controller
@RequestMapping("/article")
@ResponseBody
public class ArticleController {

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Resource
    private ArticleRepository articleRepository;

    @PostMapping()
    public Response<Void> create(@RequestBody ArticleRequest request) {
        Article entity = request.createEntity();
        articleRepository.save(entity);
        return Response.builder().ok().build();
    }

    @Operation(description = "")
    @GetMapping(value = "")
    public Response<Object> list() {
        log.debug("Query articles");
        Iterable<Article> articles = articleRepository.findAll();
        List<ArticleDto> ret = IterableUtils.toList(articles).stream().map(article -> {
            ArticleDto dto = new ArticleDto();
            dto.populateByEntity(article);
            return dto;
        }).toList();
        return Response.builder(Object.class).data(ret).build();
    }

    @Operation(description = "")
    @GetMapping(value = "page")
    public Response<ArticlePageDto> page(int page, int size) {
        log.debug("Query articles by page {} with size {}", page, size);
        Page<Article> pages = articleRepository.findAllBy(PageRequest.of(page, size));
        ArticlePageDto ret = new ArticlePageDto();
        ret.populateByEntities(pages, (dto, entity) -> {
            dto.setTitle(entity.getTitle() + "(modified)");
        });
        return Response.builder(ArticlePageDto.class).data(ret).build();
    }

}
