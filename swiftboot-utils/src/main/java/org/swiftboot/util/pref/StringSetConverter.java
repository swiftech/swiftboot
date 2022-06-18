package org.swiftboot.util.pref;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Converter for string Set, you can create your own implementation.
 *
 * @author allen
 */
public class StringSetConverter extends StringConverter<Set<String>> {

    @Override
    public Set<String> deserialize(String prefValue) {
        if (StringUtils.isNotBlank(prefValue)) {
            String[] split = StringUtils.split(prefValue, ",");
            if (split != null && split.length > 0) {
                Set<String> ret = new LinkedHashSet<>(split.length);
                Collections.addAll(ret, split);
                return ret;
            }
        }
        return null;
    }

    @Override
    public String serialize(Set<String> valueObject) {
        return StringUtils.join(valueObject, ",");
    }
}
