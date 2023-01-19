package com.jimm0063.magi.api.control.deudas.controller;

import com.jimm0063.magi.api.control.deudas.models.response.ApiResponse;
import com.jimm0063.magi.api.control.deudas.service.PaymentService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/made")
    public ResponseEntity<ApiResponse> getPaymentsMadeByUser(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.builder()
                        .responseMessage("Payment Made by User")
                        .responseObject(paymentService.getPaymentsMadeByUser(email))
                        .build());
    }

}
