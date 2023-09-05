package mms.pubsub.queue;

import mms.dao.search.Theatre;
import mms.dao.search.TheatreSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TheatreSubscriberSearchSvc implements Subscriber {
    @Value("${theatres.subscriber.topic}")
    private String topic;
    @Autowired
    private TheatreSearchRepository theatreSearchRepository;

    @Override
    public void subscribe() {
        //1. consume Theatre related events from topic i.e. theatres.subscriber.topic
        //2. update Theatre index
        theatreSearchRepository.save(new Theatre());
    }
}
