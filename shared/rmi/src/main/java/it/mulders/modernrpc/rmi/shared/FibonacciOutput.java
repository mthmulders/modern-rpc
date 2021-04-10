package it.mulders.modernrpc.rmi.shared;

import java.io.Serializable;

public class FibonacciOutput implements Serializable {
    private final long duration;
    private final long result;

    public FibonacciOutput(final long result, final long duration) {
        this.duration = duration;
        this.result = result;
    }

    public long getDuration() {
        return this.duration;
    }

    public long getResult() {
        return this.result;
    }
}
