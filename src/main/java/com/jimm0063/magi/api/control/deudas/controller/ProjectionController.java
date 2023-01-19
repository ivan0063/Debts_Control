package com.jimm0063.magi.api.control.deudas.controller;

import com.jimm0063.magi.api.control.deudas.models.request.BankProjectionReq;
import com.jimm0063.magi.api.control.deudas.models.request.ProjectionRequest;
import com.jimm0063.magi.api.control.deudas.models.response.ApiResponse;
import com.jimm0063.magi.api.control.deudas.models.response.ProjectionResponse;
import com.jimm0063.magi.api.control.deudas.service.ProjectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
