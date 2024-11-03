package com.questions.backend.user;

public enum UserStatus {
    ACTIVE, INACTIVE, REJECTED, SELECTED, IN_REVIEW, NONE;

    public static UserStatus fromString(String status) {
        try {
            return UserStatus.valueOf(status);
        } catch (NullPointerException exception) {
            return UserStatus.NONE;
        }
    }

}
