package me.a8kj.slang;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.a8kj.slang.engine.SerqProtocolEngine;
import me.a8kj.slang.infrastructure.network.OkHttpStealthDispatcher;
import me.a8kj.slang.infrastructure.persistence.FileIdentityProvider;
import me.a8kj.slang.infrastructure.persistence.FileResourceProvider;

/**
 * Singleton Facade API for Serq system.
 * Provides simplified access to the underlying {@link SerqProtocolEngine}.
 *
 * @author a8kj7sea
 */
public final class SerqAPI {

    private static volatile SerqAPI instance;

    private final SerqProtocolEngine engine;

    private SerqAPI() {

        ObjectMapper mapper = new ObjectMapper();

        this.engine = SerqProtocolEngine.builder()
                .identityProvider(new FileIdentityProvider())
                .proxyProvider(new FileResourceProvider("proxies.txt"))
                .agentProvider(new FileResourceProvider("agents.txt"))
                .dispatcher(new OkHttpStealthDispatcher(mapper))
                .build();

        this.engine.initialize();
    }

    public static SerqAPI getInstance() {

        if (instance == null) {
            synchronized (SerqAPI.class) {
                if (instance == null) {
                    instance = new SerqAPI();
                }
            }
        }

        return instance;
    }

    public void send(String target, String message) {
        engine.launch(target, message);
    }

    public void sendFlow(String target, String message) {
        engine.to(target)
                .message(message)
                .send();
    }

    public SerqProtocolEngine engine() {
        return engine;
    }
}