package com.ninedemons.catalina.valves.latency;


import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class DefaultSleepCommandTest {

    @Test
    public void testSleep() {

        DefaultSleepCommand underTest = new DefaultSleepCommand();

        long sleepInMilliseconds = 500;
        long start = System.currentTimeMillis();
        underTest.sleep(sleepInMilliseconds);
        long stop = System.currentTimeMillis();

        long delta = stop - start;

        if (! (delta >= sleepInMilliseconds)) {
            AssertJUnit.fail("Only slept for " + delta + "ms when we should have slept for at least " + sleepInMilliseconds + "ms");
        }

    }
}
