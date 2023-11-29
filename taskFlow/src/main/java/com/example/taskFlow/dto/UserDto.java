package com.example.taskFlow.dto;

import com.example.taskFlow.model.Role;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private String userName;
    private String userLastName;
    private String userEmail;
    private String userPassword;

}
