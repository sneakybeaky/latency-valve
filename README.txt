This valve can be added to a tomcat 6.x server to add latency to requests matching specific URI patterns. The valve
can be configured via JMX to modify / reset settings as needed. This is primarily meant for testing.

To install the valve, use maven to build the project and then copy the resulting JAR to $CATALINA_HOME/lib and then
modify server.xml and add a line like this within a Host section :

          <Valve className="com.ninedemons.catalina.valves.LatencyValve" enabled="false"/>

Start up tomcat with JMX enabled and you will see the bean exposed along with the methods needed to configure it.