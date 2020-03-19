package org.swiftboot.web.util;

import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * @author swiftech 2019-02-10
 **/
public class SpringUtils {

    /**
     * 把 basePackage 名称转换为类资源路径
     * @param basePackage
     * @return
     */
    public static String resolveBasePackage(String basePackage) {
        // 先处理系统变量占位符，再做转换
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }
}
