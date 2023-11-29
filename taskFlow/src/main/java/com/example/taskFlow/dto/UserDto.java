package com.example.taskFlow.dto;

import com.example.taskFlow.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    @NotEmpty(message = "\'user's name\' cannot be blank")
    private String userName;
    @NotEmpty(message = "\'user's last name\' cannot be blank")
    private String userLastName;
    @NotEmpty(message = "\'user's email\' cannot be blank")
    @Email(message = "\'user's email\' should be valid \'example@domain.com\'")
    private String userEmail;
    @NotEmpty(message = "\'user's password\'cannot be null type")
    @Size(min = 6, message = "\'user's password\' should be at least 6 characters long")
    private String userPassword;

}
