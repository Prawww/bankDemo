package com.example.bankDemo.User;

import com.example.bankDemo.Util.EntityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    private PagedResourcesAssembler<User> pagedResourcesAssembler;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addUser")
    @Operation(summary="creates a new user", tags={"user management"})
    @ApiResponse(description = "User created successfully", responseCode = "200")
    @ApiResponse(description = "Error encountered", responseCode = "400")
    public EntityResponse<?> addUser(@RequestBody UserDTO user){
        return userService.addUser(user);
    }
    @PutMapping("/updateUser")
    @Operation(summary="updates the details of an existing user", tags={"user management"})
    @ApiResponse(description = "User updated successfully", responseCode = "200")
    @ApiResponse(description = "Error encountered", responseCode = "400")
    public EntityResponse<?> updateUser(@RequestBody UserDTO user){
        return userService.updateUser(user);
    }

    @GetMapping("/getAccount")
    @Operation(summary="gets the details of an existing user", tags={"user management"})
    @ApiResponse(description = "Details provided", responseCode = "200")
    @ApiResponse(description = "Error encountered", responseCode = "400")
    public EntityResponse<?> getAccount(@RequestParam String account){
        return userService.getAccount(account);
    }

    @GetMapping("/getName")
    @Operation(summary="gets the details of an existing user", tags={"user management"})
    @ApiResponse(description = "User exists", responseCode = "200")
    @ApiResponse(description = "Error encountered", responseCode = "400")
    public EntityResponse<?> getName(@RequestParam String account){
        return userService.getName(account);
    }

    @PutMapping("/credit")
    @Operation(summary="credits the user's account", tags={"transactions"})
    @ApiResponse(description = "Credit Operation Successful", responseCode = "200")
    @ApiResponse(description = "Error encountered", responseCode = "400")
    public EntityResponse<?> credit(@RequestBody BankRequest request){
        return userService.creditAccount(request);
    }
    @PutMapping("/debit")
    @Operation(summary="debits the user's account", tags={"transactions"})
    @ApiResponse(description = "Debit Operation Successful", responseCode = "200")
    @ApiResponse(description = "Error encountered", responseCode = "400")
    public EntityResponse<?> debit(@RequestBody BankRequest request){
        return userService.debitAccount(request);
    }

    @PutMapping("/transfer")
    @Operation(summary="transfer between two accounts", tags={"transactions"})
    @ApiResponse(description = "Transfer Operation successful", responseCode = "200")
    @ApiResponse(description = "Error encountered", responseCode = "400")
    public EntityResponse<?> transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }

    @GetMapping("/userPagination")
    @Operation(summary = "Arranges the user in pages", tags={"user management"})
    public PagedModel<EntityModel<User>> userPagination(@RequestParam String email, @RequestParam int page, @RequestParam int size){
        Page<User> users = userService.userPagination(email, page, size);

        return pagedResourcesAssembler.toModel(users,
                WebMvcLinkBuilder.linkTo(UserController.class).slash("users").withSelfRel());
    }

    @GetMapping("/sortingBy")
    @Operation(summary="Sorting the users by a given parameter", tags={"user management"})
    public EntityResponse<?> sortingBy(@RequestParam String sortBy, @RequestParam boolean ascending){
        return userService.sortingBy(sortBy, ascending);
    }

    @PostMapping("/createUser")
    public EntityResponse<?> newUser(@RequestParam User user){
        return userService.newUser(user);
    }
}
