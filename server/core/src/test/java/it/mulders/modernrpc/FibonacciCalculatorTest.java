package it.mulders.modernrpc;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class FibonacciCalculatorTest implements WithAssertions {
    private static Stream<Arguments> fibonacciTestcases() {
        // Copied from https://en.wikipedia.org/wiki/Fibonacci_number#Sequence_properties.
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(1, 1),
                Arguments.of(2, 1),
                Arguments.of(3, 2),
                Arguments.of(4, 3),
                Arguments.of(5, 5),
                Arguments.of(6, 8),
                Arguments.of(7, 13),
                Arguments.of(8, 21),
                Arguments.of(9, 34),
                Arguments.of(10, 55),
                Arguments.of(11, 89),
                Arguments.of(12, 144),
                Arguments.of(13, 233),
                Arguments.of(14, 377),
                Arguments.of(15, 610),
                Arguments.of(16, 987),
                Arguments.of(17, 1597),
                Arguments.of(18, 2584),
                Arguments.of(19, 4181),
                Arguments.of(20, 6765)
        );
    }

    private final FibonacciCalculator calculator = new FibonacciCalculator();

    @MethodSource( "fibonacciTestcases")
    @ParameterizedTest(name = "fibonacci({0}) = {1}")
    void test_fibonacci_number(final int input, final int output) {
        assertThat(calculator.fibonacci(input).result()).isEqualTo(output);
    }

    @Test
    void result_has_duration() {
        assertThat(calculator.fibonacci(20).duration()).isGreaterThan(1);
    }
}