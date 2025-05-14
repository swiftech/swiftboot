package org.swiftboot.web.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.swiftboot.data.model.entity.IdPersistable;

@Entity
public class PopulatedEntity implements IdPersistable {

    @Id
    private String id;

    @Column
    private String persistentField;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getPersistentField() {
        return persistentField;
    }

    public void setPersistentField(String persistentField) {
        this.persistentField = persistentField;
    }
}
