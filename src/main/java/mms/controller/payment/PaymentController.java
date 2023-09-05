package mms.controller.payment;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mms.dto.payment.*;
import mms.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-service/v1/payments")
@Slf4j
@CrossOrigin
@Tag(name = "payment-service")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "initiate payment with gateway")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Payment initiated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Error occurred while processing")})
    @PostMapping(value = "/initiate", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PaymentInitiateResponse> initiate(@Valid @RequestBody PaymentInitiateRequest request) throws Exception {
        log.info("payment initiate request body={}", request);
        PaymentInitiateResponse response = paymentService.initiate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "process payment ")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Payment processing successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Error occurred while processing")})
    @PostMapping(value = "/process/", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @CircuitBreaker(name = "paymentProcess", fallbackMethod = "processFallback")
    public ResponseEntity<PaymentProcessResponse> process(@Valid @RequestBody PaymentProcessRequest request) throws Exception {
        log.info("payment process request body={}", request);
        PaymentProcessResponse response = paymentService.process(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<PaymentProcessResponse> processFallback(Exception ex) {
        return new ResponseEntity<>(new PaymentProcessResponse(null, null, null, "Try after sometimes"),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Operation(summary = "check payment status by order-payment-id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Get status successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Error occurred while processing")})
    @PostMapping(value = "/{transaction-id}/status", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PaymentStatus> status(@PathVariable("transaction-id") String transactionId) throws Exception {
        log.info("payment status of transactionId={}", transactionId);
        PaymentStatus response = paymentService.orderPaymentStatus(transactionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
