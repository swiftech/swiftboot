package org.swiftboot.util.time;

import java.time.ZoneId;
import java.util.*;

/**
 * Mapping between user locales and timezones for determine the timezone of user locale.
 *
 * @since 3.2
 */
public class TimeZoneMapper {

    // 1. 自动初始化的全球 Locale -> 主时区 映射表
    private static final Map<String, ZoneId> AUTO_LOCALE_ZONE_MAP = new HashMap<>();

    // 1. 预设常见的 国家/语言 -> 默认标准时区
    private static final Map<String, ZoneId> OVERRIDE_MAP = Map.of(
            "zh_CN", ZoneId.of("Asia/Shanghai"),  // 中国大陆 -> 北京时间 (UTC+8)
            "zh_TW", ZoneId.of("Asia/Taipei"),    // 中国台湾 -> 台北时间 (UTC+8)
            "zh_HK", ZoneId.of("Asia/Hong_Kong"), // 中国香港 -> 香港时间 (UTC+8)
            "ja_JP", ZoneId.of("Asia/Tokyo"),     // 日本 -> 东京时间 (UTC+9)
            "ko_KR", ZoneId.of("Asia/Seoul"),     // 韩国 -> 首尔时间 (UTC+9)
            "en_US", ZoneId.of("America/New_York"),// 美国 -> 纽约时间/东部时间
            "en_GB", ZoneId.of("Europe/London")   // 英国 -> 伦敦时间
    );

    static {
        // 【核心魔法】：遍历 JDK 支持的所有国家，提取其主要时区
        for (String countryCode : Locale.getISOCountries()) {

            Locale locale = Locale.of("", countryCode);
            // 获取该国家/地区下的所有候选时区
            Set<String> zoneIds = getZoneIdsForCountry(countryCode);

            if (!zoneIds.isEmpty()) {
                // 默认取该国第 1 个标准时区（通常是政治/经济核心区）
                String primaryZone = zoneIds.iterator().next();
                AUTO_LOCALE_ZONE_MAP.put(countryCode.toUpperCase(), ZoneId.of(primaryZone));
            }
        }
    }

    /**
     * 根据 Locale 解析最佳 ZoneId
     */
    public static ZoneId resolveZoneId(Locale locale) {
        if (locale == null || locale.getCountry().isEmpty()) {
            // 如果只有语言没有国家（如纯 "zh" 或 "en"），执行语言回退
            return resolveByLanguageOnly(locale);
        }

        String languageAndCountry = locale.getLanguage() + "_" + locale.getCountry();
        String country = locale.getCountry().toUpperCase();

        // 优先 1：查找特殊多时区国家的硬编码覆盖表 (例: en_US -> America/New_York)
        if (OVERRIDE_MAP.containsKey(languageAndCountry)) {
            return OVERRIDE_MAP.get(languageAndCountry);
        }

        // 优先 2：从 JDK 自动初始化的全球国家主时区表中查找 (例: JP -> Asia/Tokyo, CN -> Asia/Shanghai)
        if (AUTO_LOCALE_ZONE_MAP.containsKey(country)) {
            return AUTO_LOCALE_ZONE_MAP.get(country);
        }

        // 优先 3：降级按纯语言推导
        return resolveByLanguageOnly(locale);
    }

    /**
     * 利用 JDK 查找某个国家代码下的所有 ZoneId
     */
    private static Set<String> getZoneIdsForCountry(String countryCode) {
        // Java 9+ 推荐用法：从 ZoneId 过滤指定国家前缀的时区
        // 配合简易匹配算法，映射出该国主流 Zone
        Set<String> result = new LinkedHashSet<>();
        for (String zoneId : ZoneId.getAvailableZoneIds()) {
            // 排除过时的 GMT/UTC 或 3 字母缩写，保留 Region/City 格式
            if (zoneId.contains("/") && !zoneId.startsWith("Etc/")) {
                // 利用标准的 Locale/TimeZone 关联
                TimeZone tz = TimeZone.getTimeZone(zoneId);
                if (tz.getID().toUpperCase().startsWith(countryCode)) {
                    result.add(tz.getID());
                }
            }
        }

        // 如果上面没过滤出来，通过 Java 内置的 TimeZone.getAvailableIDs(country) 补全
        if (result.isEmpty()) {
            String[] ids = TimeZone.getAvailableIDs();
            for (String id : ids) {
                // 常见国家简称识别逻辑
                if (isZoneMatchesCountry(id, countryCode)) {
                    result.add(id);
                }
            }
        }
        return result;
    }

    private static ZoneId resolveByLanguageOnly(Locale locale) {
        if (locale == null) return ZoneId.of("UTC");
        return switch (locale.getLanguage()) {
            case "zh" -> ZoneId.of("Asia/Shanghai");
            case "ja" -> ZoneId.of("Asia/Tokyo");
            case "ko" -> ZoneId.of("Asia/Seoul");
            case "de" -> ZoneId.of("Europe/Berlin");
            case "fr" -> ZoneId.of("Europe/Paris");
            case "en" -> ZoneId.of("America/New_York");
            default -> ZoneId.of("UTC");
        };
    }

    private static boolean isZoneMatchesCountry(String zoneId, String countryCode) {
        // 兜底映射规则：处理常见大国的 Zone 命名规则
        return switch (countryCode) {
            case "CN" -> zoneId.equals("Asia/Shanghai");
            case "JP" -> zoneId.equals("Asia/Tokyo");
            case "KR" -> zoneId.equals("Asia/Seoul");
            case "GB" -> zoneId.equals("Europe/London");
            case "DE" -> zoneId.equals("Europe/Berlin");
            case "FR" -> zoneId.equals("Europe/Paris");
            default -> false;
        };
    }

    public static void main(String[] args) {
        System.out.println(resolveZoneId(Locale.of("zh")));
        System.out.println(resolveZoneId(Locale.of("", "CN")));
        System.out.println(resolveZoneId(Locale.SIMPLIFIED_CHINESE));;
    }
}