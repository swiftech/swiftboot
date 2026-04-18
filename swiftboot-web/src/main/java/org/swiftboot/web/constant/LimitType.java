package org.swiftboot.web.constant;

public enum LimitType {
    DEFAULT,    // Default, limit request globally for each endpoint.
    USER        // limit request for each user on each endpoint.
}