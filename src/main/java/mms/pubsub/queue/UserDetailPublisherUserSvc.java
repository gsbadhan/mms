package mms.pubsub.queue;

import mms.pubsub.message.UserDetailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserDetailPublisherUserSvc implements Publisher<String, UserDetailMessage> {
    @Value("${contacts.publisher.topic}")
    private String topic;
    @Override
    public void publish(String key, UserDetailMessage message) {
        // publish message to MQ i.e. contacts.publisher.topic
    }
}
