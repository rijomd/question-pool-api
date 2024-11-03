package com.questions.backend.sequenceTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/name")
public class NameController {

    @Autowired
    private NameService nameService;

    @GetMapping("/insert")
    public ResponseEntity<Void> addJob() {
        nameService.insert();
        return ResponseEntity.ok().build();
    }

}
