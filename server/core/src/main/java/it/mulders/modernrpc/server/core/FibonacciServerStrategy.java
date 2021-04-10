package it.mulders.modernrpc.server.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public interface FibonacciServerStrategy {
    String getName();
    CompletableFuture<Void> start(final ExecutorService executorService, final FibonacciCalculator calculator);
    void stop();
}
