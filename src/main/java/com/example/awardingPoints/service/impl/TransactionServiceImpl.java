package com.example.awardingPoints.service.impl;

import com.example.awardingPoints.dto.PointDto;
import com.example.awardingPoints.dto.TransactionResponse;
import com.example.awardingPoints.entity.Transaction;
import com.example.awardingPoints.repository.TransactionRepository;
import com.example.awardingPoints.service.TransactionService;
import com.example.awardingPoints.util.RewardsCalculator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {


    private TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public TransactionResponse calculatePointsByCustomer(Long customerId) {
        List<Transaction> allByCustomerId = transactionRepository.findAllByCustomerIdAndTransactionDateAfter(customerId, LocalDate.now().minusMonths(3));
        List<PointDto> pointDtoList = new ArrayList<>();

        Map<String, List<Transaction>> transactionMap = fillTransactionMap(allByCustomerId);
        Map<String, Integer> pointMap = fillPointMap(transactionMap);
        for (String month : pointMap.keySet()) {
            PointDto pointDto = new PointDto();
            pointDto.setMonth(month);
            pointDto.setPoints(pointMap.get(month));
            pointDtoList.add(pointDto);
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setCustomerId(customerId);
        transactionResponse.setPointDtoList(pointDtoList);
        return transactionResponse;
    }

    public Map<String, List<Transaction>> fillTransactionMap(List<Transaction> transactionList) {

        Map<String, List<Transaction>> transactionMap = new HashMap<>();

        for (Transaction transaction : transactionList) {
            List<Transaction> list = transactionMap.get(transaction.getTransactionDate().getMonth().toString());
            if (list!=null) {
                list.add(transaction);
                transactionMap.put(transaction.getTransactionDate().getMonth().toString(), list);
            } else {
                list = new ArrayList<>();
                list.add(transaction);
                transactionMap.put(transaction.getTransactionDate().getMonth().toString(), list);
            }
        }
        return transactionMap;
    }


    public Map<String, Integer> fillPointMap(Map<String, List<Transaction>> map) {
        Map<String, Integer> pointMap = new HashMap<>();
        for (String each : map.keySet()) {
            for (Transaction transaction : map.get(each)) {
                int point = RewardsCalculator.calculatePoints(transaction.getAmount());
                Integer pointSum = pointMap.get(each);
                if (pointSum != null) {
                    pointSum += point;
                    pointMap.put(each, pointSum);
                } else {
                    pointMap.put(each, point);
                }
            }
        }
        return pointMap;
    }


}


