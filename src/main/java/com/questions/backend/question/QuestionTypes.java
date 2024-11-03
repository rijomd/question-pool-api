package com.questions.backend.question;

public enum QuestionTypes {
    MCQ, CODING, NONE;

    public static QuestionTypes fromString(String type) {
        try {
            return QuestionTypes.valueOf(type);
        } catch (NullPointerException exception) {
            return QuestionTypes.NONE;
        }
    }

}
