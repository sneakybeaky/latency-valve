package com.ninedemons.catalina.valves.latency;

/**
 * A Command that supports sleeping for a specified number of milliseconds.
 *
 * The primary motivation for this was to enable testing.
 */
public interface SleepCommand {

    public void sleep(long timeInMilliseconds);
}
