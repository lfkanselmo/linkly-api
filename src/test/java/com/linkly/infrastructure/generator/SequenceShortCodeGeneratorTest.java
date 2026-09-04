package com.linkly.infrastructure.generator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.linkly.TestcontainersConfiguration;
import com.linkly.domain.port.ShortCodeGenerator;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class SequenceShortCodeGeneratorTest {

    private static final int CONCURRENT_REQUESTS = 200;

    @Autowired
    private ShortCodeGenerator shortCodeGenerator;

    @Test
    void generatesNoCollisionsUnderConcurrentLoad() throws InterruptedException {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<String>> tasks = IntStream.range(0, CONCURRENT_REQUESTS)
                    .<Callable<String>>mapToObj(i -> shortCodeGenerator::generate)
                    .toList();
            List<Future<String>> futures = executor.invokeAll(tasks);
            Set<String> codes = futures.stream().map(this::resolve).collect(Collectors.toSet());
            assertThat(codes).hasSize(CONCURRENT_REQUESTS);
        }
    }

    private String resolve(Future<String> future) {
        try {
            return future.get();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
}
