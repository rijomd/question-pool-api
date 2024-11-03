package com.questions.backend.user;

public enum Role {
        HR, CANDIDATES, MANAGER, NONE;

        public static Role fromString(String role) {
                try {
                        return Role.valueOf(role);
                } catch (NullPointerException exception) {
                        return Role.NONE;
                }
        }
}
