package it.mulders.modernrpc.rmi.client;

import it.mulders.modernrpc.client.shared.FibonacciClientOutput;
import it.mulders.modernrpc.client.shared.FibonacciClientStrategy;
import it.mulders.modernrpc.client.shared.Method;
import it.mulders.modernrpc.rmi.shared.Fibonacci;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.CompletableFuture;

import static it.mulders.modernrpc.rmi.shared.Fibonacci.STUB_NAME;

public class RmiClientStrategy implements FibonacciClientStrategy {
    @Override
    public CompletableFuture<FibonacciClientOutput> singleCall(int input) {
        var result = new CompletableFuture<FibonacciClientOutput>();

        new Thread(() -> {
            try {
                var start = System.currentTimeMillis();

                var registry = LocateRegistry.getRegistry("localhost");
                var stub = (Fibonacci) registry.lookup(STUB_NAME);
                var output = stub.fibonacci(input);

                var end = System.currentTimeMillis();
                var duration = end - start;

                result.complete(new FibonacciClientOutput(output.getResult(), output.getDuration(), duration));
            } catch (final RemoteException | NotBoundException e) {
                result.completeExceptionally(e);
            }
        }).start();

        return result;
    }

    @Override
    public boolean handles(final Method method) {
        return Method.RMI.equals(method);
    }

    @Override
    public String getName() {
        return "RMI";
    }
}
