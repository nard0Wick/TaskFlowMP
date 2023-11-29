package com.example.taskFlow.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project {

    public Project(String projectName, Date dayBreak, Date deadline, String context) {
        this.projectName = projectName;
        this.dayBreak = dayBreak;
        this.deadline = deadline;
        this.context = context;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "initium")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date dayBreak;

    @Column(name = "target_time")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date deadline;

    @Column(name = "context_load")
    private String context;

    //can be improved?
    @Column(name = "completed_tasks")
    private int completedTasks;

    @Column(name = "incompleted_tasks")
    private int uncompletedTasks;
    //

    @OneToMany(targetEntity = Task.class,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id",
            referencedColumnName = "project_id")
    private Set<Task> taskSet = new HashSet<>();

    //over test
    @OneToMany(targetEntity = Side.class,
            //merge
            cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id",
            referencedColumnName = "project_id")
    private Set<Side> sideSet = new HashSet<>();

    /*@JsonManagedReference
    @OneToMany(cascade=CascadeType.ALL,
            mappedBy = "project")
    Set<Side> sideSet = new HashSet<>();*/

    /*@OneToOne(mappedBy = "project")
    private Side side;*/
}
