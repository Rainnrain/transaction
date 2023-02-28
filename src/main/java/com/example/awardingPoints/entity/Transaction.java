package com.example.awardingPoints.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Transaction {

    @Id
    private Long id;

    private Long customerId;

    private LocalDate transactionDate;

    private Double amount;

    private Integer pointsEarned;
}
