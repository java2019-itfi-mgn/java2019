package lesson13;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorFactory;

public class JMXServer {
	private final Semaphore	sema = new Semaphore(0);	// <1>
	
	private JMXServer() {		
	}
	
	public void stop() {	// <2>
		sema.release();
	}
	
	public void waitTheEnd() {	// <3>
		try{sema.acquire();
		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] args) throws Exception {
		final JMXServer		server = new JMXServer();
		final MBeanServer 	mbs = ManagementFactory.getPlatformMBeanServer();	// <4> 
	    final ObjectName 	name = new ObjectName("java2019:type=JMXServer");	// <6>
	    final JMXInterface	jmx = new JMXInterface(server); 			// <7>

	    OperatingSystemMXBean oxb = ManagementFactory.getOperatingSystemMXBean();
	    
	    mbs.registerMBean(jmx, name);	// <8>
	    System.err.println("Start JMX Server");
	    server.waitTheEnd();			// <9>
	    System.err.println("Stop JMX Server");
	    mbs.unregisterMBean(name);		// <10>
	}
}
