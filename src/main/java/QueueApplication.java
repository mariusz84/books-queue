import consumer.ActiveMqPullConsumer;
import consumer.ActiveMqPushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.QueueModeChecker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class QueueApplication {
    private static Logger LOGGER = LoggerFactory.getLogger(QueueApplication.class);

    public static void main(final String[] args) {

        if (QueueModeChecker.isPush()) {
            ActiveMqPushConsumer.receiveMessageFromQueueIfQueueIsNotEmpty();
        } else {
            ActiveMqPullConsumer.receiveMessageFromQueueIfQueueIsNotEmpty();
        }
        LOGGER.info("" + QueueModeChecker.isPush());

        AgentLoader.run();

        //Below code is only for agent verification:
        try {
            Field field = BooksReader.class.getDeclaredField("bookId");
            LOGGER.info(field.getName());
        } catch (NoSuchFieldException e) {
            LOGGER.info(e.getMessage());
        }

        try {
            BooksReader booksReader = new BooksReader("bookTitle");
            Method method = BooksReader.class.getMethod("printLanguage", new Class[]{});
            method.invoke(booksReader, new Object[]{});
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.info(e.getMessage());
        }
    }
}