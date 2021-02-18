package org.swiftboot.data.constant;

/**
 * @author swiftech
 * @since 2.0.0
 */
public interface AutoUpdateTimeStrategy {

    /**
     * Not set update time.
     */
    String AUTO_UPDATE_TIME_NOT_SET = "not-set";

    /**
     * Set update time when fields of entity is changed.
     */
    String AUTO_UPDATE_TIME_ON_CHANGE = "on-change";

    /**
     * Set update time no matter any fields of entity is changed or not.
     */
    String AUTO_UPDATE_TIME_ALWAYS = "always";
}
