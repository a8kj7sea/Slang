package me.a8kj.slang.core.domain;

import lombok.Builder;
import lombok.Value;

/**
 * Immutable proxy configuration used for routing network requests
 * through authenticated proxy servers.
 *
 * @author a8kj7sea
 */
@Value
@Builder
public class ProxyCredentials {

    /**
     * Proxy server host address.
     */
    String host;

    /**
     * Proxy server port.
     */
    int port;

    /**
     * Username used for proxy authentication.
     */
    String username;

    /**
     * Password used for proxy authentication.
     */
    String password;
}