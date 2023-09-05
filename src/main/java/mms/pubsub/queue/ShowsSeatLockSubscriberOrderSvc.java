package mms.pubsub.queue;

import mms.dao.order.SeatAllocationStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShowsSeatLockSubscriberOrderSvc implements Subscriber {
    @Value("${shows.seats.subscriber.topic}")
    private String topic;
    @Autowired
    private SeatAllocationStatusRepository seatAllocationStatusRepository;

    @Override
    public void subscribe() {
        //1. consume events from topic i.e. shows.seats.subscriber.topic
        //2. update seats_allocation_status table
        try {
            //input: SeatLockMessage.java
            seatAllocationStatusRepository.save(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
