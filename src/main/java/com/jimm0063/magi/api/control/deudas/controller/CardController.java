package com.jimm0063.magi.api.control.deudas.controller;

import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.response.ApiResponse;
import com.jimm0063.magi.api.control.deudas.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/doPayment/{cardNickname}")
    public ResponseEntity<ApiResponse> doPayment(@PathVariable String cardNickname,
                                                 @RequestParam String email) throws EntityNotFound {
        return ResponseEntity.ok(
          ApiResponse.builder()
                  .responseMessage("The Card payment has been applied")
                  .responseObject(cardService.doCardPayment(cardNickname, email))
                  .build()
        );
    }

    @GetMapping("/undone/payment/{cardNickname}")
    public ResponseEntity<ApiResponse> undonePayment(@PathVariable String cardNickname,
                                                 @RequestParam String email) throws EntityNotFound {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .responseMessage("The Card payment has been applied")
                        .responseObject(cardService.doCardPayment(cardNickname, email))
                        .build()
        );
    }

    @GetMapping("/next/payments")
    public ResponseEntity<ApiResponse> findNextPayments(@RequestParam String email) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .responseMessage("Next Paments to be paid")
                        .responseObject(cardService.findNextPaymentByUser(email))
                        .build()
        );
    }
}
