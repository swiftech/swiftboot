package org.swiftboot.util.time;

/**
 * Calculate elapsed time since start() method called.
 */
public class ElapsedTime {

    protected long startTime = 0L;

    /**
     * Start to timing.
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Stop the timing and return elapsed time.
     *
     * @return
     */
    public long stop() {
        long ret = getElapsedTime();
        this.startTime = 0L;
        return ret;
    }

    /**
     * Get elapsed time since started.
     */
    public long getElapsedTime() {
        return Math.max(System.currentTimeMillis() - this.startTime, 0L);
    }
}
