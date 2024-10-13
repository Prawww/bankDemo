package com.example.bankDemo.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EmailDetails {

    private String recipient;

    private String messageBody;

    private String subject;

    private String attachment;
}
