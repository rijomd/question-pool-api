package com.questions.backend.question;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.questions.backend.dto.AnswerDto;
import com.questions.backend.dto.PoolDto;
import com.questions.backend.dto.QuestionsDTO;
import com.questions.backend.filters.Filter;
import com.questions.backend.filters.PaginationList;
import com.questions.backend.specification.PoolSpecification;
import com.questions.backend.user.UserService;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionPoolRepository questionPoolRepository;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private UserService userService;

    public PaginationList<QuestionsDTO> findAllQuestions(int page, int limit, List<Filter> filters, String[] values) {
        try {
            if (Objects.nonNull(values) && values.length > 0) {
                return questionRepository.query().findByIds(values)
                        .findAllPagination(limit * (page - 1), limit);
            } else {
                return questionRepository.query().filters(filters).findAllPagination(limit * (page - 1), limit);
            }
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

    public PaginationList<PoolDto> findAllPools(Map<String, String> params, int page, int size) throws Exception {
        Specification<QuestionPool> spec = PoolSpecification.buildSpecification(params);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<QuestionPool> listAll = questionPoolRepository.findAll(spec, pageable);
        List<QuestionPool> pool = listAll.getContent();

        List<PoolDto> poolDtos = pool.stream()
                .map(questionPool -> new PoolDto(questionPool))
                .collect(Collectors.toList());
        long totalElements = listAll.getTotalElements();

        if (totalElements > Integer.MAX_VALUE) {
            throw new Exception("Total elements exceed Integer.MAX_VALUE");
        }
        return new PaginationList<PoolDto>(poolDtos, (int) totalElements);
    }

    public QuestionPool addQuestionsToPool(PoolDto dto) {
        try {
            QuestionPool pool = new QuestionPool();
            pool.setPool_name(dto.getQuestion_name());
            pool.setQuestions(dto.getQuestionList());

            if (dto.getId() == null) {
                return questionPoolRepository.save(pool);
            } else {
                Optional<QuestionPool> questionPool = questionPoolRepository.findById(dto.getId());
                if (questionPool.isEmpty()) {
                    return questionPoolRepository.save(pool);
                } else {
                    QuestionPool question_Pool = questionPool.get();
                    question_Pool.setQuestions(dto.getQuestionList());
                    question_Pool.setPool_name(dto.getQuestion_name());
                    return questionPoolRepository.save(question_Pool);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }

    }

    public void bulkInsertQuestions(List<Questions> questions) {
        try {
            questionRepo.saveAll(questions);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void calculateScore(String userEmail, List<AnswerDto> answerDto) {
        Integer id = userService.getUserDetails(userEmail);
        var score = 0;
        // max 20 qeustions allowed else need to change logic
        for (AnswerDto answer : answerDto) {
            Optional<Questions> QUestionOptional = questionRepo
                    .findById((long) answer.getId());
            if (QUestionOptional.isPresent()) {
                Questions questionPool = QUestionOptional.get();
                if (questionPool.getAnswerOption() != null
                        && answer.getAnswer().toLowerCase()
                                .equals(questionPool.getAnswerOption().toLowerCase())) {
                    score++;
                }
            }
        }
        userService.updateScore(id, score);
    }
}
