package com.example.taskFlow.controller;

import com.example.taskFlow.dto.ProjectDto;
import com.example.taskFlow.dto.SideDto;
import com.example.taskFlow.dto.UserDto;
import com.example.taskFlow.model.Role;
import com.example.taskFlow.service.ProjectService;
import com.example.taskFlow.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/project")
@Tag(name = "Projects")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Operation(summary = "Create new project")
    @PostMapping("/create/{email}")
    public ResponseEntity<Object> createProject(@PathVariable String email,
                                             @Valid @RequestBody ProjectDto projectDto){
        return new ResponseEntity<>(projectService.createProject(email, projectDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get an existing project")
    @GetMapping("/{userEmail}/{projectName}")
    public ResponseEntity<Object> getProject(@PathVariable String userEmail,
                                             @PathVariable String projectName){
        return new ResponseEntity<>(projectService.getProject(userEmail, projectName), HttpStatus.OK);
    }
    @Operation(summary = "Update an existing project")
    @PutMapping("/update/{email}/{projectName}")
    public ResponseEntity<Object> updateProject(@PathVariable String email,
                                             @PathVariable String projectName,
                                             @Valid @RequestBody ProjectDto projectDto){
        return new ResponseEntity<>(projectService.updateProject(email, projectName, projectDto), HttpStatus.OK);
    }

    @Operation(summary = "Delete an existing project")
    @DeleteMapping("/{email}/{projectName}")
    public ResponseEntity<Object> deleteProject(@PathVariable String email, @PathVariable String projectName){

        return new ResponseEntity<>(projectService.deleteProject(email, projectName), HttpStatus.OK);
    }

    @Operation(summary = "Add people to the party!")
    @PostMapping("/AddPeople/{email}/{projectName}")
    public ResponseEntity<Object> joinProject(@PathVariable String email,
                                              @PathVariable String projectName,
                                              @Valid @RequestBody Set<SideDto> sideDtoSet){
        return new ResponseEntity<>(projectService.joinToTheParty(email, projectName, sideDtoSet), HttpStatus.OK);
    }

}
