package com.example.taskFlow.dto;

import com.example.taskFlow.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SideDto {
    @NotEmpty(message = "\'side's email\' cannot be blank")
    @Email(message = "\'side's email\' should be valid \'example@domain.com\'")
    private String email;
    @NotEmpty(message = "\'side's nickName\' cannot be blank")
    private String Nick;
    @NotEmpty(message = "\'side's role\' cannot be blank")
    private Role role;

}
