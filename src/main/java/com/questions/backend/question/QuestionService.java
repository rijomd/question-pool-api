package com.questions.backend.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.questions.backend.dto.QuestionsDTO;
import com.questions.backend.filters.Filter;
import com.questions.backend.filters.PaginationList;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public PaginationList<QuestionsDTO> findAllQuestions(int page, int limit, List<Filter> filters) {
        try {
            return questionRepository.query().filters(filters).findAllPagination(limit * (page - 1), limit);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    public void insert(QuestionsDTO question) throws JsonProcessingException {
        question.setCreated_by(152);
        questionRepository.insert(question);
    }

    public void update(QuestionsDTO question) throws JsonProcessingException {
        questionRepository.update(question);
    }

}
