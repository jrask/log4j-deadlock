package se.flapsdown.log4jdeadlock;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Enumeration;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Buffer extends AppenderSkeleton implements AppenderAttachable {

    int cnt = 0;
    AtomicInteger dropped = new AtomicInteger(0);

    AppenderAttachableImpl impl = new AppenderAttachableImpl();
    BlockingQueue<LoggingEvent> buffer = new LinkedBlockingQueue<>(1000);

    {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    work();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        })  ;
    }

    private void work() throws InterruptedException {
         LoggingEvent poll = buffer.take();

        synchronized(this.impl) {
            this.impl.appendLoopOnAppenders(poll);
        }
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        cnt++;
        if (cnt % 100 == 0)
            System.out.println("buffer " + buffer.size() + ", dropped = " + dropped.get());
        boolean offer = buffer.offer(loggingEvent);
        if (!offer)
            dropped.incrementAndGet();
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    @Override
    public void addAppender(Appender appender) {
        System.out.println(appender.getName());
        impl.addAppender(appender);
    }

    @Override
    public Enumeration getAllAppenders() {
        return impl.getAllAppenders();
    }

    @Override
    public Appender getAppender(String s) {
        return impl.getAppender(s);
    }

    @Override
    public boolean isAttached(Appender appender) {
        return impl.isAttached(appender);
    }

    @Override
    public void removeAllAppenders() {
        impl.removeAllAppenders();
    }

    @Override
    public void removeAppender(Appender appender) {
        impl.removeAppender(appender);
    }

    @Override
    public void removeAppender(String s) {
        impl.removeAppender(s);
    }
}
