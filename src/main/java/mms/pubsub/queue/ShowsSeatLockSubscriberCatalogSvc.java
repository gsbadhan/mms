package mms.pubsub.queue;

import mms.service.catalog.ShowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShowsSeatLockSubscriberCatalogSvc implements Subscriber {
    @Value("${shows.seats.subscriber.topic}")
    private String topic;
    @Autowired
    private ShowsService showsService;

    @Override
    public void subscribe() {
        //1. consume events from topic i.e. shows.seats.subscriber.topic
        //2. update movie_shows table
        try {
            //input: SeatLockMessage.java
            //showsService.updateShow(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
