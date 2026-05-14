package me.a8kj.slang.infrastructure.persistence;

import me.a8kj.slang.core.contracts.ResourceProvider;
import me.a8kj.slang.core.result.Result;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * File-based implementation of {@link ResourceProvider} that loads
 * resources (such as proxies or agents) from a local file into memory.
 *
 * @param <T> type of resource stored as String entries in file
 *
 * @author a8kj7sea
 */
public class FileResourceProvider implements ResourceProvider<String> {

    private final Path path;
    private List<String> cache;

    public FileResourceProvider(String filePath) {
        this.path = Paths.get(filePath);
    }

    @Override
    public synchronized Result<Void> sync() {

        try {

            if (!Files.exists(path)) {
                return Result.failure("File not found: " + path);
            }

            this.cache = Files.readAllLines(path);

            if (cache == null || cache.isEmpty()) {
                return Result.failure("Resource file is empty: " + path);
            }

            return Result.success(null);

        } catch (Exception e) {
            return Result.failure(e);
        }
    }

    @Override
    public Result<String> fetchNext() {

        if (cache == null || cache.isEmpty()) {
            return Result.failure("Resources not synchronized or empty.");
        }

        String randomItem =
                cache.get(ThreadLocalRandom.current().nextInt(cache.size()));

        return Result.success(randomItem);
    }
}