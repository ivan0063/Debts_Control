package com.jimm0063.magi.api.control.deudas.controller;

import com.jimm0063.magi.api.control.deudas.models.response.ApiResponse;
import com.jimm0063.magi.api.control.deudas.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
    private CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/fixedExpenses")
    public ResponseEntity<ApiResponse> getAllFixedExpenses(@RequestParam String email) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .responseMessage("All Fixed Expenses by user")
                        .responseObject(catalogService.getAllFixedExpenses(email))
                        .build()
        );
    }
}
