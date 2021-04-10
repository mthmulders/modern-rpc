package it.mulders.modernrpc.client.shared;

public record FibonacciClientOutput(int result, long serverDuration, long clientDuration) {
}
