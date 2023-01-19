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
}
