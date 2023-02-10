package com.jimm0063.magi.api.control.deudas.controller;

import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.response.ApiResponse;
import com.jimm0063.magi.api.control.deudas.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expsense")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/streaming/all")
    public ApiResponse getAllStreamingExpensesByUsr(@RequestParam String email) throws EntityNotFound {
        return expenseService.getAllStreamingExpensesByUsr(email);
    }

    @GetMapping("/next")
    public ResponseEntity<ApiResponse> getNextFixedExpensesByUser(@RequestParam String email) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .responseMessage("Next Payments")
                        .responseObject(expenseService.getNextFixedExpensesByUser(email))
                        .build()
        );
    }
}
