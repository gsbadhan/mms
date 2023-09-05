package mms.service.order;

import mms.clients.PaymentServiceApiClient;
import mms.controller.order.OrderAction;
import mms.dao.order.Order;
import mms.dao.order.OrderDetail;
import mms.dao.order.OrderDetailRepository;
import mms.dao.order.OrderRepository;
import mms.dto.cart.CartUpsertResponse;
import mms.dto.order.OrderCreateRequest;
import mms.dto.order.OrderCreateResponse;
import mms.dto.order.OrderProcessRequest;
import mms.dto.order.OrderProcessResponse;
import mms.dto.payment.PaymentAction;
import mms.dto.payment.PaymentInitiateRequest;
import mms.dto.payment.PaymentInitiateResponse;
import mms.exception.InvalidActionException;
import mms.pubsub.queue.ShowsSeatLockPublisher;
import mms.service.cart.CartService;
import mms.utils.Common;
import mms.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentServiceApiClient paymentServiceApiClient;
    @Autowired
    private ShowsSeatLockPublisher showsSeatLockPublisher;

    @Override
    public OrderCreateResponse createOrder(OrderCreateRequest request) throws Exception {
        CartUpsertResponse cartDetail = cartService.getCart(request.getCartId());
        OrderDetail orderDetail = orderDetailRepository.save(new OrderDetail());
        Order order = orderRepository.save(new Order(null, orderDetail, OrderAction.initiatePayment.ordinal(),
                Common.getTransactionId(), Status.pending.ordinal(), null, null, null));
        return new OrderCreateResponse(order.getId(), order.getOrderPaymentId(), OrderAction.initiatePayment);
    }

    @Override
    public OrderProcessResponse processOrder(OrderProcessRequest request) throws Exception {
        switch (request.getAction()) {
            case initiatePayment:
                PaymentInitiateResponse response = paymentServiceApiClient.initiate(new PaymentInitiateRequest());
                Order order = orderRepository.findById(request.getOrderId()).get();
                order.setStatus(Status.processing.ordinal());
                return new OrderProcessResponse(request.getOrderId(), response.getTransactionId(),
                        response.getPayementGtwId(), response.getNextAction(), response.getMetaData());
            case paymentConfirmation:
                return updateOrderOnPayment(request);
            default:
                throw new InvalidActionException(String.format("invalid action %s", request.getAction()));
        }
    }

    protected OrderProcessResponse updateOrderOnPayment(OrderProcessRequest request) {
        Order order = orderRepository.findByOrderPaymentId(request.getPaymentTransactionId()).get();
        order.setStatus(request.getPaymentStatus().ordinal());
        order.setNextAction(request.getAction().ordinal());
        order.setReason(request.getReason());
        orderRepository.save(order);
        //TODO: publish seats booking status
        showsSeatLockPublisher.publish(null, null);
        return new OrderProcessResponse(order.getId(), request.getPaymentTransactionId(), request.getPaymentGtwId(), PaymentAction.none,
                null);
    }
}
