package org.swiftboot.data.model.id;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.data.model.entity.IdPojo;

/**
 * ID generator with snowflake algorithm.
 * Default result is 32 bytes numeric string begin with snowflake ID and pad with random string,
 * set isPadTo32 = false if you want to keep original 19 bytes snowflake id.
 *
 * @author allen
 */
public class SnowflakeIdGenerator implements IdGenerator<IdPojo> {

    private final Sequence sequence = new Sequence(0);

    /**
     * Whether padding snowflake id with random numerics after.
     */
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
