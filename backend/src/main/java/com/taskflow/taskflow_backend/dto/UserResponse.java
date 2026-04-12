package com.taskflow.taskflow_backend.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserResponse {

    private UUID id;

    private String name;

    private String email;
}
