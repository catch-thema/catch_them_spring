package com.dayspark.catch_thema.user.repository;


import com.dayspark.catch_thema.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByUserName(String userName);

    User findByEmail(String email);

}
