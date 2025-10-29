package com.dayspark.catch_thema.user.controller;

import com.dayspark.catch_thema.global.dto.ApiResponse;
import com.dayspark.catch_thema.user.dto.request.JoinRequest;
import com.dayspark.catch_thema.user.dto.response.JoinResponse;
import com.dayspark.catch_thema.user.service.UserService;
import com.dayspark.catch_thema.user.dto.response.ExistsResponse;
import com.dayspark.catch_thema.user.dto.response.LogoutResponse;
import com.dayspark.catch_thema.user.dto.response.WithdrawResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<JoinResponse>> join(@RequestBody JoinRequest request) {
        Long id = userService.join(request);
        JoinResponse dto = new JoinResponse(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("Auth", "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(ApiResponse.success(new LogoutResponse(true)));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<WithdrawResponse>> withdraw(Authentication authentication) {
        String email = authentication.getName();
        userService.deleteByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(new WithdrawResponse(email)));
    }

    @GetMapping("/exists/{email}")
    public ResponseEntity<ApiResponse<ExistsResponse>> exists(@PathVariable("email") String email) {
        boolean exists = userService.checkExistEmail(email);
        return ResponseEntity.ok(ApiResponse.success(new ExistsResponse(exists)));
    }
}


