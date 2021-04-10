package it.mulders.modernrpc.rmi.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Fibonacci extends Remote {
    FibonacciOutput fibonacci(final int input) throws RemoteException;
}
