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


        Set<Side> sides = project.getSideSet().stream().filter(
                side -> taskDto.getNickSet().stream().anyMatch(
                        nick -> side.getNick().equals(nick)
                )
        ).collect(Collectors.toSet());




//        /*User user = userRepository.findByEmail(userEmail).orElseThrow(()->
//                new NullPointerException(""));*/
//        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User "+ email+" isn't related to any instance of User.class"));
//
//
//
//        /*Project project = projectRepository.findByProjectName(projectName).orElseThrow(()->
//                new NullPointerException("Project "+ projectName +" doesn't exists"));*/
//
//
//        Project project = user.getProjectSet().stream().filter(p -> p.getProjectName().equals(projectName)).findFirst()
//                .orElseThrow(()->new NullPointerException("Project "+projectName+" wasn't found"));
//
//        Set<Side> sides = project.getSideSet().stream().filter(
//                side -> taskDto.getNickSet().stream().anyMatch(
//                        nick -> side.getNick().equals(nick)
//                )
//        ).collect(Collectors.toSet());
//
//        //reference code
//        //System.out.println(sides.toString());
//
        project.getTaskSet().add(taskRepository.save(new Task(taskDto.getTaskName(),
                                taskDto.getTaskPriorityCode(),
                                taskDto.getTaskStartedOn(),
                                taskDto.getTaskDoneOn(),
                                taskDto.getTaskTimeLimit(),
                                taskDto.getTaskExplanation(),
                                taskDto.isTaskIsCompleted(),
                                sides
                )));

//        project.getTaskSet().add(new Task(taskDto.getTaskName(),
//                                          taskDto.getTaskPriorityCode(),
//                                          taskDto.getTaskStartedOn(),
//                                          taskDto.getTaskDoneOn(),
//                                          taskDto.getTaskTimeLimit(),
//                                          taskDto.getTaskExplanation(),
//                                          taskDto.isTaskIsCompleted()
//                                ));
        /*taskRepository.save(new Task(taskDto.getTaskName(),
                taskDto.getTaskPriorityCode(),
                taskDto.getTaskStartedOn(),
                taskDto.getTaskDoneOn(),
                taskDto.getTaskTimeLimit(),
                taskDto.getTaskExplanation(),
                taskDto.isTaskIsCompleted()));*/

        /*Task task = project.getTaskSet().stream().filter(t -> t.getName().equals(taskDto.getTaskName())).findFirst()
                .orElseThrow(()->new RuntimeException("Something went wrong while creating new task"));

        task.getSideSet().addAll(sides);

        projectRepository.save(project);*/

        return userRepository.save(user);
    }

    public Task getTask(String email, String projectName, String taskName) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Email " + email + " isn't related to any instance of User.class"));
        /*taskRepository.findByName(taskName).orElseThrow(()->
                new NullPointerException("Task "+taskName+" doesn't exits"));*/
        return user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectName)).findFirst()
                .map(project -> project.getTaskSet().stream().filter(t -> t.getName().equals(taskName)).findFirst()
                        .orElseThrow(() ->
                                new NullPointerException("Task " + taskName + " wasn't found"))).orElseThrow(() ->
                        new NullPointerException("Project " + projectName + " wasn't found"));
    }

    public User updateTask(String email, String projectName, String taskName, TaskDto taskDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Email " + email + " isn't related to any instance of User.class"));

        Project project = user.getProjectSet().stream().filter(p -> p.getProjectName().equals(projectName)).findFirst()
                .orElseThrow(() -> new NullPointerException("Project " + projectName + " wasn't found"));

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
                            //task.setSideSet(sides);
                            return task;
                        }
                ).orElseThrow(() -> new NullPointerException("Task " + taskName + " wasn't found"));

        /*Project project = projectRepository.findByProjectName(projectName).orElseThrow(()->
                new NullPointerException("Project "+projectName+" doesn't exits"));

        project.getTaskSet().stream().filter(task -> task.getName().equals(taskName))
                .map(task -> {task.setName(taskDto.getTaskName());
                              task.setPriorityCode(taskDto.getTaskPriorityCode());
                              task.setStartedOn(taskDto.getTaskStartedOn());
                              task.setDoneOn(taskDto.getTaskDoneOn());
                              task.setTimeLimit(taskDto.getTaskTimeLimit());
                              task.setExplanation(taskDto.getTaskExplanation());
                              task.setCompleted(taskDto.isTaskIsCompleted());
                              return task;});*/

        return userRepository.save(user);
    }

    @Transactional
    public Boolean deleteTask(String email, String projectName, String taskName) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Email " + email + " isn't related to any instance of User.class"));

        Task task = user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectName)).findFirst()
                .map(project -> project.getTaskSet().stream().filter(t -> t.getName().equals(taskName)).findFirst()
                ).orElseThrow(() -> new NullPointerException("Task " + taskName + " wasn't found")
                ).orElseThrow(() -> new NullPointerException("Project " + projectName + " wasn't found"));

        //sideRepository.deleteAll(task.getSideSet());

        taskRepository.deleteById(task.getId());


        /*user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectName)).findFirst()
                .map(project -> project.getTaskSet().stream().filter(task -> task.getName().equals(taskName)).findFirst()
                        .map(task -> {taskRepository.deleteByName(taskName);
                                                    return null;}
                        ).orElseThrow(()->new NullPointerException("Task "+taskName+" wasn't found"))
                ).orElseThrow(()->new NullPointerException("Project "+projectName+" wasn't found"));*/
        //user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectName)).findFirst()
        //        .map(project -> project.getTaskSet().stream().filter(task -> task.getName().equals(taskName)).findFirst());

        /*return user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectName)).findFirst()
                .map(project -> project.getTaskSet().stream().filter(task -> task.getName().equals(taskName)).findFirst())
                .*/

        /*user.getProjectSet().stream().filter(project -> project.getProjectName().equals(projectName)).findFirst()
                .map(project -> project.getTaskSet().stream().filter(task -> task.getName().equals(taskName)).findFirst()
                        .map(task -> {taskRepository.deleteByName(taskName);
                                      return null;}).orElseThrow(()->new NullPointerException("Task "+taskName+" wasn't found"))
                ).orElseThrow();*/
        /*Project project = projectRepository.findByProjectName(projectName)
                .orElseThrow(()->
                        new NullPointerException("Project "+projectName+" doesn't exits"));

        project.getTaskSet().remove(taskRepository.findByName(taskName).orElseThrow(()->
                new NullPointerException("Task "+taskName+" wasn't found")));

        taskRepository.deleteByName(taskName);*/

        return !taskRepository.existsById(task.getId());
    }

    public void assignTask(String email, String projectName, String taskName) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(()->new NullPointerException("Email "+email+" doesn't not match with any user"));
//        Project project = projectRepository.findByProjectName(projectName)
//                .orElseThrow(()->
//                        new NullPointerException("Project "+projectName+" doesn't exits"));
//
//        user.getSideSet().stream().filter(side -> side.getProject().equals(project)).findFirst()
//                .map(side -> side.getTaskSet().add(taskRepository.findByName(taskName)
//                        .orElseThrow(()->new NullPointerException("Task "+taskName+" wasn't found"))));
//
//        /*user.getSideSet().stream().filter(side -> side.getProject().equals(project)).findFirst()
//                .map(side -> side.getTaskSet().add(taskRepository.findByName(taskName)
//                        .orElseThrow(()->new NullPointerException("Task "+taskName+" wasn't found"))));*/
//
//        return userRepository.save(user);
    }
}
