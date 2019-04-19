package consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import listener.BooksQueueMessageListener;
import utils.MqConnector;

import javax.jms.*;

import static utils.MqConnector.*;

public class ActiveMqPushConsumer implements QueueConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMqPushConsumer.class);
    private static Session session;
    private static Queue queue;

    public static void receiveMessageFromQueueIfQueueIsNotEmpty() {
        session = MqConnector.getSession();
        queue = MqConnector.setupQueue();

        try {
            if (QueueOperations.isQueueEmpty(session, queue) == false) {
                MessageConsumer consumer = getMessageConsumer();
                consumer.setMessageListener(new BooksQueueMessageListener());
                consumer.receive();
            } else {
                LOGGER.info("Message queue is empty");
            }
            cleanUpQueue();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }
}