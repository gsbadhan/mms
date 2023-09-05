package mms.controller.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mms.dto.payment.PaytmCallbackRequest;
import mms.dto.payment.PaytmCallbackResponse;
import mms.pubsub.message.PaymentCallbackMessage;
import mms.service.payment.GatewaySource;
import mms.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-service/v1/payments/callback")
@Slf4j
@CrossOrigin
@Tag(name = "payment-service")
public class PaymentCallbackController {
    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "payment status callback from paytm")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Accepted the request"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Error occurred while processing")
    })
    @PostMapping(value = "/paytm", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PaytmCallbackResponse> paytm(@Valid @RequestBody PaytmCallbackRequest request) throws Exception {
        log.info("paytm callback request body={}", request);
        PaytmCallbackResponse response = paymentService.accpetCallback(new PaymentCallbackMessage(GatewaySource.paytm.name(), request));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
