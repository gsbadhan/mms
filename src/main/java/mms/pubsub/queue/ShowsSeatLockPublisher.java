package mms.pubsub.queue;

import mms.pubsub.message.SeatLockMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShowsSeatLockPublisher implements Publisher<String, SeatLockMessage> {
    @Value("${shows.seats.publisher.topic}")
    private String topic;
    @Override
    public void publish(String key, SeatLockMessage message) {
    // publish message to MQ i.e. shows.seats.publisher.topic
    }
}
