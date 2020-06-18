package lesson16;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SqrtClient {
	public static void callRMI() {
            
        try{final Registry 	registry = LocateRegistry.getRegistry("localhost",SqrtServerImpl.PORT);	// <1>
            final Sqrt		stub = (Sqrt) registry.lookup("Sqrt");		// <2>
            
			System.err.println("SQRT(25)=" + stub.sqrt(4));			// <3>
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		callRMI();
	}	
}
