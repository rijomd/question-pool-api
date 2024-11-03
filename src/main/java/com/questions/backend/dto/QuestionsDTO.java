package com.questions.backend.dto;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.questions.backend.question.QuestionLevel;
import com.questions.backend.question.QuestionTypes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionsDTO {
    private Integer id;
    private String question_name;
    private String question_answer;
    private String answerOption;
    private Integer job_id;
    private QuestionTypes type;
    private Map<String, String> mcq_options;
    private Integer created_by;
    private String jobName;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private QuestionLevel level;
    private String created_user;

    @SuppressWarnings("unchecked")
    public Map<String, String> getJsonData(String options) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(options, Map.class);
    }

    public String setJsonData(Map<String, String> keyValueMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(keyValueMap);
    }

}
