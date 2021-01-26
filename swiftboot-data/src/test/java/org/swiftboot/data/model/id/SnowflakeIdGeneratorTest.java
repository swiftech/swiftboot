package org.swiftboot.data.model.id;

import org.junit.jupiter.api.Test;
import org.swiftboot.data.id.entity.AEntity;
import org.swiftboot.data.id.entity.AaaaBbbbCcccDdddEeeeEntity;

/**
 * @author allen
 */
class SnowflakeIdGeneratorTest {

    @Test
    void generateOriginal() {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(false);
        AEntity entity = new AEntity();
        String generatedId = snowflakeIdGenerator.generate(entity);
        System.out.println(generatedId);
        System.out.println(generatedId.length());

        AaaaBbbbCcccDdddEeeeEntity longEntity = new AaaaBbbbCcccDdddEeeeEntity();
        generatedId = snowflakeIdGenerator.generate(longEntity);
        System.out.println(generatedId);
    }

    @Test
    void generate32() {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator();
        String generatedId = snowflakeIdGenerator.generate(null);
        System.out.println(generatedId);
    }
}