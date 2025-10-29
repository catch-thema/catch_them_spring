package com.dayspark.catch_thema.user.controller;


import com.dayspark.catch_thema.global.dto.ApiResponse;
import com.dayspark.catch_thema.user.dto.response.UserInfoResponse;
import com.dayspark.catch_thema.user.entity.User;
import com.dayspark.catch_thema.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userID}")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUser(@PathVariable Long userID){
        User user = userService.getUserById(userID);
        return ResponseEntity.ok(ApiResponse.success(new UserInfoResponse(user.getUserId(), user.getUserName(), user.getEmail())));
    }

}
