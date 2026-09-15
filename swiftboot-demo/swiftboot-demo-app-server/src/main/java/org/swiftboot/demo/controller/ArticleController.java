package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.demo.dto.ArticleDto;
import org.swiftboot.demo.dto.ArticlePageDto;
import org.swiftboot.demo.model.Article;
import org.swiftboot.demo.repository.ArticleRepository;
import org.swiftboot.demo.request.ArticleQueryRequest;
import org.swiftboot.demo.request.ArticleRequest;
import org.swiftboot.demo.service.ArticleService;
import org.swiftboot.util.time.LocalDateUtils;
import org.swiftboot.web.response.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Tag(name = "Article")
@Controller
@RequestMapping("/article")
@ResponseBody
public class ArticleController {

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
    @Resource
    private ArticleService articleService;
    @Resource
    private ArticleRepository articleRepository;

    @PostMapping()
    public Response<ArticleDto> create(@RequestBody ArticleRequest request) {
        ArticleDto dto = articleService.create(request);
        return new Response<>(dto);
    }

    @PutMapping("{id}")
    public Response<ArticleDto> update(@PathVariable("id") String id, @RequestBody ArticleRequest request) {
        ArticleDto updated = articleService.update(id, request);
        Optional<Article> byId = articleRepository.findById(id);
        log.info(String.valueOf(byId.get().getUpdateTime()));
        if (updated == null) {
            return Response.builder(ArticleDto.class).message("Not found").build();
        }
        else {
            return new Response<>(updated);
        }
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
    public Response<ArticlePageDto> page(ArticleQueryRequest request, int page, int size) {
        log.debug("Locale: %s".formatted(LocaleContextHolder.getLocale()));
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        LocalDateTime startTime = theDateTime(request.getStartTime(), request.getStartDate());
        LocalDateTime endTime = theDateTime(request.getEndTime(), request.getEndDate());
        log.debug("Query articles by create time from {} to {} page {} with size {}", startTime, endTime, page, size);
        Page<Article> pages = articleRepository.findAllByCreateTimeGreaterThanAndCreateTimeLessThan(startTime, endTime, pageRequest);
        ArticlePageDto ret = new ArticlePageDto();
        ret.populateByEntities(pages, (dto, entity) -> {
            // dto.setTitle(entity.getTitle() + "(modified)");
        });
        return Response.builder(ArticlePageDto.class).data(ret).build();
    }

    private LocalDateTime theDateTime(LocalDateTime localDateTime, LocalDate localDate) {
        LocalDateTime finalDateTime = localDateTime == null
                ? (localDate == null ? null : LocalDateUtils.toUtcStartOfDay(localDate, ZoneId.of("GMT+8")))
                : localDateTime;
        return finalDateTime;
    }

}
