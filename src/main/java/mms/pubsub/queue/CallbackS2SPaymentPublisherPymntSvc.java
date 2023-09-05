package mms.pubsub.queue;

import mms.pubsub.message.PaymentCallbackMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CallbackS2SPaymentPublisherPymntSvc implements Publisher<String, PaymentCallbackMessage> {
    @Value("${callbacks.s2s.payment.publisher.topic}")
    private String topic;

    @Override
    public void publish(String key, PaymentCallbackMessage message) {
        // take message from payment-gateway and publish same message to MQ i.e. callbacks.s2s.payment.publisher.topic
    }
}
