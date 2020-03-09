package org.swiftboot.collections;

import org.junit.jupiter.api.Test;
import org.swiftboot.collections.map.QueueMap;
import org.swiftboot.util.IdUtils;

import java.util.Locale;

/**
 * @author swiftech
 */
public class InfoTest {

    @Test
    public void test() {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        System.out.println(Info.get(QueueMap.class, R.QUEUE_SIZE_NOT_MATCH2));
        System.out.println(Info.get(CollectionUtils.class, R.COLLECTION_TYPE_NOT_SUPPORTED1));;
        System.out.println(Info.get(IdUtils.class, org.swiftboot.util.R.ID_FAILED1));;
    }
}
