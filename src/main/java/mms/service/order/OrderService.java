package mms.service.order;

import mms.dto.order.OrderCreateRequest;
import mms.dto.order.OrderCreateResponse;
import mms.dto.order.OrderProcessRequest;
import mms.dto.order.OrderProcessResponse;

public interface OrderService {
    OrderCreateResponse createOrder(OrderCreateRequest request) throws Exception;

    OrderProcessResponse processOrder(OrderProcessRequest request) throws Exception;

}
