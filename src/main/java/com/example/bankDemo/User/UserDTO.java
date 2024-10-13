package com.example.bankDemo.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Schema(name = "user's firstName")
    private String firstName;
    @Schema(name = "user's lastName")
    private String lastName;
    @Schema(description = "user's gender")
    private String gender;
    @Schema(description = "user's current address")
    private String address;
    @Schema(description = "user's email")
    private String email;
    @Schema(description = "user's phoneNumber")
    private String phoneNumber;
    @Schema(description = "user's national ID number")
    private String nationalId;
    private String msg;
    private String comment;

}
