package com.example.taskFlow.service;

import com.example.taskFlow.dto.TaskDto;
import com.example.taskFlow.model.Project;
import com.example.taskFlow.model.Side;
import com.example.taskFlow.model.Task;
import com.example.taskFlow.model.User;
import com.example.taskFlow.repository.ProjectRepository;
import com.example.taskFlow.repository.SideRepository;
import com.example.taskFlow.repository.TaskRepository;
import com.example.taskFlow.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    SideRepository sideRepository;

    public User addTask(String email, String projectName, TaskDto taskDto) {

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("User " + email + " isn't related to any instance of User.class"));

        Project project = user.getProjectSet().stream().filter(p -> p.getProjectName().equals(projectName)).findFirst()
                .orElseThrow(() -> new NullPointerException("Project " + projectName + " wasn't found"));
        //validation
        if(project.getTaskSet().stream().anyMatch(task -> task.getName().equals(taskDto.getTaskName()))){
            throw new KeyAlreadyExistsException("Task \'"+ taskDto.getTaskName()+"\' is already related with an instance of Task.Class");
        }

        Set<Side> sides = project.getSideSet().stream().filter(
                side -> taskDto.getNickSet().stream().anyMatch(
                        nick -> side.getNick().equals(nick)
                )
        ).collect(Collectors.toSet());

        project.getTaskSet().add(taskRepository.save(new Task(taskDto.getTaskName(),
                                taskDto.getTaskPriorityCode(),
                                taskDto.getTaskStartedOn(),
                                taskDto.getTaskDoneOn(),
                                taskDto.getTaskTimeLimit(),
                                taskDto.getTaskExplanation(),
                                taskDto.isTaskIsCompleted(),
                                sides
                )));

        return userRepository.save(user);
    }

    public Task getTask(String email, String projectName, String taskName) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Email \'" + email + "\' isn't related to any instance of User.class"));

        return user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectName)).findFirst()
                .map(project -> project.getTaskSet().stream().filter(t -> t.getName().equals(taskName)).findFirst()
                        .orElseThrow(() ->
                                new NullPointerException("Task \'" + taskName + "\' wasn't found"))).orElseThrow(() ->
                        new NullPointerException("Project \'" + projectName + "\' wasn't found"));
    }

    public User updateTask(String email, String projectName, String taskName, TaskDto taskDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Email \'" + email + "\' isn't related to any instance of User.class"));

        Project project = user.getProjectSet().stream().filter(p -> p.getProjectName().equals(projectName)).findFirst()
                .orElseThrow(() -> new NullPointerException("Project \'" + projectName + "\' wasn't found"));

        Set<Side> sides = project.getSideSet().stream().filter(
                side -> taskDto.getNickSet().stream().anyMatch(
                        nick -> side.getNick().equals(nick)
                )
        ).collect(Collectors.toSet());

        project.getTaskSet().stream()
                .filter(task -> task.getName().equals(taskName)).findFirst().map(
                        task -> {
                            task.setName(taskDto.getTaskName());
                            task.setPriorityCode(taskDto.getTaskPriorityCode());
                            task.setStartedOn(taskDto.getTaskStartedOn());
                            task.setDoneOn(taskDto.getTaskDoneOn());
                            task.setTimeLimit(taskDto.getTaskTimeLimit());
                            task.setExplanation(taskDto.getTaskExplanation());
                            task.setCompleted(taskDto.isTaskIsCompleted());
                            task.setSideSet(sides);
                            return task;
                        }
                ).orElseThrow(() -> new NullPointerException("Task \'" + taskName + "\' wasn't found"));

        return userRepository.save(user);
    }

    //on process
    @Transactional
    public Boolean deleteTask(String email, String projectName, String taskName) {
        Project project = userRepository.findByEmail(email)
                .map(user -> user.getProjectSet().stream().filter(p -> p.getProjectName().equals(projectName)).findFirst()
                        .orElseThrow(()->new NullPointerException("Project \'" + projectName + "\' wasn't found")))
                .orElseThrow(()->
                new IllegalArgumentException("Email \'" + email + "\' isn't related to any instance of User.class"));

        Task task = project.getTaskSet().stream().filter(t -> t.getName().equals(taskName)).findFirst()
                .orElseThrow(()->new NullPointerException("Task \'" + taskName + "\' wasn't found"));

        project.getTaskSet().remove(task);



//        Task task = user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectName)).findFirst()
//                .map(project -> project.getTaskSet().stream().filter(t -> t.getName().equals(taskName)).findFirst()
//                ).orElseThrow(() -> new NullPointerException("Project \'" + projectName + "\' wasn't found")//
//                ).orElseThrow(() -> new NullPointerException("Task \'" + taskName + "\' wasn't found"));

        //sideRepository.deleteAll(task.getSideSet());


        taskRepository.deleteById(task.getId());

        return !taskRepository.existsById(task.getId());
    }

}
