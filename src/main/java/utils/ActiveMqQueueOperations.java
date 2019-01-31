package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.Enumeration;
import java.util.concurrent.*;

public class ActiveMqQueueOperations {
    private static final Logger log = LoggerFactory.getLogger(ActiveMqQueueOperations.class);
    private static final int DELAY_TO_CHECK_QUEUE = 60;
    protected static Connection connection;
    protected static Session session;
    protected static Queue queue;

    public static void receiveMessageFromQueueIfQueueIsNotEmpty() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> receiveMessageFromQueue();
        scheduledExecutorService.schedule(task, DELAY_TO_CHECK_QUEUE, TimeUnit.SECONDS);
    }

    private static void receiveMessageFromQueue() {
        try {
            MqConnector.setupQueue();

            MessageConsumer consumer = MqConnector.getMessageConsumer();
            session = MqConnector.getSession();
            if (isQueueEmpty() == false) {
                TextMessage textMsg = (TextMessage) consumer.receive();
                System.out.println(textMsg.getText());
                log.info("Received: " + textMsg.getText());
            } else {
                log.info("Message queue is empty");
            }

            MqConnector.cleanUpQueue();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private static boolean isQueueEmpty() {
        Enumeration enumeration = null;
        try {
            QueueBrowser queueBrowser = session.createBrowser(queue);
            enumeration = queueBrowser.getEnumeration();
        } catch (JMSException e) {
            log.info(e.getMessage());
        }
        return !enumeration.hasMoreElements();
    }
}