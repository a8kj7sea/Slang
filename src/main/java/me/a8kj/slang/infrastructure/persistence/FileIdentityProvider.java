package me.a8kj.slang.infrastructure.persistence;

import me.a8kj.slang.core.contracts.IdentityProvider;
import me.a8kj.slang.core.result.Result;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * File-based implementation of {@link IdentityProvider}.
 * <p>
 * Stores a generated UUID identifier in a local file and reuses it
 * for subsequent calls.
 *
 * @author a8kj7sea
 */
public class FileIdentityProvider implements IdentityProvider {

    private static final Path PATH = Paths.get("identity.serq");

    @Override
    public synchronized Result<String> getIdentifier() {

        try {

            if (Files.exists(PATH)) {
                return Result.success(Files.readString(PATH).trim());
            }

            String id = UUID.randomUUID().toString();

            Files.writeString(PATH, id);

            return Result.success(id);

        } catch (Exception e) {
            return Result.failure(e);
        }
    }
}