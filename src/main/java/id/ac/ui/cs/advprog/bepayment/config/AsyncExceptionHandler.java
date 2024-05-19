package id.ac.ui.cs.advprog.bepayment.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    Logger logger = Logger.getLogger(getClass().getName());
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... args) {
        logger.log(Level.SEVERE, () -> String.format("Method Name: %s --- Args: %s --- Error Message: %s",
                method.getName(), Arrays.toString(args), ex.getMessage()));
    }
}
