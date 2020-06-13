package org.swiftboot.data.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.swiftboot.data.Info;
import org.swiftboot.data.R;

public abstract class BaseJsonable implements Jsonable {

    @Override
    public String toJson() {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(Info.get(BaseJsonable.class, R.CONVERT_TO_JSON_FAIL));
        }
    }

}
