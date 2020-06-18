package lesson16;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Sqrt extends Remote {	// <1>
    double sqrt(double value) throws RemoteException;	// <2>
	
//    public static void main(String[] args) throws IOException {
//	}
}
