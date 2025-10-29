package com.dayspark.catch_thema.user.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;


    @Column(nullable = false, length = 20)
    private String userName;

    @Column(nullable = false, length = 100)
    private String email;

    private String password;
    private String role;

    protected User() {}

    private User(String userName, String email, String password){
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = "ROLE_CUSTOMER";
    }

    public static User of(String userName, String email ,String password){
        return new User(userName, email, password);
    }


}
