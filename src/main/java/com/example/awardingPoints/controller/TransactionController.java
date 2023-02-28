package com.example.awardingPoints.controller;

import com.example.awardingPoints.dto.TransactionResponse;
import com.example.awardingPoints.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transaction", description = "Transaction Operations")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @Operation(summary = "Get rewards of the user")
    @ApiResponse(responseCode = "200", description = "Returning the rewards of a user",
            content = {@Content(mediaType = "application/json")})
    public ResponseEntity<TransactionResponse> getPoints(@RequestParam("customerId") Long customerId) {
        return ResponseEntity.ok(transactionService.calculatePointsByCustomer(customerId));
    }

}
