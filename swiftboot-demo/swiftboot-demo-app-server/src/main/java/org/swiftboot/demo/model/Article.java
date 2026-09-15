package org.swiftboot.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.swiftboot.data.model.entity.BaseLocalDateTimeEntity;
import org.swiftboot.data.model.entity.IdPersistable;

@Entity
@Table(name = "article")
public class Article extends BaseLocalDateTimeEntity implements IdPersistable {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
