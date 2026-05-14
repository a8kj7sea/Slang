package me.a8kj.slang.core.contracts;

import me.a8kj.slang.core.domain.NglPayload;
import me.a8kj.slang.core.domain.ProxyCredentials;
import me.a8kj.slang.core.result.Result;

/**
 * Contract responsible for dispatching payloads through a network layer.
 * Implementations of this interface define how requests are sent,
 * including handling proxy configuration and user-agent customization.
 *
 * @author a8kj7sea
 */
public interface Dispatcher {

    /**
     * Sends an {@link NglPayload} using the provided user-agent and proxy configuration.
     *
     * @param payload   the payload containing request data to be dispatched
     * @param userAgent the user-agent string used for the request context
     * @param proxy     proxy credentials used to route the request
     * @return a {@link Result} containing the response status code if successful,
     *         or an error if the dispatch fails
     */
    Result<Integer> send(NglPayload payload, String userAgent, ProxyCredentials proxy);
}