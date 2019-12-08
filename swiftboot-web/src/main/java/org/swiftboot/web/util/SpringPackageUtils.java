package org.swiftboot.web.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @auth swiftech
 */
public class SpringPackageUtils {

    /**
     * 从指定的包中扫描所有的类
     *
     * @param packages   扫描的包名，例如：org.swiftboot.model
     * @param superClass 过滤类型
     * @return
     */
    public static Set<Class<?>> scanClasses(String[] packages, Class<?> superClass) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        Set<Class<?>> candidates = new HashSet<>();

        for (String aPackage : packages) {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    SpringUtils.resolveBasePackage(aPackage) + "/*.class";
//            System.out.println("Scan classes from : " + packageSearchPath);
            Resource[] resources = new Resource[0];
            try {
                resources = resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    Class<?> aClass = Class.forName(metadataReader.getClassMetadata().getClassName());
                    if (superClass == null || superClass.isAssignableFrom(aClass)) {
                        candidates.add(aClass);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return candidates;
    }
}
