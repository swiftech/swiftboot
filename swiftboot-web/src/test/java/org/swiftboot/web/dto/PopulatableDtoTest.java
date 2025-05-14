package org.swiftboot.web.dto;

import org.junit.jupiter.api.Test;
import org.swiftboot.util.IdUtils;
import org.swiftboot.util.JsonUtils;

import java.io.IOException;

public class PopulatableDtoTest {

    @Test
    public void testPopulatableDto() {
        PopulatedEntity populatedEntity = new PopulatedEntity();
        populatedEntity.setId(IdUtils.makeUUID());
        populatedEntity.setPersistentField("hello DTO");
        PopulatedDto dto = PopulatableDto.createDto(PopulatedDto.class, populatedEntity, false);
        try {
            System.out.println(JsonUtils.object2Json(dto));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
