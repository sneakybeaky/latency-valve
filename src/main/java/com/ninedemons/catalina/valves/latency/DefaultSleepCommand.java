package com.ninedemons.catalina.valves.latency;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 * Default implementation
 */
public class DefaultSleepCommand implements SleepCommand {

    private static Log log = LogFactory.getLog(DefaultSleepCommand.class);

    public void sleep(long timeInMilliseconds) {
        try {
            Thread.sleep(timeInMilliseconds);
        } catch (InterruptedException e) {
            log.warn("Unable to sleep",e);
        }
    }
}
