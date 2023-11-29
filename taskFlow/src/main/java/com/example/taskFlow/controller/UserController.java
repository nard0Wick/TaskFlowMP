package com.example.taskFlow.controller;

import com.example.taskFlow.dto.UserDto;
import com.example.taskFlow.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/")
@Tag(name = "Users")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(summary = "Create new user")
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }
    @Operation(summary = "Get a user by email")
    @GetMapping("/get/{email}")
    public ResponseEntity<Object> getUser(@PathVariable String email){
        return new ResponseEntity<>(userService.getUser(email), HttpStatus.OK);
    }
    @Operation(summary = "Get all users")
    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @Operation(summary = "Update a user by email")
    @PutMapping("/update/{email}")
    public ResponseEntity<Object> updateUser(@PathVariable String email,
                                             //@Valid
                                             @RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.updateUser(email, userDto), HttpStatus.OK);
    }
    @Operation(summary = "Delete a user by email")
    @DeleteMapping ("/delete/{email}")
    public ResponseEntity<Object> delUser(@PathVariable String email){

        return new ResponseEntity<>(userService.deleteUser(email), HttpStatus.OK);
    }
}
