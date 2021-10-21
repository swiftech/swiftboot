package org.swiftboot.collections;

import org.swiftboot.collections.map.QueueMap;
import org.swiftboot.util.IdUtils;
import org.swiftboot.util.Info.Resource;

public class R implements Resource {

    public static final String COLLECTION_TYPE_NOT_SUPPORTED1 = "collection_type_not_supported1";
    public static final String QUEUE_SIZE_NOT_MATCH2 = "queue_size_not_match2";


    public static Class<?>[] getResourceClasses() {
        return new Class<?>[]{
                R.class,
                org.swiftboot.util.R.class
        };
    }

    public static void main(String[] args) {
//        System.out.println(Info.get(QueueMap.class, R.QUEUE_SIZE_NOT_MATCH2));
        Info info = new Info();
        Info.validateForAllLocale(); // 这一行会导致utils的Info加载，但是 collections 的 R 还没有添加进去，所以要先 new 一个 Info
        System.out.println();

        System.out.println(Info.get(QueueMap.class, R.QUEUE_SIZE_NOT_MATCH2));
        System.out.println(Info.get(CollectionUtils.class, R.COLLECTION_TYPE_NOT_SUPPORTED1));
        System.out.println(Info.get(IdUtils.class, org.swiftboot.util.R.ID_FAILED1));
    }


}
