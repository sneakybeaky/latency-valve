package com.ninedemons.catalina.valves.latency;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple stub that counts the number of times invoked
 */
public class StubSleepCommand implements SleepCommand {

    private AtomicInteger timesInvoked = new AtomicInteger(0);

    public void sleep(long timeInMilliseconds) {
        timesInvoked.incrementAndGet();
    }

    public int getTimesInvoked() {
        return timesInvoked.get();
    }

    public void resetCount() {
        timesInvoked.set(0);
    }
}
