package com.jimm0063.magi.api.control.deudas.controller;

import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.request.DebtReqModel;
import com.jimm0063.magi.api.control.deudas.models.request.MultiDebtReq;
import com.jimm0063.magi.api.control.deudas.models.response.ApiResponse;
import com.jimm0063.magi.api.control.deudas.service.DebtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debt")
public class DebtController {
    private final DebtService debtService;

    public DebtController (DebtService debtService) {
        this.debtService = debtService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addDebt(@RequestBody DebtReqModel debtModel) throws EntityNotFound {
        return ResponseEntity.ok(debtService.addDebt(debtModel));
    }

    @PostMapping("/multi/add")
    public ResponseEntity<ApiResponse> multiAddDebt(@RequestBody MultiDebtReq multiDebtModel) throws EntityNotFound {
        return ResponseEntity.ok(debtService.addMultiDebt(multiDebtModel));
    }

    @GetMapping("/all/user")
    public ResponseEntity<ApiResponse> getAllByUser(@RequestParam String email ) throws EntityNotFound {
        return ResponseEntity.ok(debtService.findAllDebtsByUser(email));
    }

    @GetMapping("/all/card/{card_nickname}")
    public ResponseEntity<ApiResponse> getAllByUserAndCard(
            @RequestParam String email, @PathVariable("card_nickname") String nickname) throws EntityNotFound {
        return ResponseEntity.ok(ApiResponse.builder()
                                    .responseMessage("All Debts By Card")
                                    .responseObject(debtService.findAllByCard(email, nickname))
                                    .build());
    }

    @GetMapping("/finished/all/user")
    public ResponseEntity<ApiResponse> getAllFinishedByUser(@RequestParam String email ) throws EntityNotFound {
        return ResponseEntity.ok(debtService.findAllFinishedDebtsByUser(email));
    }

    @GetMapping("/about/finish")
    public ResponseEntity<ApiResponse> getAllAboutToFinish(@RequestParam String email ) throws EntityNotFound {
        return ResponseEntity.ok(ApiResponse.builder()
                .responseMessage("Debts that end this month")
                .responseObject(debtService.debtsAboutToFinish(email))
                .build()
        );
    }
}
