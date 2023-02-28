package com.example.awardingPoints.service;

import com.example.awardingPoints.dto.TransactionResponse;
import com.example.awardingPoints.entity.Transaction;

public interface TransactionService {

    TransactionResponse calculatePointsByCustomer(Long customerId);
}
