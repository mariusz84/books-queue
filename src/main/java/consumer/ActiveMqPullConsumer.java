package consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.MqConnector;

import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.*;

public class ActiveMqPullConsumer implements QueueConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMqPullConsumer.class);
    private static final int INITIAL_DELAY = 60;
    private static final int REPETITION_FREQUENCY = 180;
    private static final int NUMBER_OF_THREADS = 1;
    private static Session session;
    private static Queue queue;

    public static void receiveMessageFromQueueIfQueueIsNotEmpty() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(NUMBER_OF_THREADS);
        Runnable task = () -> receiveMessageFromQueue();
        scheduledExecutorService.scheduleAtFixedRate(task, INITIAL_DELAY, REPETITION_FREQUENCY, TimeUnit.SECONDS);
    }

    private static void receiveMessageFromQueue() {
        session = MqConnector.getSession();
        queue = MqConnector.setupQueue();

        try {
            if (QueueOperations.isQueueEmpty(session, queue) == false) {
                MessageConsumer consumer = MqConnector.getMessageConsumer();
                TextMessage textMsg = (TextMessage) consumer.receive();
                LOGGER.info("Received: " + textMsg.getText());
            } else {
                LOGGER.info("Message queue is empty");
            }
            MqConnector.cleanUpQueue();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }
}