package com.example.taskFlow.unitTests;

import com.example.taskFlow.controller.ProjectController;
import com.example.taskFlow.dto.ProjectDto;
import com.example.taskFlow.dto.TaskDto;
import com.example.taskFlow.model.User;
import com.example.taskFlow.repository.ProjectRepository;
import com.example.taskFlow.repository.TaskRepository;
import com.example.taskFlow.service.ProjectService;
import com.example.taskFlow.service.TaskService;
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

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ProjectController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;
    @MockBean
    private TaskRepository taskRepository;
    @Autowired
    private ObjectMapper objectMapper;

    User user;
    ProjectDto projectDto;
    TaskDto taskDto;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User(
                "user",
                "lastName",
                "user@domain.com",
                "password"
        );
        projectDto = new ProjectDto(
                "prj1",
                new SimpleDateFormat("yyyy-MM-dd")
                        .parse("2023-11-29"),
                new SimpleDateFormat("yyyy-MM-dd")
                        .parse("2023-11-30"),
                "no-context"
        );
        taskDto = new TaskDto(
                "tsk1",
                "sweet",
                new SimpleDateFormat("yyyy-MM-dd")
                        .parse("2023-11-28"),
                new SimpleDateFormat("yyyy-MM-dd")
                        .parse("2023-11-30"),
                new SimpleDateFormat("yyyy-MM-dd")
                        .parse("2023-11-30"),
                "Task no.1",
                true,
                new HashSet<>() {{
                    add("nick1");
                    add("nick2");
                    add("nick3");
                }});
    }
//working here!
    /*@Test
    public void postTaskWorks() throws Exception{
        ResultActions response = mockMvc.perform(post("/task/create/{email}/{projectName}",
                user.getEmail(), "prj1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }*/

}
