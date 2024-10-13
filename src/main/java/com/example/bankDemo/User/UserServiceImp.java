package com.example.bankDemo.User;

import com.example.bankDemo.Email.EmailDetails;
import com.example.bankDemo.Email.EmailService;
import com.example.bankDemo.Transaction.CreditDebitDTO;
import com.example.bankDemo.Transaction.TransactionService;
import com.example.bankDemo.Util.EntityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImp implements UserService {

    private final UserRepo userRepo;
    private final EmailService emailService;

    private final TransactionService transactionService;

    private final PostRepo postRepo;

    @Autowired
    public UserServiceImp(UserRepo userRepo, EmailService emailService, TransactionService transactionService, PostRepo postRepo) {
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.transactionService = transactionService;
        this.postRepo = postRepo;
    }

    public EntityResponse<?> addUser(UserDTO user) {
        try {
            String account = generateAccount();
            Post post = Post.builder()
                    .comment(user.getComment())
                    .message(user.getMsg())
                    .build();
            User newUser = User.builder()
                    .address(user.getAddress())
                    .email(user.getEmail())
                    .balance(0.00)
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .nationalId(user.getNationalId())
                    .phoneNumber(user.getPhoneNumber())
                    .gender(user.getGender())
                    .accountNumber(account)
                    .status("ACTIVE")
                    .post(post)
                    .build();

            post.setUser(newUser);
            User savedUser = userRepo.save(newUser);


            EmailDetails emailDetails = EmailDetails.builder()
                    .subject("ACCOUNT CREATION")
                    .recipient(savedUser.getEmail())
                    .messageBody("Congratulations, you have successfully created an account.")
                    .build();

            try {
                emailService.sendMailAlert(emailDetails);
                System.out.println("Failed to send email to");
            } catch (MailException e) {
                System.out.println("Failed to send email to");
            }

            AccountInfo accountInfo = AccountInfo.builder()
                    .accountNumber(savedUser.getAccountNumber())
                    .balance(savedUser.getBalance())
                    .fullName(savedUser.getFirstName() + " " + savedUser.getLastName())
                    .status("ACTIVE")
                    .build();

            return EntityResponse.builder()
                    .code(HttpStatus.CREATED.value())
                    .message("Account created successfully")
                    .entity(accountInfo)
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return EntityResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error encountered")
                    .entity(null)
                    .build();
        }
    }

    private String generateAccount() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int LENGTH = 10;
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            randomString.append(CHARACTERS.charAt(index));
        }
        return randomString.toString();
    }

    public EntityResponse<?> updateUser(UserDTO user) {
        try {
            Optional<User> usr = userRepo.findByEmail(user.getEmail());

            if (usr.isPresent()) {
                User existingUser = usr.get();
                existingUser.setFirstName(user.getFirstName());
                existingUser.setLastName(user.getLastName());
                existingUser.setAddress(user.getAddress());
                existingUser.setGender(user.getGender());
                existingUser.setNationalId(user.getNationalId());
                existingUser.setPhoneNumber(user.getPhoneNumber());
                existingUser.setEmail(user.getEmail());

                userRepo.save(existingUser);
                return EntityResponse.builder()
                        .entity(existingUser)
                        .message("User updated successfully")
                        .code(HttpStatus.OK.value())
                        .build();
            } else {
                return EntityResponse.builder()
                        .entity(null)
                        .message("User not found")
                        .code(HttpStatus.NOT_FOUND.value())
                        .build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return EntityResponse.builder()
                    .entity(null)
                    .message("Error encountered")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }

    @Override
    public EntityResponse<?> getAccount(String account) {
        try{
            boolean accountExists = userRepo.existsByAccountNumber(account);

            if(!accountExists){
                return EntityResponse.builder()
                        .entity(null)
                        .message("Account does not exist")
                        .code(HttpStatus.NOT_FOUND.value())
                        .build();
            }
           User user = userRepo.findByAccountNumber(account);
            return EntityResponse.builder()
                    .entity(AccountInfo.builder()
                            .accountNumber(user.getAccountNumber())
                            .balance(user.getBalance())
                            .fullName(user.getFirstName() + " " + user.getLastName())
                            .status(user.getStatus())
                            .build())
                    .message("Account " + user.getAccountNumber() + " exists")
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        catch (Exception e){
            return EntityResponse.builder()
                    .entity(null)
                    .message("Error encountered")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }

    @Override
    public EntityResponse<?> getName(String account){
        try{
            boolean accountExists = userRepo.existsByAccountNumber(account);

            if(!accountExists){
                return EntityResponse.builder()
                        .message("Account does not exist")
                        .build();
            }
            User user = userRepo.findByAccountNumber(account);
            return EntityResponse.builder()
                    .entity(AccountInfo.builder()
                            .accountNumber(user.getAccountNumber())
                            .balance(user.getBalance())
                            .fullName(user.getFirstName() + " " + user.getLastName())
                            .status(user.getStatus())
                            .build())
                    .message(user.getLastName() + user.getFirstName())
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        catch (Exception e){
            return EntityResponse.builder()
                    .entity(null)
                    .message("Error encountered")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }

    @Override
    public EntityResponse<?> creditAccount(BankRequest request) {
        EntityResponse<AccountInfo> res = new EntityResponse<>();
        try{
            boolean accountExists = userRepo.existsByAccountNumber(request.getAccountNumber());

            if(!accountExists){
                res.setMessage("Account does not exist");
                res.setEntity(null);
                res.setCode(HttpStatus.NOT_FOUND.value());
            }

            User user = userRepo.findByAccountNumber(request.getAccountNumber());
            user.setBalance(user.getBalance()+ request.getAmount());

            User savedUser = userRepo.save(user);

            CreditDebitDTO transaction = CreditDebitDTO.builder()
                    .amount(request.getAmount())
                    .senderAccount(savedUser.getAccountNumber())
                    .transactionType("Credit Transaction")
                    .build();

            transactionService.addTransaction(transaction);
            res.setCode(HttpStatus.OK.value());
            res.setMessage("Credit operation successful");
            res.setEntity(AccountInfo.builder()
                            .balance(savedUser.getBalance())
                            .fullName(savedUser.getFirstName() + " " + savedUser.getLastName())
                            .status(savedUser.getStatus())
                            .accountNumber(savedUser.getAccountNumber())
                    .build());
        }
        catch (Exception e){
            res.setMessage("Error encountered.");
            res.setEntity(null);
            res.setCode(HttpStatus.BAD_REQUEST.value());
        }
        return res;
    }

    @Override
    public EntityResponse<?> debitAccount(BankRequest request) {
        EntityResponse<AccountInfo> res = new EntityResponse<>();
        try{
            boolean accountExists = userRepo.existsByAccountNumber(request.getAccountNumber());

            if(!accountExists){
                res.setMessage("Account does not exist");
                res.setEntity(null);
                res.setCode(HttpStatus.NOT_FOUND.value());
            }

            User user = userRepo.findByAccountNumber(request.getAccountNumber());
            if(user.getBalance() < request.getAmount()){
                System.out.println("Balance is insufficient");
                res.setEntity(null);
                res.setCode(HttpStatus.BAD_REQUEST.value());
                res.setMessage("Account has insufficient balance");
            }
            else{
                user.setBalance(user.getBalance() - request.getAmount());
                User savedUser = userRepo.save(user);

                CreditDebitDTO transaction = CreditDebitDTO.builder()
                        .amount(request.getAmount())
                        .senderAccount(savedUser.getAccountNumber())
                        .transactionType("Debit Transaction")
                        .build();

                transactionService.addTransaction(transaction);
                res.setMessage("Debit successful");
                res.setCode(HttpStatus.OK.value());
                res.setEntity(AccountInfo.builder()
                                .accountNumber(savedUser.getAccountNumber())
                                .balance(savedUser.getBalance())
                                .fullName(savedUser.getFirstName() + " " + savedUser.getLastName())
                                .status(savedUser.getStatus())
                        .build());
            }
        }
        catch(Exception e){
            res.setMessage("Error encountered.");
            res.setEntity(null);
            res.setCode(HttpStatus.BAD_REQUEST.value());
        }
        return res;
    }

 @Override
 public EntityResponse<?> transfer(TransferRequest request){
        try {
            boolean userExists = userRepo.existsByAccountNumber(request.getReceiverAccount());

            if(!userExists){
                EntityResponse.builder()
                        .message("Account does not exist")
                        .code(HttpStatus.NOT_FOUND.value())
                        .entity(null)
                        .build();
            }
            User receiver = userRepo.findByAccountNumber(request.getReceiverAccount());
            User sender = userRepo.findByAccountNumber(request.getSenderAccount());

            if(sender.getBalance() > request.getAmount()) {
                double newSenderAmt = sender.getBalance() - request.getAmount();
                sender.setBalance(newSenderAmt);
                userRepo.save(sender);

                double newReceiverAmt = receiver.getBalance() + request.getAmount();
                receiver.setBalance(newReceiverAmt);
                userRepo.save(receiver);

                return EntityResponse.builder()
                        .message("Transfer successful")
                        .code(HttpStatus.OK.value())
                        .entity(AccountInfo.builder()
                                .accountNumber(sender.getAccountNumber())
                                .fullName(sender.getFirstName() + " " + sender.getLastName())
                                .balance(sender.getBalance())
                                .status("ACTIVE")
                                .build())
                        .build();
            }
            else{
                return EntityResponse.builder()
                        .message("Sender's Account has insufficient balance")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .entity(null)
                        .build();
            }
            }
        catch (Exception e){
            return EntityResponse.builder()
                    .message("Error encountered")
                    .code(HttpStatus.NOT_FOUND.value())
                    .entity(null)
                    .build();
        }
        }

        @Override
        public Page<User> userPagination(String email, int page, int size){
            Pageable pageable = PageRequest.of(page,size);
            return userRepo.findAllByEmail(email, pageable);
        }

    @Override
    public EntityResponse<?> sortingBy(String sortBy, boolean ascending) {
        try {

            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            List<User> users = userRepo.findAll(sort);

            return EntityResponse.builder()
                    .entity(users)
                    .code(HttpStatus.FOUND.value())
                    .message("Users sorted successfully by " + sortBy)
                    .build();

        }
        catch (Exception e){
            return EntityResponse.builder()
                    .entity(null)
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error encountered")
                    .build();
        }
    }
    @Override
    public EntityResponse<?> newUser(User user){


        User savedUser = userRepo.save(user);

        return EntityResponse.builder()
                .message("New user created")
                .code(HttpStatus.CREATED.value())
                .entity(savedUser)
                .build();
    }


}

