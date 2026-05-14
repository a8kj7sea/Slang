package me.a8kj.slang.core.contracts;

import me.a8kj.slang.core.result.Result;

/**
 * Generic contract for providing and managing sequential resources such as
 * proxies, agents, or other reusable external assets.
 *
 * @param <T> the type of resource being provided
 *
 * @author a8kj7sea
 */
public interface ResourceProvider<T> {

    /**
     * Fetches the next available resource from the provider.
     *
     * @return a {@link Result} containing the next resource if available,
     *         or an error if retrieval fails
     */
    Result<T> fetchNext();

    /**
     * Synchronizes or refreshes the underlying resource pool.
     *
     * @return a {@link Result} indicating success or failure of the sync operation
     */
    Result<Void> sync();
}