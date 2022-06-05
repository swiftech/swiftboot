package org.swiftboot.util.pref;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Converter for string list.
 *
 * @author allen
 */
public class StringListConverter extends StringConverter<List<String>> {

    @Override
    public List<String> deserialize(String prefValue) {
        if (StringUtils.isNotBlank(prefValue)) {
            String[] split = StringUtils.split(prefValue, ",");
            if (split != null && split.length > 0) {
                List<String> ret = new ArrayList<>(split.length);
                Collections.addAll(ret, split);
                return ret;
            }
        }
        return null;
    }

    @Override
    public String serialize(List<String> valueObject) {
        return StringUtils.join(valueObject, ",");
    }
}
