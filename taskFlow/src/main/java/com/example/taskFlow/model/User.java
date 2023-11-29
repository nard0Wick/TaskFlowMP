package com.example.taskFlow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.print.attribute.standard.Sides;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    public User(String name, String lastName, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_last_name")
    private String lastName;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @Column(name = "use_role")
    private Role role;

    @OneToMany(targetEntity = Project.class,
               cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",
                referencedColumnName = "user_id")
    private Set<Project> projectSet = new HashSet<>();

    //over test
    /*@OneToMany(cascade=CascadeType.ALL,
            mappedBy = "user")
    Set<Side> sideSet = new HashSet<>();*/
    @OneToMany(targetEntity = Side.class,
            //merge
            cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id")
    private Set<Side> sideSet;

    /*@ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_tasks",
            joinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id",
                    referencedColumnName = "task_id")
    )
    private Set<Task> taskSet;*/

}
