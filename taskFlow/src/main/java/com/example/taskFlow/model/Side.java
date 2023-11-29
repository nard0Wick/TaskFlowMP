package com.example.taskFlow.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sides")
public class Side {

    public Side(String nick, Role role) {
        this.nick = nick;
        this.role = role;
    }

    @Id
    @Column(name = "side_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick_name")
    private String nick;

    @Column(name = "role")
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "sideSet")
    @JsonBackReference
    private Set<Task> taskSet;

    /*@ManyToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "sides_tasks",
            joinColumns = @JoinColumn(name = "side_id",
            referencedColumnName = "side_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id",
            referencedColumnName = "task_id")
    )
    @JsonBackReference
    Set<Task> taskSet;*/

    //--------------------------------------------------over test--------------------------------
    /*@ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "user_id",
    referencedColumnName = "user_id")
    User user;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "project_id",
    referencedColumnName = "project_id")
    Project project;*/

    /*@ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "sides")

    private Set<Task> taskSet;*/
    //
    /*@ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinTable(
            name = "sides_tasks",
            joinColumns = @JoinColumn(name = "side_id",
                    referencedColumnName = "side_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id",
                    referencedColumnName = "task_id")
    )
    private Set<Task> taskSet;*/

    /*//recently added
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private Project project;*/
}
