package consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.MqConnector;

import javax.jms.*;
import java.util.Enumeration;
import java.util.concurrent.*;

public class ActiveMqPullConsumer {
    private static final Logger log = LoggerFactory.getLogger(ActiveMqPullConsumer.class);
    private static final int INITIAL_DELAY = 60;
    private static final int REPETITION_FREQUENCY = 180;
    private static Session session;
    private static Queue queue;

    public static void receiveMessageFromQueueIfQueueIsNotEmpty() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> receiveMessageFromQueue();
        scheduledExecutorService.scheduleAtFixedRate(task, INITIAL_DELAY, REPETITION_FREQUENCY, TimeUnit.SECONDS);
    }

    private static void receiveMessageFromQueue() {
        prepareQueue();
        try {
            if (isQueueEmpty() == false) {
                MessageConsumer consumer = MqConnector.getMessageConsumer();
                TextMessage textMsg = (TextMessage) consumer.receive();
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

    private static void prepareQueue() {
        session = MqConnector.getSession();
        queue = MqConnector.setupQueue();
    }
}