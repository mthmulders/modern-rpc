package it.mulders.modernrpc.rmi.server;

import it.mulders.modernrpc.rmi.shared.Fibonacci;
import it.mulders.modernrpc.rmi.shared.FibonacciOutput;
import it.mulders.modernrpc.server.core.FibonacciCalculator;

public class FibonacciRmiServer implements Fibonacci {
    private final FibonacciCalculator calculator;

    public FibonacciRmiServer(final FibonacciCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public FibonacciOutput fibonacci(int input) {
        var result = calculator.fibonacci(input);
        return new FibonacciOutput(result.result(), result.duration());
    }
}
