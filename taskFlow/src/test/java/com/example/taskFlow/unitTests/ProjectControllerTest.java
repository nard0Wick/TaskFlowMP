package com.example.taskFlow.unitTests;

import com.example.taskFlow.controller.ProjectController;
import com.example.taskFlow.dto.ProjectDto;
import com.example.taskFlow.model.Project;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ProjectController.class)
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProjectService projectService;
    @MockBean
    private ProjectRepository projectRepository;
    @Autowired
    private ObjectMapper objectMapper;

    User user;
    ProjectDto projectDto;

    Project project;
    @BeforeEach
    public void setUp() throws Exception {
        projectDto = new ProjectDto(
                "prj1",
                new SimpleDateFormat("yyyy-MM-dd")
                        .parse("2023-11-29"),
                new SimpleDateFormat("yyyy-MM-dd")
                        .parse("2023-11-30"),
                "no-context"
        );
        project = new Project(
                "prj1",
                new SimpleDateFormat("yyyy-MM-dd")
                        .parse("2023-11-29"),
                new SimpleDateFormat("yyyy-MM-dd")
                        .parse("2023-11-30"),
                "no-context"
        );
        user = new User(
                "user",
                "lastName",
                "user@domain.com",
                "password"
        );
    }

    @Test
    public void postProjectWorksFine() throws Exception{
        ResultActions response = mockMvc.perform(post("/project/create/{email}", user.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void postProjectDoesntWork() throws Exception{
        projectDto.setProjectName("");

        ResultActions response = mockMvc.perform(post("/project/create/{email}", user.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectDto)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void deleteProjectWorks() throws Exception{
        when(projectService.deleteProject(user.getEmail(), project.getProjectName())).thenReturn(true);

        ResultActions response = mockMvc.perform(delete("/project/delete/{email}/{projectName}",
                user.getEmail(),
                project.getProjectName()));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}
