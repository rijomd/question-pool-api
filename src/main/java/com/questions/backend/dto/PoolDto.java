package com.questions.backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.questions.backend.question.QuestionPool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PoolDto {

    private Long id;
    private String question_name;
    private List<Integer> questionList;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public PoolDto(QuestionPool questionPool) {
        this.id = (long) questionPool.getId();
        this.question_name = questionPool.getPool_name();
        this.questionList = questionPool.getQuestions();
        this.createdAt = questionPool.getCreatedDate();
        this.updatedAt = questionPool.getUpdatedDate();
    }

}
