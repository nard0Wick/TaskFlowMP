package com.example.taskFlow.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "tasks")
public class Task {

    public Task(String name,
                String priorityCode,
                Date startedOn,
                Date doneOn,
                Date timeLimit,
                String explanation,
                boolean isCompleted,
                Set<Side> sides
                ) {
        this.name = name;
        this.priorityCode = priorityCode;
        this.startedOn = startedOn;
        this.doneOn = doneOn;
        this.timeLimit = timeLimit;
        this.explanation = explanation;
        this.isCompleted = isCompleted;
        this.sideSet = sides;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_name")
    private String name;

    @Column(name = "priority_code")
    private String priorityCode;

    @Column(name = "started_on")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date startedOn;

    @Column(name = "done_on")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date doneOn;

    @Column(name = "should_be_on")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date timeLimit;

    @Column(name = "description")
    private String explanation;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @ManyToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "sides_tasks",
            joinColumns = @JoinColumn(name = "task_id",
                    referencedColumnName = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "side_id",
                    referencedColumnName = "side_id")
    )
    Set<Side> sideSet;

    /*@ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "taskSet")

    private Set<Side> sideSet; */

    //--------------------------------------------idk-------------------------------
        /*@ManyToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "taskSet")
    Set<Side> sideSet;*/

    /*@ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinTable(
            name = "sides_tasks",
            joinColumns = @JoinColumn(name = "task_id",
                    referencedColumnName = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "side_id",
                    referencedColumnName = "side_id")
    )
    private Set<Side> sides;*/
}
