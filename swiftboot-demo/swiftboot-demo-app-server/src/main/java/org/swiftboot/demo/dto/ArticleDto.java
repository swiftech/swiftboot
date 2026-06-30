package org.swiftboot.demo.dto;

import org.swiftboot.demo.model.Article;
import org.swiftboot.web.dto.BasePopulateDto;

/**
 *
 */
public class ArticleDto extends BasePopulateDto<Article> {

    private String id;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
