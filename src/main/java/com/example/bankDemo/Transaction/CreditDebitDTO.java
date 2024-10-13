package com.example.bankDemo.Transaction;

import jakarta.persistence.Entity;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDebitDTO {

    private String transactionType;

    private double amount;

    private String senderAccount;
}
