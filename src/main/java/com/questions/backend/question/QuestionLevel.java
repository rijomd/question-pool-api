package com.questions.backend.question;

public enum QuestionLevel {
    FRESHER, JUNIOR, INTERMEDIATE, SENIOR, SUPER_SENIOR, NONE;

    public static QuestionLevel fromString(String level) {
        try {
            return QuestionLevel.valueOf(level);
        } catch (NullPointerException exception) {
            return QuestionLevel.NONE;
        }
    }
}
