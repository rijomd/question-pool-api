package com.questions.backend.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.questions.backend.dto.RegisterRequestDTO;
import com.questions.backend.filters.Filter;
import com.questions.backend.filters.FilterType;
import com.questions.backend.filters.PaginationList;

@RestController
@RequestMapping(path = "api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userList")
    public ResponseEntity<PaginationList<Users>> getUser(@RequestParam Map<String, String> params,
            @RequestHeader Map<String, String> headers) throws Exception {

        int page = Integer.valueOf(params.get("page"));
        int size = Integer.valueOf(params.get("limit"));
        List<Filter> filters = new ArrayList<Filter>();

        if (params.get("name") != null) {
            filters.add(new Filter(params.get("name"), FilterType.NAME));
        }
        if (params.get("email") != null) {
            filters.add(new Filter(params.get("email"), FilterType.EMAIL));
        }
        if (params.get("role") != null) {
            filters.add(new Filter(params.get("role"), FilterType.ROLE));
        }
        return ResponseEntity.ok(userService.findAllUsers(page, size, filters));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addJob(@RequestBody RegisterRequestDTO request) {
        try {
            userService.add(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateJob(@RequestBody RegisterRequestDTO request) throws Exception {
        userService.update(request);
        return ResponseEntity.ok().build();
    }

}
