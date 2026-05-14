package me.a8kj.slang.core.contracts;

import me.a8kj.slang.core.result.Result;

/**
 * Provides a unique identity identifier used for request tracking or device/session identification.
 * Implementations define how identifiers are generated or retrieved.
 *
 * @author a8kj7sea
 */
public interface IdentityProvider {

    /**
     * Retrieves a unique identifier.
     *
     * @return a {@link Result} containing the identifier string if successful,
     *         or an error if generation or retrieval fails
     */
    Result<String> getIdentifier();
}