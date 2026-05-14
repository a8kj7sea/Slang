package me.a8kj.slang.core.contracts;

/**
 * Defines a fluent messaging flow for constructing and sending requests.
 * <p>
 * This interface provides a chainable API for setting a target,
 * defining a message, and dispatching it.
 *
 * @author a8kj7sea
 */
public interface SerqFlow {

    /**
     * Sets the target recipient for the flow.
     *
     * @param target the destination identifier
     * @return the current {@link SerqFlow} instance for chaining
     */
    SerqFlow to(String target);

    /**
     * Sets the message content to be sent.
     *
     * @param content the message body
     * @return the current {@link SerqFlow} instance for chaining
     */
    SerqFlow message(String content);

    /**
     * Executes the flow and sends the constructed request.
     */
    void send();
}