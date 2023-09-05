package mms.service.payment;

import mms.dto.payment.*;
import mms.pubsub.message.PaymentCallbackMessage;

public interface PaymentService {
    PaymentInitiateResponse initiate(PaymentInitiateRequest request) throws Exception;

    PaytmCallbackResponse accpetCallback(PaymentCallbackMessage paymentCallbackMessage) throws Exception;

    PaymentStatus orderPaymentStatus(String transactionId) throws Exception;

    PaymentProcessResponse process(PaymentProcessRequest request) throws Exception;
}
