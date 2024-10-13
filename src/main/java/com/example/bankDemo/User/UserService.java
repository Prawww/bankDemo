package com.example.bankDemo.User;

import com.example.bankDemo.Util.EntityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

public interface UserService{
    EntityResponse<?> addUser(UserDTO user);

    EntityResponse<?> updateUser(UserDTO user);

    EntityResponse<?> getAccount(String account);

    EntityResponse<?> getName(String account);

    EntityResponse<?> newUser(User user);

    EntityResponse<?> creditAccount(BankRequest request);

    EntityResponse<?> debitAccount(BankRequest request);

    EntityResponse<?> transfer(TransferRequest request);

    Page<User> userPagination(String email, int page, int size);

    EntityResponse<?> sortingBy(String sortBy, boolean ascending);
}
