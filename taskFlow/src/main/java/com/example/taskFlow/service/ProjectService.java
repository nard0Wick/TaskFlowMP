package com.example.taskFlow.service;

import com.example.taskFlow.dto.ProjectDto;
import com.example.taskFlow.dto.SideDto;
import com.example.taskFlow.dto.UserDto;
import com.example.taskFlow.model.Project;
import com.example.taskFlow.model.Role;
import com.example.taskFlow.model.Side;
import com.example.taskFlow.model.User;
import com.example.taskFlow.repository.ProjectRepository;
import com.example.taskFlow.repository.SideRepository;
import com.example.taskFlow.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SideRepository sideRepository;

    //working here!
    public User createProject(String userEmail, ProjectDto projectDto) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Email \'" + userEmail + "\' isn't related to any instance of User.class"));

        if(user.getProjectSet().stream().anyMatch(project ->
                project.getProjectName().equals(projectDto.getProjectName()))){
            throw new KeyAlreadyExistsException("Project \'"+ projectDto.getProjectName()+"\' is already related with an instance of Project.Class");
        }

        Side side = new Side(userEmail + "_CREATOR", Role.PROJECT_LEADER);
        user.getSideSet().add(side);

        user.getProjectSet().add(new Project(projectDto.getProjectName(),
                projectDto.getProjectDayBreak(),
                projectDto.getProjectDeadLine(),
                projectDto.getProjectContext()));

        user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectDto.getProjectName())).findFirst()
                .map(project -> project.getSideSet().add(side));

        sideRepository.save(side);
        return userRepository.save(user);

    }

    public Project getProject(String email, String projectName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email \'" + email + "\' isn't related to any instance of User.class"));

        return user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectName))
                .findFirst().orElseThrow(() -> new NullPointerException("Project " + projectName + " wasn't found"));

    }

    public User updateProject(String userEmail, String projectName, ProjectDto projectDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Email \'" + userEmail + "\' isn't related to any instance of User.class"));

        user.getProjectSet().stream()
                .filter(project -> project.getProjectName().equals(projectName)).findFirst()
                .map(project -> {
                    project.setProjectName(projectDto.getProjectName());
                    project.setDayBreak(projectDto.getProjectDayBreak());
                    project.setDeadline(projectDto.getProjectDeadLine());
                    project.setContext(projectDto.getProjectContext());
                    return project;
                })
                .orElseThrow(() -> new NullPointerException("Project \'" + projectName + "\' wasn't found"));

        return userRepository.save(user);

    }

    @Transactional
    public boolean deleteProject(String userEmail, String projectName) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException("Email \'" + userEmail + "\' isn't related to any instance of User.class"));

        Project project = user.getProjectSet().stream().filter(p -> p.getProjectName().equals(projectName)).findFirst()
                .orElseThrow(() -> new NullPointerException("Project \'" + projectName + "\' wasn't found"));

        sideRepository.deleteAll(project.getSideSet());

        user.getProjectSet().remove(project);

        projectRepository.deleteById(project.getId());


        return !projectRepository.existsById(project.getId());
    }

    //related methods with --sides--

    //can be improved
    public Project joinToTheParty(String creatorEmail, String projectName, Set<SideDto> sideDtoSet) {


        Project project = userRepository.findByEmail(creatorEmail)
                .map(user -> user.getProjectSet().stream().filter(p -> p.getProjectName().equals(projectName)).findFirst()
                        .orElseThrow(() ->
                                new NullPointerException("Project \'" + projectName + "\' wasn't found")))
                .orElseThrow(() ->
                        new IllegalArgumentException("User \'" + creatorEmail + "\' isn't related to any instance of User.class"));

        if (!sideDtoSet.isEmpty()) {

            Set<SideDto> sidesDto = sideDtoSet.stream().filter(
                    sideDto -> userRepository.findAll().stream().anyMatch(
                            user -> user.getEmail().equals(sideDto.getEmail())

                            && project.getSideSet().stream().noneMatch(side ->
                            side.getNick().equals(sideDto.getNick()))
                            //!user.getSideSet().stream().filter(side -> side.ge)
                    )
            ).collect(Collectors.toSet());

            Set<Side> sides = sidesDto.stream().map(sideDto ->
                            new Side(sideDto.getNick(), sideDto.getRole()))
                    .collect(Collectors.toSet());

            sides.addAll(project.getSideSet());
            project.setSideSet(sides);

            return projectRepository.save(project);
        }
        return project;
    }
}


