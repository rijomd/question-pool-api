package com.questions.backend.question;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.questions.backend.dto.QuestionsDTO;
import com.questions.backend.filters.Filter;
import com.questions.backend.filters.FilterType;
import com.questions.backend.filters.PaginationList;

@RestController
@RequestMapping(path="api/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/list")
    public ResponseEntity<PaginationList<QuestionsDTO>> getUser(@RequestParam Map<String, String> params,
            @RequestHeader Map<String, String> headers) throws Exception {

        int page = Integer.valueOf(params.get("page"));
        int size = Integer.valueOf(params.get("limit"));
        List<Filter> filters = new ArrayList<Filter>();

        if (params.get("job_id") != null) {
            filters.add(new Filter(params.get("job_id"), FilterType.JOB_ID));
        }
        if (params.get("type") != null) {
            filters.add(new Filter(params.get("type"), FilterType.TYPE));
        }
        if (params.get("level") != null) {
            filters.add(new Filter(params.get("level"), FilterType.LEVEL));
        }
        if (params.get("question_name") != null) {
            filters.add(new Filter(params.get("question_name"), FilterType.QUESTION_NAME));
        }
        return ResponseEntity.ok(questionService.findAllQuestions(page, size, filters));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addJob(@RequestBody QuestionsDTO request, @RequestHeader Map<String, String> headers)
            throws JsonProcessingException {
        questionService.insert(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateJob(@RequestBody QuestionsDTO request) throws Exception {
        questionService.update(request);
        return ResponseEntity.ok().build();
    }

}
