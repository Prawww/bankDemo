package com.example.bankDemo.Util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EntityResponse <T>{
    private String message;
    private int code;
    private T entity;
}
