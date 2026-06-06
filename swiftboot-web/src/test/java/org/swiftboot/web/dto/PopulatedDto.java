package org.swiftboot.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 */
@Schema
public class PopulatedDto extends NonPopulateBaseDto implements PopulatableDto<PopulatedEntity> {

    private String persistentField;

    private TestEnum testEnum;

    public String getPersistentField() {
        return persistentField;
    }

    public void setPersistentField(String persistentField) {
        this.persistentField = persistentField;
    }

    public TestEnum getTestEnum() {
        return testEnum;
    }

    public void setTestEnum(TestEnum testEnum) {
        this.testEnum = testEnum;
    }
}
