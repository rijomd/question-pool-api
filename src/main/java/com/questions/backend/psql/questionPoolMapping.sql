CREATE TABLE question_pool_question (
    question_pool_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    PRIMARY KEY (question_pool_id, question_id),
    FOREIGN KEY (question_pool_id) REFERENCES question_pool(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);
