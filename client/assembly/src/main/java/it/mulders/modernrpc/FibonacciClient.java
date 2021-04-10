package it.mulders.modernrpc;

import it.mulders.modernrpc.client.shared.FibonacciClientOutput;
import it.mulders.modernrpc.client.shared.FibonacciClientStrategy;
import it.mulders.modernrpc.client.shared.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FibonacciClient {
    private static final int NO_GOAL_SPECIFIED = -1;
    private static final int NOT_ENOUGH_PARAMETERS = -2;
    private static final int UNKNOWN_METHOD = -3;
    private static final int INVOCATION_FAILED = -4;
    private static final int UNKNOWN_ERROR = -5;
    private static final Logger logger = LoggerFactory.getLogger(FibonacciClient.class);

    public static void main(final String... args) {
        if (args.length == 0) {
            logger.error("Specify a goal with optional params");
            logger.error("");
            logger.error("Goals:");
            logger.error(" - single <method>");
            logger.error("");
            logger.error("Parameters and their values values:");
            logger.error(" - method: rmi");
            System.exit(NO_GOAL_SPECIFIED);
        }

        var goal = args[0];
        var remainder = new String[args.length - 1];
        System.arraycopy(args, 1, remainder, 0, args.length - 1);
        var exitCode = switch (goal) {
            case "single" -> single(remainder);
            default -> unknownGoal(goal);
        };
        System.exit(exitCode);
    }

    private static int single(final String... args) {
        if (args.length != 2 || !isInteger(args[1])) {
            logger.error("Specify the parameters for this goal");
            logger.error("");
            logger.error("Parameters and their values values:");
            logger.error(" - method: rmi");
            logger.error(" - input: <any integer value>");
            System.exit(NO_GOAL_SPECIFIED);
        }

        var method = args[0];
        var input = Integer.parseInt(args[1]);

        return Method.findMethod(method)
                .flatMap(FibonacciClient::findClientStrategy)
                .map(strategy -> strategy.singleCall(input))
                .map(future -> processResult(input, future))
                .map(FibonacciClient::awaitResult)
                .orElseGet(() -> {
                    logger.error("Unknown method {}", method);
                    return UNKNOWN_METHOD;
                });
    }

    private static CompletableFuture<Integer> processResult(final int input,
                                                            final CompletableFuture<FibonacciClientOutput> future) {
        return future.thenApply(result -> {
            logger.info("Fibonacci number #{} is {}", input, result.result());
            logger.info("Computation took {} ms on server", result.serverDuration());
            logger.info("Computation took {} ms in total", result.clientDuration());
            return 0;
        }).exceptionally(throwable -> {
            logger.error("Failed with {}", throwable.getLocalizedMessage(), throwable);
            return INVOCATION_FAILED;
        });
    }

    private static int awaitResult(final CompletableFuture<Integer> future) {
        try {
            return future.get();
        } catch (final ExecutionException | InterruptedException e) {
            logger.error("Failed with {}", e.getLocalizedMessage(), e);
            return UNKNOWN_ERROR;
        }
    }

    private static boolean isInteger(final String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private static int unknownGoal(final String command) {
        logger.error("Unknown goal {}", command);
        return NOT_ENOUGH_PARAMETERS;
    }

    private static Optional<FibonacciClientStrategy> findClientStrategy(final Method method) {
        logger.debug("Searching client strategy for method {}", method);
        var loader = ServiceLoader.load(FibonacciClientStrategy.class);
        return loader.stream()
                .map(ServiceLoader.Provider::get)
                .peek(strategy -> logger.info("Found {} client", strategy.getName()))
                .filter(strategy -> strategy.handles(method))
                .findAny();
    }
}
