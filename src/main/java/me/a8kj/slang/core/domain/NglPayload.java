package me.a8kj.slang.core.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * Immutable payload used for sending requests to the target system.
 * Contains all required metadata for dispatching a message.
 *
 * @author a8kj7sea
 */
@Value
@Builder
@Jacksonized
public class NglPayload {

    /**
     * Target username or recipient identifier.
     */
    String username;

    /**
     * The question or message content being sent.
     */
    String question;

    /**
     * Unique device identifier used for request tracking.
     */
    String deviceId;

    /**
     * Game or service slug identifier.
     */
    String gameSlug;

    /**
     * Optional referrer field used for request metadata.
     */
    @Builder.Default
    String referrer = "";
}