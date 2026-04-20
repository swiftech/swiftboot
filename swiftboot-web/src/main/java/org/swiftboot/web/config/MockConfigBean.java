package org.swiftboot.web.config;

public class MockConfigBean {

    /**
     * Enable mock timeout, default is false.
     */
    private boolean mockTimeout = false;

    /**
     * Timeout in milliseconds, default is 10 seconds.
     */
    private long timeout = 10 * 1000;

    public boolean isMockTimeout() {
        return mockTimeout;
    }

    public void setMockTimeout(boolean mockTimeout) {
        this.mockTimeout = mockTimeout;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
