package com.example.taskFlow.controller;

import com.example.taskFlow.dto.ProjectDto;
import com.example.taskFlow.dto.TaskDto;
import com.example.taskFlow.service.ProjectService;
import com.example.taskFlow.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@Tag(name = "Tasks")
public class TaskController {
    @Autowired
    TaskService taskService;

    @Operation(summary = "Add a task")
    @PostMapping("/create/{userEmail}/{projectName}")
    public ResponseEntity<Object> createTask(@PathVariable String userEmail,
                                             @PathVariable String projectName,
                                             @RequestBody TaskDto taskDto){
        return new ResponseEntity<>(taskService.addTask(userEmail, projectName, taskDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get an existing task")
    @GetMapping("/{userEmail}/{projectName}/{taskName}")
    public ResponseEntity<Object> getTask(@PathVariable String userEmail,
                                          @PathVariable String projectName,
                                          @PathVariable String taskName){
        return new ResponseEntity<>(taskService.getTask(userEmail, projectName, taskName), HttpStatus.OK);
    }
    @Operation(summary = "Update an existing task")
    @PutMapping("/update/{userEmail}/{projectName}/{taskName}")
    public ResponseEntity<Object> updateTask(@PathVariable String userEmail,
                                             @PathVariable String projectName,
                                             @PathVariable String taskName,
                                             @RequestBody TaskDto taskDto){
        return new ResponseEntity<>(taskService.updateTask(userEmail,projectName, taskName, taskDto), HttpStatus.OK);
    }

    @Operation(summary = "Delete an existing task")
    @DeleteMapping("delete/{userEmail}/{projectName}/{taskName}")
    public ResponseEntity<Object> deleteTask(@PathVariable String userEmail,
                                             @PathVariable String projectName,
                                             @PathVariable String taskName){

        return new ResponseEntity<>(taskService.deleteTask(userEmail, projectName, taskName),HttpStatus.OK);
    }
    @Operation(summary = "Assign an existing task")
    @PostMapping("/assign/{email}/{projectName}/{taskName}")
    public ResponseEntity<Object> createUser(@PathVariable String email,
                                             @PathVariable String projectName,
                                             @PathVariable String taskName){

        return new ResponseEntity<>(/*taskService.assignTask(email, projectName, taskName),*/HttpStatus.OK);
    }
}
