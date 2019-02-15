package utils;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import consumer.ActiveMqPullConsumer;

import javax.jms.*;

public class MqConnector extends ActiveMqPullConsumer {
    private static final Logger log = LoggerFactory.getLogger(MqConnector.class);
    private static final String M2_BROKER = "localhost:61616";
    private static final String BOOKS_QUEUE = "booksQueue";
    private static Connection connection;
    private static Session session;
    private static Queue queue;

    public static MessageConsumer getMessageConsumer() {
        MessageConsumer consumer = null;
        try {
            consumer = session.createConsumer(queue);
        } catch (JMSException e) {
            log.info(e.getMessage());
        }
        return consumer;
    }

    public static Session getSession() {
        createConnection();
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            log.info(e.getMessage());
        }
        return session;
    }

    public static Queue setupQueue() {
        session = getSession();
        try {
            queue = session.createQueue(BOOKS_QUEUE);
        } catch (JMSException e) {
            log.info(e.getMessage());
        }
        return queue;
    }

    public static void cleanUpQueue() {
        try {
            session.close();
            connection.close();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private static void createConnection() {
        connection = null;
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    "tcp://" + M2_BROKER);
            connection = connectionFactory.createConnection();
            connection.start();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}