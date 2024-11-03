package com.questions.backend.query;

import com.questions.backend.filters.FilterType;
import com.questions.backend.filters.Query;
import com.questions.backend.user.UserRepo;
import com.questions.backend.user.Users;

public class UserQuery extends Query<Users, UserRepo> {

    public UserQuery(UserRepo userRepo) {
        super(userRepo, "User", FilterType.NAME, FilterType.EMAIL, FilterType.ROLE);
    }

}
