package me.a8kj.slang.infrastructure.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.a8kj.slang.core.contracts.Dispatcher;
import me.a8kj.slang.core.domain.NglPayload;
import me.a8kj.slang.core.domain.ProxyCredentials;
import me.a8kj.slang.core.result.Result;
import okhttp3.*;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * OkHttp-based implementation of {@link Dispatcher} that sends requests
 * through HTTP proxies with basic stealth headers support.
 *
 * @author a8kj7sea
 */
@RequiredArgsConstructor
public class OkHttpStealthDispatcher implements Dispatcher {

    private final ObjectMapper mapper;

    private static final String ENDPOINT = "https://ngl.link/api/submit";

    @Override
    public Result<Integer> send(NglPayload payload, String userAgent, ProxyCredentials creds) {

        try {

            Proxy proxy = new Proxy(
                    Proxy.Type.HTTP,
                    new InetSocketAddress(creds.getHost(), creds.getPort())
            );

            Authenticator auth = (route, response) -> {
                String credential = Credentials.basic(
                        creds.getUsername(),
                        creds.getPassword()
                );

                return response.request()
                        .newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .proxy(proxy)
                    .proxyAuthenticator(auth)
                    .build();

            RequestBody body = RequestBody.create(
                    mapper.writeValueAsString(payload),
                    MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                    .url(ENDPOINT)
                    .header("User-Agent", userAgent)
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("Referer", "https://ngl.link/" + payload.getUsername())
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return Result.success(response.code());
            }

        } catch (Exception e) {
            return Result.failure(e);
        }
    }
}