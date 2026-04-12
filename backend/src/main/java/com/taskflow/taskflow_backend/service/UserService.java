package com.taskflow.taskflow_backend.service;

import com.taskflow.taskflow_backend.dto.UserResponse;
import com.taskflow.taskflow_backend.entity.User;
import com.taskflow.taskflow_backend.exception.NotFoundException;
import com.taskflow.taskflow_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse userDetails(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
