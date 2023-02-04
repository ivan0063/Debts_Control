package com.jimm0063.magi.api.control.deudas.controller;

import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.request.SavingsUpdateRequestModel;
import com.jimm0063.magi.api.control.deudas.models.response.ApiResponse;
import com.jimm0063.magi.api.control.deudas.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/validate")
    public ApiResponse updateSavings(@RequestParam String email) throws EntityNotFound {
        return ApiResponse.builder()
                .responseMessage("User is valid!!")
                .responseObject(userService.validateUser(email))
                .build();
    }

    @PostMapping("/update/savings")
    public ApiResponse updateSavings(@RequestBody SavingsUpdateRequestModel savingsUpdateRequestModel) throws EntityNotFound {
        return userService.updateUserSavings(savingsUpdateRequestModel);
    }

    @GetMapping("/financial/status")
    public ApiResponse getUserFinancialStatus(@RequestParam String email) throws EntityNotFound {
        return ApiResponse.builder()
                .responseObject(userService.financialStatus(email))
                .responseMessage("User Financial Status")
                .build();
    }

    @GetMapping("/cards")
    public ApiResponse getCardsByUser(@RequestParam String email) {
        return ApiResponse.builder()
                .responseObject(userService.getCardsByUser(email))
                .responseMessage("Cards by user")
                .build();
    }

    @GetMapping("/savings/updated/values")
    public ApiResponse getUpdatedSavingValues(@RequestParam String email) {
        return ApiResponse.builder()
                .responseObject(userService.getSavingsUpdatedByUser(email))
                .responseMessage("Cards by user")
                .build();
    }
}
