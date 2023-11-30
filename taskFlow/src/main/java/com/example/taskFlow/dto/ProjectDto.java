package com.example.taskFlow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ProjectDto {
    @NotEmpty(message = "\'project's name\' cannot be blank")
    private String projectName;
    private Date projectDayBreak;
    private Date projectDeadLine;
    private String projectContext;
}
