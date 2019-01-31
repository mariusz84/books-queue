import utils.ActiveMqQueueOperations;

public class QueueApplication {

    public static void main(final String[] args) {
        ActiveMqQueueOperations.receiveMessageFromQueueIfQueueIsNotEmpty();
    }
}