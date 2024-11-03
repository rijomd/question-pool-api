package com.questions.backend.exceptions;

import com.questions.backend.filters.FilterType;

public class MissingFilterImplementationException extends RuntimeException {

    public MissingFilterImplementationException(FilterType type, String label) {
        super("Missing filter : " + type.name() + " on " + label + " query");
    }

}
