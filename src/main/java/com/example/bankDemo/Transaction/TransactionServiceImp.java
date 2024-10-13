package com.example.bankDemo.Transaction;

import com.example.bankDemo.Util.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImp implements TransactionService{

    private final TransactionRepo transactionRepo;

    @Autowired
    public TransactionServiceImp(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public EntityResponse<?> addTransaction(CreditDebitDTO transaction){
        try {
            Transaction trans = Transaction.builder()
                    .transactionType(transaction.getTransactionType())
                    .senderAccount(transaction.getSenderAccount())
                    .amount(transaction.getAmount())
                    .build();
            Transaction saved = transactionRepo.save(trans);
            return EntityResponse.builder()
                    .message("Transaction added successful")
                    .entity(saved)
                    .code(HttpStatus.OK.value())
                    .build();
        }
        catch (Exception e){
          return  EntityResponse.builder()
                    .message("Error encountered.")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .entity(null)
                    .build();
        }
    }
}
