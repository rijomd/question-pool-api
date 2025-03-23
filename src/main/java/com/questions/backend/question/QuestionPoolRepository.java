package com.questions.backend.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuestionPoolRepository
                extends JpaRepository<QuestionPool, Long>, JpaSpecificationExecutor<QuestionPool> {

}
