package it.mulders.modernrpc.rmi.server;

import it.mulders.modernrpc.rmi.shared.Fibonacci;
import it.mulders.modernrpc.rmi.shared.FibonacciOutput;
import it.mulders.modernrpc.server.core.FibonacciCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FibonacciRmiServer implements Fibonacci {
    private static final Logger logger = LoggerFactory.getLogger(FibonacciRmiServer.class);

    private final FibonacciCalculator calculator;

    public FibonacciRmiServer(final FibonacciCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public FibonacciOutput fibonacci(int input) {
        logger.debug("Calculating Fibonacci number with input {}", input);
        var result = calculator.fibonacci(input);
        return new FibonacciOutput(result.result(), result.duration());
    }
}
