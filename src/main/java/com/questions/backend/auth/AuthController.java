package com.questions.backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.questions.backend.dto.RegisterRequestDTO;
import com.questions.backend.user.Role;

@RestController
@RequestMapping(path = "api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    // for manage or hr
    @PostMapping("/activate")
    public ResponseEntity<Integer> register(@RequestBody RegisterRequestDTO request) throws Exception {
        if (request.getRole().name().equals(Role.HR.name()) || request.getRole().name().equals(Role.MANAGER.name())) {
            return ResponseEntity.ok(authenticationService.register(request));
        } else {
            throw new Exception("Not applicable in candidates");
        }
    }
}
