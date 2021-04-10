package it.mulders.modernrpc.server.core;

import it.mulders.modernrpc.shared.core.FibonacciOutput;

public class FibonacciCalculator {
    public FibonacciOutput fibonacci(final int input) {
        var start = System.currentTimeMillis();
        var result = calculateFibonacci(input);
        var end = System.currentTimeMillis();
        return new FibonacciOutput(result, end - start);
    }

    private int calculateFibonacci(final int input) {
        if (input == 0) return 0;
        if (input == 1) return 1;

        return calculateFibonacci(input - 1) + calculateFibonacci(input -2);
    }
}
