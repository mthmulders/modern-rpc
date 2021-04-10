package it.mulders.modernrpc.rmi.shared;

import java.io.Serializable;

public class FibonacciOutput implements Serializable {
    private final long duration;
    private final int result;

    public FibonacciOutput(final int result, final long duration) {
        this.duration = duration;
        this.result = result;
    }

    public long getDuration() {
        return this.duration;
    }

    public int getResult() {
        return this.result;
    }
}
