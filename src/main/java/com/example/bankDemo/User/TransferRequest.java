package com.example.bankDemo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransferRequest {
    private String senderAccount;

    private String receiverAccount;

    private double amount;
}
