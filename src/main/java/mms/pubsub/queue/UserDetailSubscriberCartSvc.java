package mms.pubsub.queue;

import mms.dao.cart.CartBookingCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserDetailSubscriberCartSvc implements Subscriber {
    @Value("${contacts.subscriber.topic}")
    private String topic;
    @Autowired
    CartBookingCacheRepository cartBookingCacheRepository;

    @Override
    public void subscribe() {
        //1. consume events from topic i.e. contacts.subscriber.topic
        //2. Get cart-id from UserDetailMessage.java
        //3. update cart detail cache using CartBookingCacheRepository.java
    }
}
