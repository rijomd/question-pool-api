package com.questions.backend.user;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            e.printStackTrace();
            throw e;
        }
    }

    public void add(RegisterRequestDTO request) {
        try {
            MultipartFile file = null;
            if (request.getFile() != null) {
                storageService.store(file);
            }
            var user = Users.builder()
                    .email(request.getEmail())
                    .name(request.getName())
                    .role(request.getRole())
                    .job_id(request.getJob_id())
                    .question_pool_id(request.getQuestion_pool_id())
                    .resumeLink(file != null ? file.getOriginalFilename() : "")
                    .yearOfExp(request.getYearOfExp())
                    .status(UserStatus.ACTIVE)
                    .password(passwordEncoder.encode(request.getPassword() != null ? request.getPassword() : "user"))
                    .build();
            userRepo.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void update(RegisterRequestDTO request) throws Exception {
        Optional<Users> userOptional = userRepo.findById(request.getId());
        if (userOptional.isEmpty()) {
            throw new Exception("user not found");
        }
        Users user = userOptional.get();
        MultipartFile file = null;
        if (request.getFile() != null) {
            storageService.store(file);
            if (user.getResumeLink().equals(request.getFile().getOriginalFilename())) {
                storageService.store(file);
            }
        }
        user.setJob_id(request.getJob_id() != null ? request.getJob_id() : user.getJob_id());
        user.setName(request.getName() != null ? request.getName() : user.getName());
        user.setResumeLink(file != null ? file.getOriginalFilename() : "");
        user.setYearOfExp(request.getYearOfExp() != null ? request.getYearOfExp() : user.getYearOfExp());
        user.setStatus(request.getStatus() != null ? request.getStatus() : user.getStatus());
        user.setQuestion_pool_id(
                request.getQuestion_pool_id() != null ? request.getQuestion_pool_id() : user.getQuestion_pool_id());
        userRepo.save(user);
    }

    public Integer getUserDetails(String username) {
        try {
            Optional<Users> userOptional = userRepo.findByEmail(username);
            if (userOptional.isPresent()) {
                Users user = userOptional.get();
                return user.getId();
            }
            return 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public void updateScore(Integer userId, Integer score) {
        Optional<Users> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            if (user.getStatus() == UserStatus.ACTIVE) {
                user.setScore(score);
                user.setStatus(UserStatus.IN_REVIEW);
                userRepo.save(user);
            } else {
                throw new RuntimeException("Only once a time can apply");
            }
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }

    }

}
