package com.questions.backend.user;

import org.springframework.stereotype.Service;

import com.questions.backend.dto.RegisterRequestDTO;
import com.questions.backend.filters.Filter;
import com.questions.backend.filters.PaginationList;
import com.questions.backend.utils.storage.StorageService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StorageService storageService;

    public PaginationList<Users> findAllUsers(int page, int limit, List<Filter> filters) {
        try {
            return userRepository.query().filters(filters).findAllPagination(limit * (page - 1), limit);
        } catch (Exception e) {
            throw e;
        }
    }

    public void add(RegisterRequestDTO request) {
        storageService.store(request.getFile());
        var user = Users.builder()
                .email(request.getEmail())
                .name(request.getName())
                .role(request.getRole())
                .job_id(request.getJob_id())
                .resumeLink(request.getResumeLink())
                .yearOfExp(request.getYearOfExp())
                .status(UserStatus.ACTIVE)
                .password(passwordEncoder.encode(request.getPassword() != null ? request.getPassword() : "user"))
                .build();
        userRepo.save(user);
    }

    public void update(RegisterRequestDTO request) throws Exception {
        Optional<Users> userOptional = userRepo.findById(request.getId());
        if (userOptional.isEmpty()) {
            throw new Exception("user not found");
        }
        Users user = userOptional.get();
        user.setJob_id(request.getJob_id());
        user.setName(request.getName() != null ? request.getName() : user.getName());
        user.setResumeLink(request.getResumeLink() != null ? request.getResumeLink() : user.getResumeLink());
        user.setYearOfExp(request.getYearOfExp() != null ? request.getYearOfExp() : user.getYearOfExp());
        user.setStatus(request.getStatus() != null ? request.getStatus() : user.getStatus());
        userRepo.save(user);
    }

}
