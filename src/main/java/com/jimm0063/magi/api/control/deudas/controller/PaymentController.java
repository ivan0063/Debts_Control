package com.jimm0063.magi.api.control.deudas.controller;

import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.response.ApiResponse;
import com.jimm0063.magi.api.control.deudas.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/made/card/{card_nickname}")
    public ResponseEntity<ApiResponse> getPaymentsMadeByUserAndCard(@PathVariable("card_nickname") String nickname,
                                                                    @RequestParam String email) throws EntityNotFound {
        return ResponseEntity.ok(ApiResponse.builder()
                .responseMessage("Payment Made by User")
                .responseObject(paymentService.getPaymentsMadeByUser(nickname, email))
                .build());
    }

}
