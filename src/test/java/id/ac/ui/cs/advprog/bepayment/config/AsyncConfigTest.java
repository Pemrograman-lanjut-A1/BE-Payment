package id.ac.ui.cs.advprog.bepayment.config;

import id.ac.ui.cs.advprog.bepayment.config.AsyncConfig;
import id.ac.ui.cs.advprog.bepayment.config.AsyncExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AsyncConfigTest {

    @Mock
    private AsyncExceptionHandler asyncExceptionHandler;

    @InjectMocks
    private AsyncConfig asyncConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAsyncExecutor() {
        Executor executor = asyncConfig.asyncExecutor();

        assertNotNull(executor);
        assertEquals(ThreadPoolTaskExecutor.class, executor.getClass());

        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) executor;
        assertEquals(5, taskExecutor.getCorePoolSize());
        assertEquals(10, taskExecutor.getMaxPoolSize());
        assertEquals(500, taskExecutor.getQueueCapacity());
        assertEquals("Async-example-thread-", taskExecutor.getThreadNamePrefix());
    }

    @Test
    void testGetAsyncExecutor() {
        Executor asyncExecutor = asyncConfig.getAsyncExecutor();

        assertNotNull(asyncExecutor);
    }

    @Test
    void testGetAsyncUncaughtExceptionHandler() {
        AsyncUncaughtExceptionHandler exceptionHandler = asyncConfig.getAsyncUncaughtExceptionHandler();

        assertNotNull(exceptionHandler);
        assertEquals(asyncExceptionHandler, exceptionHandler);
    }
}
