package consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import listener.BooksQueueMessageListener;
import utils.MqConnector;

import javax.jms.*;
import java.util.Enumeration;

import static utils.MqConnector.*;

public class ActiveMqPushConsumer {
    private static final Logger log = LoggerFactory.getLogger(ActiveMqPushConsumer.class);
    private static Session session;
    private static Queue queue;

    public static void receiveMessageFromQueueIfQueueIsNotEmpty() {
        prepareQueue();
        try {
            if (isQueueEmpty() == false) {
                MessageConsumer consumer = getMessageConsumer();
                consumer.setMessageListener(new BooksQueueMessageListener());
                consumer.receive();
            } else {
                log.info("Message queue is empty");
            }
            cleanUpQueue();
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