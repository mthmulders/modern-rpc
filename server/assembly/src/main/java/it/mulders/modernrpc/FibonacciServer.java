package it.mulders.modernrpc;

import it.mulders.modernrpc.server.core.FibonacciCalculator;
import it.mulders.modernrpc.server.core.FibonacciServerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

public class FibonacciServer {
    private static final Logger logger = LoggerFactory.getLogger(FibonacciServer.class);

    public static void main(final String... args) throws InterruptedException {
        logger.info("Starting Fibonacci servers");

        var calculator = new FibonacciCalculator();

        var executorService = Executors.newFixedThreadPool(8);

        var strategies = findServerStrategies();

        strategies.stream()
                .peek(strategy -> logger.info("Found {} server", strategy.getName()))
                .map(strategy -> strategy.start(executorService, calculator))
                .forEach(CompletableFuture::join);

        logger.info("Press [enter] to stop the servers");

        System.console().readLine();

        strategies.forEach(FibonacciServerStrategy::stop);
        logger.info("All servers stopped");

        executorService.shutdown();
        logger.info("Executor Service shut down");
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    static List<FibonacciServerStrategy> findServerStrategies() {
        var loader = ServiceLoader.load(FibonacciServerStrategy.class);
        return loader.stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
