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
    public User createProject(String userEmail, ProjectDto projectDto){

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()->
                        new IllegalArgumentException("Email "+userEmail+" isn't related to any instance of User.class"));

        Side side = new Side(userEmail+"_CREATOR", Role.PROJECT_LEADER);
        user.getSideSet().add(side);

        user.getProjectSet().add(new Project(projectDto.getProjectName(),
                projectDto.getProjectDayBreak(),
                projectDto.getProjectDeadLine(),
                projectDto.getProjectContext()));

        user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectDto.getProjectName())).findFirst()
                .map(project -> project.getSideSet().add(side));

        sideRepository.save(side);
        return userRepository.save(user);

        /*user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectDto.getProjectName())).findFirst()
                .map(project -> project.getSideSet().add(new Side(userEmail+"_CREATOR",Role.PROJECT_LEADER)));

        return userRepository.save(user);*/

        /*Project project = new Project(
                projectDto.getProjectName(),
                projectDto.getProjectDayBreak(),
                projectDto.getProjectDeadLine(),
                projectDto.getProjectContext(),

        );*/
//
//        user.getProjectSet().add(project);
//        //
//        Side side = new Side(userEmail+"_CREATOR",Role.PROJECT_LEADER);
//        user.getSideSet().add(side);
//        project.getSideSet().add(side);
//
//        //sideRepository.save(side);
//        projectRepository.save(project);
//
//        //defining creator as project leader
//        /*user.getSideSet().add(side);
//
//        //storing on the database
//        userRepository.save(user);
//
//        Project project = projectRepository.findByProjectName(projectDto.getProjectName())
//                .orElseThrow(()->
//                        new NullPointerException("Something went wrong, from creating project method."));
//
//        project.getSideSet().add(side);
//
//        projectRepository.save(project);
//
//        //setting project on side project reference
//        side.setProject(project);
//        sideRepository.save(side);*/
//
//        return userRepository.save(user);
    }
    public Project getProject(String email, String projectName){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("Email "+email+" isn't related to any instance of User.class"));

        return user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectName))
                .findFirst().orElseThrow(()->new NullPointerException("Project "+projectName+" wasn't found"));

        /*return projectRepository.findByProjectName(projectName)
                .orElseThrow(() -> new NullPointerException("Project "+projectName+" doesn't march any one stored"));*/
    }
    public User updateProject(String userEmail, String projectName, ProjectDto projectDto){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()->
                        new NullPointerException("Email "+userEmail+" isn't related to any instance of User.class"));

        user.getProjectSet().stream()
                .filter(project -> project.getProjectName().equals(projectName)).findFirst()
                    .map(project -> {project.setProjectName(projectDto.getProjectName());
                                     project.setDayBreak(projectDto.getProjectDayBreak());
                                     project.setDeadline(projectDto.getProjectDeadLine());
                                     project.setContext(projectDto.getProjectContext());
                                     return project;})
                .orElseThrow(()->new NullPointerException("Project "+projectName+" wasn't found"));

        //
        //Side side = new Side(Role.PROJECT_LEADER);

        //defining creator as project leader
        //user.getSideSet().add(side);

        //storing on the database
        return userRepository.save(user);

        /*Project project = projectRepository.findByProjectName(projectDto.getProjectName())
                .orElseThrow(()->
                        new NullPointerException("Something went wrong, from creating project method."));

        project.getSideSet().add(side);

        projectRepository.save(project);

        return user;*/
    }
    @Transactional
    public boolean deleteProject(String userEmail, String projectName){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()->
                new IllegalArgumentException("Email "+userEmail+" isn't related to any instance of User.class"));

        /*user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectName)).findFirst()
                .map(project -> {projectRepository.delete(project);
                                 return null;});
        userRepository.save(user);*/

        Project project = user.getProjectSet().stream().filter(p -> p.getProjectName().equals(projectName)).findFirst()
                .orElseThrow(()->new NullPointerException("Project "+projectName+" wasn't found"));

        //project.getSideSet().removeAll(project.getSideSet());
        sideRepository.deleteAll(project.getSideSet());

        user.getProjectSet().remove(project);

        projectRepository.deleteById(project.getId());


        return !projectRepository.existsById(project.getId());
    }

    //related methods with --sides--
    //return project for all
    public Project joinToTheParty(String creatorEmail, String projectName, Set<SideDto> sideDtoSet){

        Project project = userRepository.findByEmail(creatorEmail)
                .map(user -> user.getProjectSet().stream().filter(p -> p.getProjectName().equals(projectName)).findFirst()
                        .orElseThrow( ()->
                                new IllegalArgumentException("Project "+projectName+" wasn't found") ))
                .orElseThrow(()->
                        new IllegalArgumentException("User "+creatorEmail+" isn't related to any instance of User.class"));


        /*Set<Side> sides = userRepository.findAll().stream().filter(
                user -> sideDtoSet.stream().anyMatch(
                        sideDto -> sideDto.getEmail().equals(user.getEmail())
                )
            ).collect(Collectors.toSet());  */

        Set<SideDto> sidesDto = sideDtoSet.stream().filter(
                sideDto -> userRepository.findAll().stream().anyMatch(
                        user -> user.getEmail().equals(sideDto.getEmail())
                )
        ).collect(Collectors.toSet());

        Set<Side> sides = sidesDto.stream().map(sideDto ->
                        new Side(sideDto.getNick(), sideDto.getRole()))
                .collect(Collectors.toSet());
///i'm here
        //project.getSideSet().stream().filter(side -> side.getNick().equals())
        sides.addAll(project.getSideSet());
        project.setSideSet(sides);

        //testing
        /*User user = userRepository.findByEmail(creatorEmail).orElseThrow(()->
                new NullPointerException("Cannot resolve your request"));
        user.getSideSet().addAll(sides);
        userRepository.save(user);*/
        //
        return projectRepository.save(project);

    }
//    public User joinToTheParty(String projectName, String userEmail, Role role){
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(()->new NullPointerException("User "+userEmail+" wasn't found"));
//
//        Project project = projectRepository.findByProjectName(projectName)
//                .orElseThrow(()->new NullPointerException("Project "+projectName+"doesn't exits"));
//
//        Side side = new Side(role);
//
//        /*user.getSideSet().add(side);
//        //userRepository.save(user);
//
//        project.getSideSet().add(side);
//        //projectRepository.save(project);
//
//        side.setProject(project);
//        //sideRepository.save(side);
//
//
//
//        //project.
//
//        //project.getSideSet().add(new Side())
//
//        return user;*/
//
//
//        user.getSideSet().add(side);
//        userRepository.save(user);
//
//        project.getSideSet().add(side);
//        projectRepository.save(project);
//
//        return user;
//    }
    @Transactional
    public void rejectFromTheParty(String projectName, String userEmail){
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(()->new NullPointerException("User "+userEmail+" wasn't found"));
//
//        Project project = projectRepository.findByProjectName(projectName)
//                .orElseThrow(()->new NullPointerException("Project "+projectName+"doesn't exits"));
//
//        user.getSideSet().stream().filter(side ->
//                side.getProject().equals(project)).findFirst().map(side -> {project.getSideSet().remove(side);
//                                                                            user.getSideSet().remove(side);
//                                                                            sideRepository.delete(side);
//                                                                            return null;});
//
//        return userRepository.save(user);
    }


}


