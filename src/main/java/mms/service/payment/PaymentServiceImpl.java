package mms.service.payment;

import jakarta.persistence.EntityNotFoundException;
import mms.clients.OrderServiceApiClient;
import mms.controller.order.OrderAction;
import mms.dao.payment.PaymentGatewayName;
import mms.dao.payment.PaymentTransaction;
import mms.dao.payment.PaymentTransactionRepository;
import mms.dto.order.OrderProcessRequest;
import mms.dto.payment.*;
import mms.dto.payment.gateway.*;
import mms.exception.InvalidActionException;
import mms.exception.InvalidPaymentGatewayException;
import mms.pubsub.message.PaymentCallbackMessage;
import mms.pubsub.queue.CallbackS2SPaymentPublisherPymntSvc;
import mms.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private CallbackS2SPaymentPublisherPymntSvc pymntSvcCallbackPublisher;
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private OrderServiceApiClient orderServiceApiClient;

    @Override
    public PaymentInitiateResponse initiate(PaymentInitiateRequest request) throws Exception {
        PaymentGateway paymentGateway = getPaymentGateway(request.getPayementGtwId());
        InitiateResponse response = paymentGateway.initiate(new InitiateRequest(request.getPayementGtwId(),
                request.getPayementOptionId()));
        return new PaymentInitiateResponse();
    }


    @Override
    public PaytmCallbackResponse accpetCallback(PaymentCallbackMessage paymentCallbackMessage) throws Exception {
        pymntSvcCallbackPublisher.publish(paymentCallbackMessage.getGtwName(), (PaymentCallbackMessage) paymentCallbackMessage.getCallback());
        return new PaytmCallbackResponse();
    }

    @Override
    public PaymentStatus orderPaymentStatus(String transactionId) throws Exception {
        Optional<PaymentTransaction> paymentTransaction = paymentTransactionRepository.findByTransactionId(transactionId);
        if (paymentTransaction.isEmpty())
            throw new EntityNotFoundException();
        PaymentTransaction pt = paymentTransaction.get();
        return new PaymentStatus(pt.getTransactionId(), pt.getStatus(), pt.getReason(), pt.getRetry(),
                pt.getCreated(), pt.getUpdated());
    }

    @Override
    public PaymentProcessResponse process(PaymentProcessRequest request) throws Exception {
        PaymentGateway paymentGateway = getPaymentGateway(request.getPayementGtwId());
        PaymentProcessResponse paymentProcessResponse = null;
        switch (request.getNextAction()) {
            case debited:
                VerifyResponse verifyResponse = paymentGateway.verify(new VerifyRequest());
                paymentProcessResponse = PaymentProcessResponse.copy(verifyResponse);
                notifyOrderService(paymentProcessResponse);
                return paymentProcessResponse;
            case wallet:
                DebitResponse debitResponse = paymentGateway.debit(new DebitRequest());
                paymentProcessResponse = PaymentProcessResponse.copy(debitResponse);
                notifyOrderService(paymentProcessResponse);
                return paymentProcessResponse;
            default:
                throw new InvalidActionException(String.format("invalid action %s", request.getNextAction()));
        }
    }


    private PaymentGateway getPaymentGateway(Integer gatewayId) throws InvalidPaymentGatewayException {
        PaymentGateway paymentGateway = PaymentGatewayFactory.getGateway(PaymentGatewayName.getEnum(gatewayId));
        return paymentGateway;
    }

    protected void notifyOrderService(PaymentProcessResponse response) {
        orderServiceApiClient.orderProcess(new OrderProcessRequest(null, OrderAction.paymentConfirmation, null, null,
                null, Status.success, null));
    }

}
