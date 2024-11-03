package com.questions.backend.job;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/jobList")
    public ResponseEntity<Optional<List<JobCategory>>> getUser(@RequestParam Map<String, String> params,
            @RequestHeader Map<String, String> headers) throws Exception {

        return ResponseEntity.ok(jobService.findAllJobs());
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addJob(@RequestBody JobCategory request) {
        jobService.add(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateJob(@RequestBody JobCategory request) throws Exception {
        jobService.update(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") int id) throws Exception {
        jobService.delete(id);
        return ResponseEntity.ok().build();
    }

}
