package org.swiftboot.collections;

import java.util.Map;

/**
 * @author swiftech
 **/
public class QueueMapTest {

    public static void main(String[] args) {
        QueueMap queueMap = new QueueMap();
        System.out.println("TEST PUT");
        for (int i = 0; i < 10; i++) {
            queueMap.put((long) i, "v-" + i);
        }
        System.out.println("TEST LAST");
        Map last = queueMap.getLast(11);
        for (Object k : last.keySet()) {
            System.out.printf("%s=%s%n", k, queueMap.get(k));
        }

        System.out.println("TEST GET");

//		for (Object k : queueMap.keySet()) {
//			System.out.println(k + "->" + queueMap.get(k));
//		}
        System.out.println("Done");

    }
}
