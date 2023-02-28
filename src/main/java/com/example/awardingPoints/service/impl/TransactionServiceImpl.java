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
        //Finding All transactions by current customer
        List<Transaction> allByCustomerId =
                transactionRepository.findAllByCustomerIdAndTransactionDateAfter
                        (customerId, LocalDate.now().minusMonths(3));
        //pointDtoList containing the total amount of points
        List<PointDto> pointDtoList = new ArrayList<>();

        //Adding all the transactions to a map based on the months of the transaction
        Map<String, List<Transaction>> transactionMap = fillTransactionMap(allByCustomerId);

        //Call fill point map method, and return the point Map
        Map<String, Integer> pointMap = fillPointMap(transactionMap);
        //Iterate through point map
        for (String month : pointMap.keySet()) {
            //Create a pointDTO and set the feilds , add it to the pointDtoList
            PointDto pointDto = new PointDto();
            pointDto.setMonth(month);
            pointDto.setPoints(pointMap.get(month));
            pointDtoList.add(pointDto);
        }
        //Create a new trasaction Response and set the list to it's feild
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setCustomerId(customerId);
        transactionResponse.setPointDtoList(pointDtoList);
        return transactionResponse;
    }

    public Map<String, List<Transaction>> fillTransactionMap(List<Transaction> transactionList) {

        Map<String, List<Transaction>> transactionMap = new HashMap<>();

        for (Transaction transaction : transactionList) {
            List<Transaction> list = transactionMap.get(transaction.getTransactionDate().getMonth().toString());
            //Adding key of the month, and a list of transactions
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

// Map<String, List<Transaction>> transactionMap = fillTransactionMap(allByCustomerId);
    // We passed this map into this method
    public Map<String, Integer> fillPointMap(Map<String, List<Transaction>> map) {
        Map<String, Integer> pointMap = new HashMap<>();
        //Iterating through the map
        for (String each : map.keySet()) {
            //Iterating through the transactions of each keySet to calculate the points
            for (Transaction transaction : map.get(each)) {
                int point = RewardsCalculator.calculatePoints(transaction.getAmount());
                //getting the current iteration from the pointMap
                Integer pointSum = pointMap.get(each);
                //If it is NOT null, it has already points added so you need to add points to pointSum
                if (pointSum != null) {
                    pointSum += point;
                    pointMap.put(each, pointSum);
                } else {
                    //If it's null you can add the points directly.
                    pointMap.put(each, point);
                }
            }
        }
        return pointMap;
    }


}


