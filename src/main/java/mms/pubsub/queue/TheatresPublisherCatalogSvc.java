package mms.pubsub.queue;

import mms.pubsub.message.TheatreMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TheatresPublisherCatalogSvc implements Publisher<String, TheatreMessage> {
    @Value("${theatres.publisher.topic}")
    private String topic;
    @Override
    public void publish(String key, TheatreMessage message) {
        // publish message to MQ i.e. theatres.publisher.topic
    }
}
