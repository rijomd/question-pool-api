package com.questions.backend.question;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.questions.backend.dto.AnswerDto;
import com.questions.backend.dto.PoolDto;
import com.questions.backend.dto.QuestionsDTO;
import com.questions.backend.filters.Filter;
import com.questions.backend.filters.FilterType;
import com.questions.backend.filters.PaginationList;

@RestController
@RequestMapping(path = "api/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    public static class BulkInsertRequest {
        private List<Questions> questions;

        public List<Questions> getQuestions() {
            return questions;
        }

        public void setQuestions(List<Questions> questions) {
            this.questions = questions;
        }

    }

    @GetMapping("/list")
    public ResponseEntity<PaginationList<QuestionsDTO>> getQuestionList(@RequestParam Map<String, String> params,
            @RequestHeader Map<String, String> headers) throws Exception {

        int page = Integer.valueOf(params.get("page"));
        int size = Integer.valueOf(params.get("limit"));
        List<Filter> filters = new ArrayList<Filter>();
        String[] values = null;

        if (params.get("job_id") != null) {
            filters.add(new Filter(Integer.valueOf(params.get("job_id")), FilterType.JOB_ID));
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
        if (params.get("id_list") != null) {
            String decodedIdList = URLDecoder.decode(params.get("id_list"), StandardCharsets.UTF_8);
            values = decodedIdList.split(",");
        }
        return ResponseEntity.ok(questionService.findAllQuestions(page, size, filters, values));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addQuestions(@RequestBody QuestionsDTO request,
            @RequestHeader Map<String, String> headers)
            throws JsonProcessingException {
        questionService.insert(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateQuestions(@RequestBody QuestionsDTO request) throws Exception {
        questionService.update(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/poolList")
    public ResponseEntity<PaginationList<PoolDto>> getQuestionPoolList(@RequestParam Map<String, String> params,
            @RequestHeader Map<String, String> headers) throws Exception {
        int page = Integer.valueOf(params.get("page"));
        int size = Integer.valueOf(params.get("limit"));
        return ResponseEntity.ok(questionService.findAllPools(params, page, size));
    }

    @PostMapping("/add-pool")
    public ResponseEntity<QuestionPool> addQuestionsToPool(
            @RequestBody PoolDto dto) {
        QuestionPool updatedPool = questionService.addQuestionsToPool(dto);
        return ResponseEntity.ok(updatedPool);
    }

    @PostMapping("/bulk-insert")
    public String bulkInsert(@RequestBody BulkInsertRequest request) {
        questionService.bulkInsertQuestions(request.getQuestions());
        return "Bulk insert successful!";
    }

    @MutationMapping
    public String submitAnswers(@Argument List<AnswerDto> answers, @Argument String userName) {
        if (userName != null) {
            if (userName != null && !userName.isEmpty()) {
                questionService.calculateScore(userName, answers);
            }
        }
        return "completed";
    }

}
