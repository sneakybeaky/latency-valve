package com.ninedemons.catalina.valves;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Valve used in testing.
 */
public class StubValve extends ValveBase {

    AtomicInteger timesInvoked = new AtomicInteger(0);

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        timesInvoked.incrementAndGet();
    }

    public int getTimesInvoked() {
        return timesInvoked.get();
    }

    public void resetCount() {
        timesInvoked.set(0);
    }

}
