package org.swiftboot.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.annotation.PopulateIgnore;

/**
 *
 */
@Schema
public class NonPopulateBaseDto {

    @PopulateIgnore
    private String nonPersistentField;

    public String getNonPersistentField() {
        return nonPersistentField;
    }

    public void setNonPersistentField(String nonPersistentField) {
        this.nonPersistentField = nonPersistentField;
    }
}
