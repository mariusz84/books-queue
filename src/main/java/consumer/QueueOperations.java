package consumer;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import java.util.Enumeration;

public class QueueOperations {

    protected static boolean isQueueEmpty(Session session, Queue queue) throws JMSException {
        QueueBrowser queueBrowser = session.createBrowser(queue);
        Enumeration enumeration = queueBrowser.getEnumeration();
        return !enumeration.hasMoreElements();
    }
}