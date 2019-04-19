package listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class BooksQueueMessageListener implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooksQueueMessageListener.class);

    @Override
    public void onMessage(Message message) {
        String msg;
        try {
            Thread.sleep(10000);
            msg = String.format("Received the following message: [ %s ]",
                    ((TextMessage) message).getText());
        } catch (JMSException | InterruptedException e) {
            msg = String.format("Cannot receive message");
        }
        LOGGER.info(msg);
    }
}