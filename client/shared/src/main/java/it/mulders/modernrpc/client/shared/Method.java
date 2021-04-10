package it.mulders.modernrpc.client.shared;

import java.util.Arrays;
import java.util.Optional;

public enum Method {
    RMI("rmi");

    Method(final String value) {
        this.value = value;
    }

    private final String value;

    public static Optional<Method> findMethod(final String input) {
        return Arrays.stream(Method.values())
                .filter(method -> method.value.equalsIgnoreCase(input))
                .findAny();
    }
}
