package com.example.taskFlow.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
public class ProjectDto {
    private String projectName;
    private Date projectDayBreak;
    private Date projectDeadLine;
    private String projectContext;
}
