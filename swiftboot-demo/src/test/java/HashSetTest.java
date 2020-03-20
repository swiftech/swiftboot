//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author swiftech
 */
public class HashSetTest {

    @Test
    public void testSet() {
        Set hs = new HashSet();
        hs.add("a");
        hs.add("b");
        hs.add("b");
        Assert.assertEquals(2, hs.size());
    }
}
