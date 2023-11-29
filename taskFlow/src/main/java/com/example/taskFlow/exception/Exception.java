package com.example.taskFlow.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Exception {
    private String message;
    private String cause;
}
