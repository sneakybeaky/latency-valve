package com.ninedemons.catalina.valves;

import com.ninedemons.catalina.valves.latency.DefaultSleepCommand;
import com.ninedemons.catalina.valves.latency.SleepCommand;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementation of a Valve that adds a specified latency to the request. Useful for
 * testing upstream services when service degradation happens.
 *
 */
public class LatencyValve extends ValveBase {

    private static Log log = LogFactory.getLog(LatencyValve.class);
    private boolean enabled;
    private long latencyInMilliseconds = 0;

    List<Pattern> patterns = new ArrayList<Pattern>();


    protected SleepCommand sleepCommand = new DefaultSleepCommand();

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        doLatency(request);
        // Perform the request
        getNext().invoke(request, response);
    }

    private void doLatency(Request request) {
        if (conditionsMet(request)) {
            sleep();
        }
    }

    protected void sleep() {
        sleepCommand.sleep(latencyInMilliseconds);
    }

    protected boolean conditionsMet(Request request) {

        boolean result = false;

        if (enabled) {

            String uri = request.getRequestURI();
            for(Pattern pattern : patterns) {

                if (pattern.matcher(uri).matches()) {
                    log.debug("Found matching regex " + pattern.pattern() + " for URI " + uri);
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void clearUriPatterns() {
        patterns.clear();
    }

    public void addUriPattern(String uriPattern) {
        Pattern pattern = Pattern.compile(uriPattern);
        patterns.add(pattern);
    }

    /**
     * Returns the list of regular expressions we look for
     * @return list of regular expressions - 0 length if none
     */
    public List<String> getUriPatterns() {

        ArrayList<String> result = new ArrayList<String>(patterns.size());

        for (Pattern pattern : patterns) {
            result.add(pattern.pattern());

        }
        return result;
    }

    public long getLatencyInMilliseconds() {
        return latencyInMilliseconds;
    }

    public void setLatencyInMilliseconds(long latencyInMilliseconds) {
        this.latencyInMilliseconds = latencyInMilliseconds;
    }


    public void setSleepCommand(SleepCommand sleepCommand) {
        this.sleepCommand = sleepCommand;
    }
}
