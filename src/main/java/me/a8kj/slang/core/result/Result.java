package me.a8kj.slang.core.result;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A functional-style result wrapper representing either a successful value
 * or a failure with an associated error.
 *
 * @param <T> the type of the success value
 *
 * @author a8kj7sea
 */
public sealed interface Result<T> permits Result.Success, Result.Failure {

    /**
     * Represents a successful computation result.
     *
     * @param value the computed value
     */
    record Success<T>(T value) implements Result<T> {}

    /**
     * Represents a failed computation result.
     *
     * @param error the error that caused the failure
     */
    record Failure<T>(Throwable error) implements Result<T> {}

    /**
     * Creates a successful result.
     *
     * @param value the success value
     * @param <T>   type of the value
     * @return a successful {@link Result}
     */
    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * Creates a failed result from an exception.
     *
     * @param error the throwable error
     * @param <T>   type of the expected value
     * @return a failed {@link Result}
     */
    static <T> Result<T> failure(Throwable error) {
        return new Failure<>(error);
    }

    /**
     * Creates a failed result from an error message.
     *
     * @param message the error message
     * @param <T>     type of the expected value
     * @return a failed {@link Result}
     */
    static <T> Result<T> failure(String message) {
        return new Failure<>(new RuntimeException(message));
    }

    /**
     * Checks if this result is successful.
     *
     * @return true if success, otherwise false
     */
    default boolean isSuccess() {
        return this instanceof Success;
    }

    /**
     * Executes the given consumer if this is a success result.
     *
     * @param consumer action to execute with the success value
     */
    default void ifSuccess(Consumer<T> consumer) {
        if (this instanceof Success<T> s) {
            consumer.accept(s.value());
        }
    }

    /**
     * Executes the given consumer if this is a failure result.
     *
     * @param consumer action to execute with the error
     */
    default void ifFailure(Consumer<Throwable> consumer) {
        if (this instanceof Failure<T> f) {
            consumer.accept(f.error());
        }
    }

    /**
     * Transforms the success value into another type.
     *
     * @param mapper transformation function
     * @param <R>    new result type
     * @return mapped {@link Result}
     */
    default <R> Result<R> map(Function<T, R> mapper) {
        if (this instanceof Success<T> s) {
            try {
                return success(mapper.apply(s.value()));
            } catch (Exception e) {
                return failure(e);
            }
        }
        return failure(((Failure<T>) this).error());
    }

    /**
     * Flat-maps the success value into another {@link Result}.
     *
     * @param mapper transformation function returning a Result
     * @param <R>    new result type
     * @return flattened {@link Result}
     */
    default <R> Result<R> flatMap(Function<T, Result<R>> mapper) {
        if (this instanceof Success<T> s) {
            try {
                return mapper.apply(s.value());
            } catch (Exception e) {
                return failure(e);
            }
        }
        return failure(((Failure<T>) this).error());
    }
}