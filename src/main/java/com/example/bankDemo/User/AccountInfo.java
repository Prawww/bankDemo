package com.example.bankDemo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountInfo {

    private String fullName;
    private String status;
    private String accountNumber;
    private double balance;
}
