package org.swiftboot.demo.request;


import org.swiftboot.demo.model.Article;
import org.swiftboot.web.request.BasePopulateRequest;

public class ArticleRequest extends BasePopulateRequest<Article> {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
