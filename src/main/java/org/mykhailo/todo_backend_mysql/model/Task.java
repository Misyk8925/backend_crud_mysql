package org.mykhailo.todo_backend_mysql.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.mykhailo.todo_backend_mysql.repository.UserRepository;

@Entity
@Data
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "description", nullable = false)
    public String description;

    @Column(name = "status", nullable = false)
    public String status;

    @Column(name = "date", nullable = false)
    public String date;

    @ManyToOne
    @JoinColumn(name = "user")
    @JsonBackReference
    private User user;


}
