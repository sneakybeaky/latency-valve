package com.ninedemons.catalina.valves;

import com.ninedemons.catalina.valves.latency.StubSleepCommand;
import org.apache.catalina.connector.Request;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;


import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;


public class LatencyValveTest {

    private static final String NON_MATCHING_PATTERN = "/some/pattern/.*";
    private static final String MATCHING_PATTERN_2 = ".*";
    private static final String MATCHING_PATTERN_1 = "/some/example/uri\\.html";
    private static final int LATENCY_IN_MILLISECONDS = 10000;
    LatencyValve underTest = new LatencyValve();
    StubValve stubValve = new StubValve();
    StubSleepCommand stubSleepCommand = new StubSleepCommand();

    Request request = new Request();

    @BeforeMethod
    public void setUp() throws Exception {
        underTest.setNext(stubValve);
        underTest.setSleepCommand(stubSleepCommand);

        resetValveData();

        org.apache.coyote.Request coyoteRequest = new org.apache.coyote.Request();
        coyoteRequest.requestURI().setString("/some/example/uri.html");

        request.setCoyoteRequest(coyoteRequest);
    }

    private void resetValveData() {
        underTest.clearUriPatterns();
        assertEquals(0,underTest.getUriPatterns().size());
        stubValve.resetCount();
        assertEquals(0, stubValve.getTimesInvoked());
        stubSleepCommand.resetCount();
        assertEquals(0, stubSleepCommand.getTimesInvoked());
        underTest.setEnabled(true);
        assertTrue(underTest.isEnabled());
        underTest.setLatencyInMilliseconds(LATENCY_IN_MILLISECONDS);
        assertEquals(LATENCY_IN_MILLISECONDS,underTest.getLatencyInMilliseconds());
    }

    @Test
    public void testCallPassedDownChainWhenNotEnabled() throws IOException, ServletException {
        underTest.setEnabled(false);

        underTest.invoke(null,null);

        assertEquals("Sleep should not have been invoked",0,stubSleepCommand.getTimesInvoked());
        assertEquals("Next valve should have been invoked",1,stubValve.getTimesInvoked());
    }

    @Test
    public void testCallPassedDownChainWhenNoPatternsSet() throws IOException, ServletException {

        underTest.invoke(request,null);

        assertEquals("Sleep should not have been invoked",0,stubSleepCommand.getTimesInvoked());
        assertEquals("Next valve should have been invoked",1,stubValve.getTimesInvoked());
    }

    @Test
    public void testCallPassedDownChainWhenNoMatchingPattern() throws IOException, ServletException {

        underTest.addUriPattern(NON_MATCHING_PATTERN);

        underTest.invoke(request,null);

        assertEquals("Sleep should not have been invoked",0,stubSleepCommand.getTimesInvoked());
        assertEquals("Next valve should have been invoked",1,stubValve.getTimesInvoked());
    }

    @Test
    public void testWhenOneMatchingPattern() throws IOException, ServletException {

        underTest.addUriPattern(NON_MATCHING_PATTERN);
        underTest.addUriPattern(MATCHING_PATTERN_1);

        underTest.invoke(request,null);

        assertEquals("Sleep should have been invoked",1,stubSleepCommand.getTimesInvoked());
        assertEquals("Next valve should have been invoked",1,stubValve.getTimesInvoked());
    }

    @Test
    public void testWhenTwoMatchingPattern() throws IOException, ServletException {

        underTest.addUriPattern(NON_MATCHING_PATTERN);
        underTest.addUriPattern(MATCHING_PATTERN_1);

        underTest.invoke(request,null);

        assertEquals("Sleep should have been invoked just once",1,stubSleepCommand.getTimesInvoked());
        assertEquals("Next valve should have been invoked",1,stubValve.getTimesInvoked());

        resetValveData();
        underTest.addUriPattern(NON_MATCHING_PATTERN);
        underTest.addUriPattern(MATCHING_PATTERN_2);

        underTest.invoke(request,null);

        assertEquals("Sleep should have been invoked just once",1,stubSleepCommand.getTimesInvoked());
        assertEquals("Next valve should have been invoked",1,stubValve.getTimesInvoked());

        resetValveData();
        underTest.addUriPattern(NON_MATCHING_PATTERN);
        underTest.addUriPattern(MATCHING_PATTERN_1);
        underTest.addUriPattern(MATCHING_PATTERN_2);

        underTest.invoke(request,null);

        assertEquals("Sleep should have been invoked just once",1,stubSleepCommand.getTimesInvoked());
        assertEquals("Next valve should have been invoked",1,stubValve.getTimesInvoked());
    }

    @Test
    public void testGetUriPatternsWhenNonSet() {
        List<String> foundPatterns = underTest.getUriPatterns();
        assertNotNull(foundPatterns);
        assertEquals("No URI patterns set, so none should be returned",0,foundPatterns.size());
    }

    @Test
    public void testGetUriPatternsWhenSomeSet() {
        underTest.addUriPattern(NON_MATCHING_PATTERN);
        underTest.addUriPattern(MATCHING_PATTERN_1);
        underTest.addUriPattern(MATCHING_PATTERN_2);

        List<String> foundPatterns = underTest.getUriPatterns();

        assertEquals( 3, foundPatterns.size());
        assertTrue(foundPatterns.contains(NON_MATCHING_PATTERN));
        assertTrue(foundPatterns.contains(MATCHING_PATTERN_1));
        assertTrue(foundPatterns.contains(MATCHING_PATTERN_2));
    }

}
