package org.swiftboot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;

/**
 * Internationalization helper class for managing application locales and resource bundles.
 * Supports English and Simplified Chinese.
 * If you want to change the language, use setLocale() directly.
 * <p>
 * Supports loading multiple resource bundles. Use {@link #addBundle(String)} to register additional bundles.
 *
 * @author SwiftBoot
 * @since 3.1.0
 */
public class I18nHelper {

    private static final Logger log = LoggerFactory.getLogger(I18nHelper.class);

    private static final String DEFAULT_BUNDLE_NAME = "messages";

    private static I18nHelper instance;

    private ResourceBundle resourceBundle;
    private List<ResourceBundle> resourceBundles;
    private List<String> bundleNames;
    private Locale currentLocale;

    private I18nHelper() {
        init();
    }

    /**
     * Get the singleton instance of I18nHelper.
     *
     * @return the singleton instance
     */
    public static I18nHelper getInstance() {
        if (instance == null) {
            instance = new I18nHelper();
        }
        return instance;
    }

    /**
     * Initialize the resource bundle based on user preference or system locale.
     */
    private void init() {
        resourceBundles = new ArrayList<>();
        bundleNames = new ArrayList<>();
        bundleNames.add(DEFAULT_BUNDLE_NAME);
        this.currentLocale = Locale.getDefault();
        reloadResourceBundle();
    }

    /**
     * Reload the resource bundle with the current locale.
     */
    private void reloadResourceBundle() {
        resourceBundles.clear();
        Map<String, Object> map = new HashMap<>();
        for (String bundleName : bundleNames) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(bundleName, currentLocale);
                resourceBundles.add(bundle);
                log.info("Resource bundle loaded: {} for locale: {} with {} strings", bundleName, currentLocale, bundle.keySet().size());
                for (String key : bundle.keySet()) {
                    map.put(key, bundle.getString(key));
                }
            } catch (Exception e) {
                log.warn("Failed to load resource bundle: {} for locale: {}", bundleName, currentLocale, e);
            }
        }
        this.resourceBundle = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                return map.get(key);
            }

            @Override
            public Enumeration<String> getKeys() {
                return Collections.enumeration(map.keySet());
            }
        };
    }

    /**
     * Set the application locale and reload the resource bundle.
     * Note: UI components need to be updated manually after calling this method.
     *
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        if (locale == null) {
            log.warn("Cannot set null locale");
            return;
        }
        this.currentLocale = locale;
        reloadResourceBundle();
    }

    /**
     * Set the application locale by language code string.
     *
     * @param languageCode the language code (e.g., "en", "zh_CN")
     */
    public void setLocale(String languageCode) {
        if (languageCode == null || languageCode.isEmpty()) {
            log.warn("Cannot set null or empty language code");
            return;
        }
        Locale locale = Locale.of(languageCode);
        setLocale(locale);
    }

    /**
     * Get the current locale.
     *
     * @return the current locale
     */
    public Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Get a localized string by key.
     * Searches through all registered resource bundles in order.
     *
     * @param key the key for the localized string
     * @return the localized string, or the key if not found
     */
    public String get(String key) {
        for (ResourceBundle bundle : resourceBundles) {
            try {
                return bundle.getString(key);
            } catch (Exception e) {
                // Continue to next bundle
            }
        }
        log.warn("Key '{}' not found in any resource bundle for locale {}", key, currentLocale);
        return key;
    }

    /**
     * Get a localized string by key with parameters.
     *
     * @param key
     * @param params
     * @return
     */
    public String get(String key, Object... params) {
        for (ResourceBundle bundle : resourceBundles) {
            try {
                String pattern = bundle.getString(key);
                return MessageFormat.format(pattern, params);
            } catch (Exception e) {
                // Continue to next bundle
                log.warn("Failed to get message for '%s'".formatted(key));
            }
        }
        return key;
    }

    /**
     * Get the primary resource bundle (default messages bundle).
     * For multiple bundles, consider using {@link #getAllBundles()}.
     *
     * @return the primary resource bundle
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Get all registered resource bundles.
     *
     * @return list of all resource bundles
     */
    public List<ResourceBundle> getAllBundles() {
        return resourceBundles;
    }

    public boolean addBundles(List<String> bundleNames) {
        boolean result = true;
        return bundleNames.stream().map(this::addBundle).reduce(result, (b1, b2) -> b1 && b2);
    }

    /**
     * Add a custom resource bundle to be loaded.
     * The bundle will be loaded with the current locale.
     * Call this method before initializing UI components.
     *
     * @param bundleName the base name of the resource bundle (e.g., "custom_messages")
     * @return true if the bundle was added successfully
     */
    public boolean addBundle(String bundleName) {
        if (bundleName == null || bundleName.isEmpty()) {
            log.warn("Bundle name cannot be null or empty");
            return false;
        }

        if (bundleNames.contains(bundleName)) {
            log.warn("Bundle '{}' is already registered", bundleName);
            return false;
        }

        bundleNames.add(bundleName);
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName, currentLocale);
            resourceBundles.add(bundle);
            log.info("Custom resource bundle added: {} for locale: {}", bundleName, currentLocale);
            return true;
        } catch (Exception e) {
            log.warn("Failed to load custom resource bundle: {} for locale: {}", bundleName, currentLocale, e);
            return false;
        }
    }

    /**
     * Remove a registered resource bundle.
     *
     * @param bundleName the base name of the resource bundle to remove
     * @return true if the bundle was removed successfully
     */
    public boolean removeBundle(String bundleName) {
        if (bundleName == null || bundleName.isEmpty()) {
            log.warn("Bundle name cannot be null or empty");
            return false;
        }

        if (DEFAULT_BUNDLE_NAME.equals(bundleName)) {
            log.warn("Cannot remove the default bundle: {}", DEFAULT_BUNDLE_NAME);
            return false;
        }

        int index = bundleNames.indexOf(bundleName);
        if (index >= 0) {
            bundleNames.remove(index);
            if (index < resourceBundles.size()) {
                resourceBundles.remove(index);
            }
            log.info("Resource bundle removed: {}", bundleName);
            return true;
        }
        log.warn("Bundle '{}' not found", bundleName);
        return false;
    }

    /**
     * Reload all registered resource bundles with the current locale.
     * This is useful when you want to refresh the bundles after adding or removing custom bundles.
     */
    public void reloadAllBundles() {
        reloadResourceBundle();
    }

    /**
     * Check if the current locale is Simplified Chinese.
     *
     * @return true if the current locale is Simplified Chinese
     */
    public boolean isChinese() {
        return Locale.SIMPLIFIED_CHINESE.equals(currentLocale);
    }

    /**
     * Check if the current locale is English.
     *
     * @return true if the current locale is English
     */
    public boolean isEnglish() {
        return Locale.ENGLISH.equals(currentLocale);
    }
}
