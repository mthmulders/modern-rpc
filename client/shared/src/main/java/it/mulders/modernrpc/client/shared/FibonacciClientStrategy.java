package it.mulders.modernrpc.client.shared;

import java.util.concurrent.CompletableFuture;

public interface FibonacciClientStrategy {
    CompletableFuture<FibonacciClientOutput> singleCall(final int input);
    String getName();
    boolean handles(final Method method);
}
