package lesson16;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SqrtServerImpl implements Sqrt {
	public static final int		PORT = 2000;
	
	@Override
	public double sqrt(double value) throws RemoteException {
		return Math.sqrt(value);
	}

	public static void createAndRegisterObject() {
        try{final SqrtServerImpl	obj = new SqrtServerImpl();		// <1>
            final Sqrt 				stub = (Sqrt) UnicastRemoteObject.exportObject(obj, 0);	// <2>
            final Registry 			registry = LocateRegistry.getRegistry(PORT);	// <3>
        	
			registry.bind("Sqrt", stub);		// <4>
			System.err.println("Server side was registered");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void unregisterObject() {
        try{final Registry 			registry = LocateRegistry.getRegistry(PORT);
	    	
			registry.unbind("Sqrt");			// <5>
			System.err.println("Server side was removed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		createAndRegisterObject();
		System.in.read();
		unregisterObject();
	}
}
