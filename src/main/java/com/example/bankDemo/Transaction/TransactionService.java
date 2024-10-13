package com.example.bankDemo.Transaction;

import com.example.bankDemo.Util.EntityResponse;

public interface TransactionService {

    EntityResponse<?> addTransaction(CreditDebitDTO transaction);
}
