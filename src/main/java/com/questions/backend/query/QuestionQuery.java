package com.questions.backend.query;

import com.questions.backend.dto.QuestionsDTO;
import com.questions.backend.filters.FilterType;
import com.questions.backend.filters.Query;
import com.questions.backend.question.QuestionRepository;

public class QuestionQuery extends Query<QuestionsDTO, QuestionRepository> {

    public QuestionQuery(QuestionRepository userRepo) {
        super(userRepo, "Questions", FilterType.JOB_ID, FilterType.TYPE, FilterType.LEVEL,
                FilterType.QUESTION_NAME, FilterType.ID_LIST);
    }

}