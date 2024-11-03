package com.questions.backend.dto;

import org.springframework.web.multipart.MultipartFile;

import com.questions.backend.user.Role;
import com.questions.backend.user.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private String resumeLink;
    private String yearOfExp;
    private Integer job_id;
    private UserStatus status;
    private MultipartFile file;
}
