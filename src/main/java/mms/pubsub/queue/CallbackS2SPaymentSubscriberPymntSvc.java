package mms.pubsub.queue;

import mms.dto.payment.PaymentAction;
import mms.dto.payment.PaymentProcessRequest;
import mms.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CallbackS2SPaymentSubscriberPymntSvc implements Subscriber {
    @Value("${callbacks.s2s.payment.subscriber.topic}")
    private String topic;
    @Autowired
    private PaymentService paymentService;

    @Override
    public void subscribe() {
        //1. consume events from topic i.e. callbacks.s2s.payment.subscriber.topic
        //2. update gw_transaction table i.e. PaymentTransactionRepository.java
        try {
            paymentService.process(new PaymentProcessRequest(null, null, null, PaymentAction.debited, 0.0, null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
