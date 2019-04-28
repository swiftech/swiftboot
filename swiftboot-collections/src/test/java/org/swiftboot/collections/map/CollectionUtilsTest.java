package org.swiftboot.collections.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.swiftboot.collections.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @author Allen 2019-04-28
 **/
public class CollectionUtilsTest {

    @Test
    public void testConstructByType() {
        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                List list = CollectionUtils.constructCollectionByType(List.class);
                Assertions.assertNotNull(list);

                Set set = CollectionUtils.constructCollectionByType(Set.class);
                Assertions.assertNotNull(set);
            }
        });
    }
}
