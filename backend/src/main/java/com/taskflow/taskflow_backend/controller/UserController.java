package com.taskflow.taskflow_backend.controller;

import com.taskflow.taskflow_backend.dto.UserResponse;
import com.taskflow.taskflow_backend.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserResponse userDetails(@RequestParam
                                        @NotBlank(message = "email is required")
                                        @Email(message = "invalid email format") String email) {
       return userService.userDetails(email);
    }

}
