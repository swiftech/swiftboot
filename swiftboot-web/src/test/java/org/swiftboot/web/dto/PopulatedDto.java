package org.swiftboot.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 */
@Schema
public class PopulatedDto extends NonPopulateBaseDto implements PopulatableDto<PopulatedEntity> {

    private String persistentField;

    public String getPersistentField() {
        return persistentField;
    }

    public void setPersistentField(String persistentField) {
        this.persistentField = persistentField;
    }
}
