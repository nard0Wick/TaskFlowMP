package com.example.taskFlow.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ArgumentNotValidException {
    String message;
    List<String> causes;
}
