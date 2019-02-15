import consumer.ActiveMqPullConsumer;
import consumer.ActiveMqPushConsumer;
import utils.QueueModeChecker;

public class QueueApplication {

    public static void main(final String[] args) {

        if (QueueModeChecker.isPush()) {
            ActiveMqPushConsumer.receiveMessageFromQueueIfQueueIsNotEmpty();
        } else {
            ActiveMqPullConsumer.receiveMessageFromQueueIfQueueIsNotEmpty();
        }
    }
}