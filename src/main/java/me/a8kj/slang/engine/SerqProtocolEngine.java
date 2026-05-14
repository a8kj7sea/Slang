package me.a8kj.slang.engine;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import me.a8kj.slang.core.contracts.Dispatcher;
import me.a8kj.slang.core.contracts.IdentityProvider;
import me.a8kj.slang.core.contracts.ResourceProvider;
import me.a8kj.slang.core.contracts.SerqFlow;
import me.a8kj.slang.core.domain.NglPayload;
import me.a8kj.slang.core.domain.ProxyCredentials;
import me.a8kj.slang.core.result.Result;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Core execution engine responsible for orchestrating identity,
 * resources, and dispatch operations in a controlled flow.
 *
 * Designed as a singleton-managed engine with fluent API support.
 *
 * @author a8kj7sea
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SerqProtocolEngine implements SerqFlow {

    private final IdentityProvider identityProvider;
    private final ResourceProvider<String> proxyProvider;
    private final ResourceProvider<String> agentProvider;
    private final Dispatcher dispatcher;

    private String currentTarget;
    private String currentMessage;

    private static SerqProtocolEngine instance;

    public static synchronized void init(SerqProtocolEngine engine) {
        if (instance == null) {
            instance = engine;
        }
    }

    public static SerqProtocolEngine getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Engine not initialized");
        }
        return instance;
    }

    @Override
    public SerqFlow to(String target) {
        this.currentTarget = target;
        return this;
    }

    @Override
    public SerqFlow message(String content) {

        if (content == null || content.isBlank()) {
            System.err.println("[serq-Engine] Validation Error: Message is empty.");
            return this;
        }

        if (content.length() > 300) {
            System.err.println("[serq-Engine] Validation Error: Message exceeds 300 characters.");
            return this;
        }

        this.currentMessage = content;
        return this;
    }

    @Override
    public void send() {

        if (currentTarget == null || currentMessage == null) {
            System.err.println("[serq-Engine] Dispatch aborted: Missing target or valid message.");
            return;
        }

        launch(currentTarget, currentMessage);

        this.currentTarget = null;
        this.currentMessage = null;
    }

    public void initialize() {

        Result<Void> proxySync = proxyProvider.sync();

        if (proxySync instanceof Result.Failure<Void> failure) {
            System.err.println("[Init] Proxy Sync Error: " + failure.error());
        }

        Result<Void> agentSync = agentProvider.sync();

        if (agentSync instanceof Result.Failure<Void> failure) {
            System.err.println("[Init] Agent Sync Error: " + failure.error());
        }
    }

    public void launch(String target, String message) {

        Thread.ofVirtual().start(() -> {

            try {

                long delay = ThreadLocalRandom.current().nextLong(500, 1501);
                Thread.sleep(delay);

                Result<String> idRes = identityProvider.getIdentifier();
                Result<String> proxyRes = proxyProvider.fetchNext();
                Result<String> agentRes = agentProvider.fetchNext();

                if (!(idRes instanceof Result.Success<String> idSuccess)) {
                    System.err.println("[serq-Engine] Identity Provider Failure.");
                    return;
                }

                if (!(proxyRes instanceof Result.Success<String> proxySuccess)) {
                    System.err.println("[serq-Engine] Proxy Provider Failure.");
                    return;
                }

                if (!(agentRes instanceof Result.Success<String> agentSuccess)) {
                    System.err.println("[serq-Engine] Agent Provider Failure.");
                    return;
                }

                String[] p = proxySuccess.value().split(":");

                if (p.length != 4) {
                    System.err.println("[serq-Engine] Invalid proxy format.");
                    return;
                }

                ProxyCredentials creds = ProxyCredentials.builder()
                        .host(p[0])
                        .port(Integer.parseInt(p[1]))
                        .username(p[2])
                        .password(p[3])
                        .build();

                NglPayload payload = NglPayload.builder()
                        .username(target)
                        .question(message)
                        .deviceId(idSuccess.value())
                        .gameSlug("ngl")
                        .build();

                Result<Integer> dispatchResult =
                        dispatcher.send(payload, agentSuccess.value(), creds);

                if (dispatchResult instanceof Result.Success<Integer> success) {

                    System.out.printf(
                            "[serq-Engine] Delivered: %d | Node: %s%n",
                            success.value(),
                            p[0]
                    );

                } else if (dispatchResult instanceof Result.Failure<Integer> failure) {

                    System.err.println("[serq-Engine] Dispatch Error: " + failure.error());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("[serq-Engine] Runtime Exception: " + e.getMessage());
            }
        });
    }
}