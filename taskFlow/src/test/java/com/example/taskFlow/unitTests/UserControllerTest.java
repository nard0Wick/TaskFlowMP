package com.example.taskFlow.unitTests;

import com.example.taskFlow.controller.UserController;
import com.example.taskFlow.dto.UserDto;
import com.example.taskFlow.model.User;
import com.example.taskFlow.repository.ProjectRepository;
import com.example.taskFlow.repository.UserRepository;
import com.example.taskFlow.service.ProjectService;
import com.example.taskFlow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.management.openmbean.KeyAlreadyExistsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    User user;
    UserDto userDto;

    @BeforeEach
    public void setUp(){
        userDto = new UserDto(
                "user",
                "lastName",
                "user@domain.com",
                "password");

        user = new User(
                "user",
                "lastName",
                "user@domain.com",
                "password");
    }

    @Test
    public void postUserWorks() throws Exception{
        when(userService.createUser(userDto)).thenReturn(user);

        ResultActions response = mockMvc
                .perform(post("/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void postUserFails() throws Exception{
        when(userService.createUser(userDto)).thenReturn(user);
        when(userService.createUser(userDto)).thenThrow(KeyAlreadyExistsException.class);//same reference

        userDto.setUserName("");//expected: not empty
        userDto.setUserEmail("user");//expected: user@domain.com
        userDto.setUserPassword("pass");//expected: al least six characters long

        ResultActions response = mockMvc
                .perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void putUserWorks() throws Exception{
        //when(userService.updateUser(user.getEmail(),userDto)).thenReturn(user);

        ResultActions response = mockMvc
                .perform(put("/user/update/{email}", user.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserWorks() throws Exception{
        when(userService.deleteUser(user.getEmail())).thenReturn(true);

        ResultActions response = mockMvc
                .perform(delete("/user/delete/{email}", user.getEmail()));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
