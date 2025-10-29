package com.dayspark.catch_thema.user.service;


import com.dayspark.catch_thema.user.dto.request.JoinRequest;
import com.dayspark.catch_thema.user.entity.User;
import com.dayspark.catch_thema.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.dayspark.catch_thema.global.exception.ErrorMessage;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean checkExistEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public Long join(JoinRequest joinRequest){
        if (userRepository.existsByEmail(joinRequest.getEmail())) {
            throw new IllegalArgumentException(ErrorMessage.USER_ALREADY_EXIST.getMessage());
        }
        String encoded = passwordEncoder.encode(joinRequest.getPassword());
        User user = User.of(joinRequest.getUserName(), joinRequest.getEmail(), encoded);
        User saved = userRepository.save(user);
        return saved.getUserId();
    }

    public String getUserInformation(Long userId){
        return userRepository.findById(userId)
                .map(User::getUserName)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_NOT_EXIST.getMessage()));
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_NOT_EXIST.getMessage()));
    }

    public void deleteByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException(ErrorMessage.USER_NOT_EXIST.getMessage());
        }
        userRepository.delete(user);
    }

}
