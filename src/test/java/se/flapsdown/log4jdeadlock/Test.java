package se.flapsdown.log4jdeadlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    private static final Logger LOG = LoggerFactory.getLogger(Test.class);

    @org.junit.Test
    public void test_with_lots_of_threads() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Thread.sleep(1);
            executorService.submit(() -> LOG.info("hello"));
        }

    }
}
