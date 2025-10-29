package com.dayspark.catch_thema.global.jwt;

import com.dayspark.catch_thema.global.dto.ApiResponse;
import com.dayspark.catch_thema.user.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dayspark.catch_thema.global.exception.ErrorMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final com.dayspark.catch_thema.user.repository.UserRepository userRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                       com.dayspark.catch_thema.user.repository.UserRepository userRepository) {
        super.setFilterProcessesUrl("/api/auth/login");
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.objectMapper = new ObjectMapper();
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationException("Failed to parse login request") {};
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = authResult.getName();
        com.dayspark.catch_thema.user.entity.User user = userRepository.findByEmail(username);
        String role = user != null ? user.getRole() : authResult.getAuthorities().stream().findFirst().map(Object::toString).orElse("ROLE_USER");

        long expirationSeconds = 60L * 60L * 10L; // 10시간
        Long userId = user != null ? user.getUserId() : null;
        String token = jwtUtil.createJwt(userId, username, role, expirationSeconds);

        ResponseCookie jwtCookie = ResponseCookie.from("Auth", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(expirationSeconds)
                .sameSite("Lax")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        LoginResponse dto = new LoginResponse(userId, username, role, expirationSeconds);
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(dto)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.fail(ErrorMessage.USER_WRONG_PASSWORD.getMessage())
        ));
    }
}


