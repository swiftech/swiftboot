package org.swiftboot.data.model.id;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.data.model.entity.IdPojo;

/**
 * @author allen
 */
public class SnowflakeIdGenerator implements IdGenerator<IdPojo> {

    private final Sequence sequence = new Sequence(0);

    private boolean isPadTo32 = true;

    public SnowflakeIdGenerator() {
    }

    public SnowflakeIdGenerator(boolean isPadTo32) {
        this.isPadTo32 = isPadTo32;
    }

    @Override
    public String generate(IdPojo object) {
        String snowflakeId = String.valueOf(sequence.nextId());
        if (isPadTo32){
            snowflakeId = StringUtils.rightPad(snowflakeId, 32, RandomStringUtils.randomNumeric(32 - snowflakeId.length()));
        }
        return snowflakeId;
    }
}
