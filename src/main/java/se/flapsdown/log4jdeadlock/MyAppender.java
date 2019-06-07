package se.flapsdown.log4jdeadlock;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAppender extends ConsoleAppender {

    static Logger LOG = LoggerFactory.getLogger(MyAppender.class);

    int cnt = 0;
    @Override
    public void append(LoggingEvent event) {
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        cnt++;
        //if (cnt % 2 == 0)
            LOG.info("append() " + Thread.currentThread().getName());

        super.append(event);
    }
}
