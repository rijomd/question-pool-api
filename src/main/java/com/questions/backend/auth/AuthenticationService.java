package com.questions.backend.auth;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.questions.backend.Config.JwtService;
import com.questions.backend.dto.RegisterRequestDTO;
import com.questions.backend.user.Role;
import com.questions.backend.user.UserRepository;
import com.questions.backend.user.UserStatus;
import com.questions.backend.user.Users;

@Component
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public Integer register(RegisterRequestDTO request) {
        var user = Users.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(user);

        return user.getId();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Users user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        ArrayList<String> menus = new ArrayList<String>();

        if (user.getRole().equals(Role.CANDIDATES)) {
            menus.add("answerPool");
        } else {
            menus.add("Dashboard");
            menus.add("UserList");
            menus.add("jobList");
            menus.add("questionList");
            menus.add("questionPool");
        }
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).menu_list(menus)
                .users(user)
                .build();
    }

}
