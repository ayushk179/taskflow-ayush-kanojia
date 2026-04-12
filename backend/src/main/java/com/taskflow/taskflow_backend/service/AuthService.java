package com.taskflow.taskflow_backend.service;

import com.taskflow.taskflow_backend.dto.LoginRequest;
import com.taskflow.taskflow_backend.dto.RegisterRequest;
import com.taskflow.taskflow_backend.entity.User;
import com.taskflow.taskflow_backend.exception.BadRequestException;
import com.taskflow.taskflow_backend.exception.NotFoundException;
import com.taskflow.taskflow_backend.exception.UnauthorizedException;
import com.taskflow.taskflow_backend.repository.UserRepository;
import com.taskflow.taskflow_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        log.info("user : {} is saved",request.getName());

        userRepository.save(user);
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return jwtUtil.generateToken(user);
    }
}

