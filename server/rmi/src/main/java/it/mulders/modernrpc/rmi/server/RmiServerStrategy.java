package it.mulders.modernrpc.rmi.server;

import it.mulders.modernrpc.rmi.shared.Fibonacci;
import it.mulders.modernrpc.server.core.FibonacciCalculator;
import it.mulders.modernrpc.server.core.FibonacciServerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class RmiServerStrategy implements FibonacciServerStrategy {
    private static final String NAME = "Fibonacci";
    private static final Logger logger = LoggerFactory.getLogger(RmiServerStrategy.class);

    private Registry registry;
    private Fibonacci server;

    @Override
    public String getName() {
        return "RMI";
    }

    @Override
    public CompletableFuture<Void> start(final ExecutorService executorService, final FibonacciCalculator calculator) {
        var result = new CompletableFuture<Void>();

        this.server = new FibonacciRmiServer(calculator);

        executorService.submit(() -> {
            logger.info("Starting RMI server");
            try {
                logger.debug("Locating RMI registry");
                this.registry = LocateRegistry.getRegistry();

                logger.debug("Exporting implementation");
                var stub = UnicastRemoteObject.exportObject(server, 2001);

                logger.debug("Binding stub in registry");
                registry.bind(NAME, stub);
                result.complete(null);
            } catch (final RemoteException | AlreadyBoundException e) {
                logger.error("Could not start RMI server: {}", e.getLocalizedMessage(), e);
                result.completeExceptionally(e);
            }
            logger.info("Started RMI server");
        });

        return result;
    }

    @Override
    public void stop() {
        if (registry == null) {
            logger.info("RMI registry not located");
            return;
        }
        if (server == null) {
            logger.info("RMI server not running");
            return;
        }

        logger.info("Stopping RMI server");
        try {
            registry.unbind(NAME);
            UnicastRemoteObject.unexportObject(server, true);
        } catch (RemoteException | NotBoundException e) {
            logger.error( "Error while stopping RMI server: {}", e.getLocalizedMessage(), e);
        }
    }
}
