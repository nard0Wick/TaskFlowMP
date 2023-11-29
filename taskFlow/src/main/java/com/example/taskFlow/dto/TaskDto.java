package com.example.taskFlow.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
public class TaskDto {
    @NotEmpty(message = "\'task's name\' cannot be blank")
    private String taskName;
    private String taskPriorityCode;
    private Date taskStartedOn;
    private Date taskDoneOn;
    private Date taskTimeLimit;
    private String taskExplanation;
    private boolean taskIsCompleted;
    private Set<String> nickSet;
}
