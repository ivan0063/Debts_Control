package com.jimm0063.magi.api.control.deudas.controller;

import com.jimm0063.magi.api.control.deudas.models.request.BankProjectionReq;
import com.jimm0063.magi.api.control.deudas.models.request.CardProjectionReq;
import com.jimm0063.magi.api.control.deudas.models.request.ProjectionRequest;
import com.jimm0063.magi.api.control.deudas.models.response.ApiResponse;
import com.jimm0063.magi.api.control.deudas.models.response.ProjectionResponse;
import com.jimm0063.magi.api.control.deudas.service.ProjectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projection")
public class ProjectionController {
    private final ProjectionService projectionService;

    public ProjectionController(ProjectionService projectionService) {
        this.projectionService = projectionService;
    }

    @PostMapping("/bank")
    public ResponseEntity<ApiResponse> projectionByBank(@RequestBody BankProjectionReq bankProjectionReq) {
        ProjectionResponse projectionByBank = projectionService.bankProjection(bankProjectionReq.getEmail(), bankProjectionReq.getBank(), bankProjectionReq.getProjectionUntil());

        return ResponseEntity.ok(ApiResponse.builder()
                .responseMessage("Debt Projection")
                .responseObject(projectionByBank)
                .build()
        );
    }


    @PostMapping("/all/debts")
    public ResponseEntity<ApiResponse> projectionByAllDebts(@RequestBody ProjectionRequest projectionRequest) {
        ProjectionResponse projectionByBank = projectionService.allDebtProjectionByDate(projectionRequest.getEmail(), projectionRequest.getProjectionUntil());

        return ResponseEntity.ok(ApiResponse.builder()
                .responseMessage("All Debt Projection")
                .responseObject(projectionByBank)
                .build()
        );
    }

    @PostMapping("/card")
    public ResponseEntity<ApiResponse> projectionByAllCard(@RequestBody CardProjectionReq projectionRequest) {
        ProjectionResponse projectionByBank = projectionService
                .cardProjection(projectionRequest.getEmail(), projectionRequest.getCard(), projectionRequest.getProjectionUntil());

        return ResponseEntity.ok(ApiResponse.builder()
                .responseMessage("Projection by Card")
                .responseObject(projectionByBank)
                .build()
        );
    }

    @PostMapping("/savings")
    public ResponseEntity<ApiResponse> getFinancialProjection(@RequestBody ProjectionRequest projectionRequest) {

        return ResponseEntity.ok(ApiResponse.builder()
                .responseMessage("Savings projection")
                .responseObject(projectionService.financialProjection(projectionRequest))
                .build()
        );
    }
}
