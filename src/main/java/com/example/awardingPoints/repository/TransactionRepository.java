package com.example.awardingPoints.repository;

import com.example.awardingPoints.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    List<Transaction> findAllByCustomerIdAndTransactionDateAfter(Long customerId, LocalDate transactionDate);
}
