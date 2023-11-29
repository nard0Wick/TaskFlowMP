package com.example.taskFlow.dto;

import com.example.taskFlow.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SideDto {
    private String email;
    private String Nick;
    private Role role;

}
