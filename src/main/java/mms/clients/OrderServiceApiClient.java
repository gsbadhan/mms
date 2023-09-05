package mms.clients;

import jakarta.validation.Valid;
import mms.dto.order.OrderProcessRequest;
import mms.dto.order.OrderProcessResponse;
import mms.dto.payment.PaymentInitiateRequest;
import mms.dto.payment.PaymentInitiateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "orderSvcClient", path = "/order-svc/v1/orders", url = "http://localhost:9090")
public interface OrderServiceApiClient {

    @PostMapping("/process")
    OrderProcessResponse orderProcess(@Valid @RequestBody OrderProcessRequest request);
}
