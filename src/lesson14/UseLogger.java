package lesson14;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class UseLogger {
	private static Logger logger;	// = Logger.getLogger("MyLogger");	

	public static void main(String[] args) throws IOException {
		try(final InputStream	is = UseLogger.class.getResourceAsStream("logger.conf")) {
			LogManager.getLogManager().readConfiguration(is);
		}
		logger = Logger.getLogger("MyLogger");
		
		if (logger.isLoggable(Level.INFO)) {
//			System.err.println("SE:   Test message");
			logger.log(Level.INFO,"Test message"); // System.err.println("Test message");
		}
		logger.log(Level.SEVERE,"Test error", new RuntimeException("Simulate error"));	// <6>
	}
}
