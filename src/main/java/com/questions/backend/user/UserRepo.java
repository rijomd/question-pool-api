package com.questions.backend.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.questions.backend.filters.Filter;
import com.questions.backend.filters.FilterType;
import com.questions.backend.filters.PaginationList;
import com.questions.backend.query.UserQuery;
import com.questions.backend.utils.FilterUtill;

import jakarta.annotation.PostConstruct;

@Repository
public class UserRepo implements com.questions.backend.filters.Repository<Users> {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private FilterUtill filterUtil;

    Map<FilterType, String> filterMap = new HashMap<>();

    public UserQuery query() {
        return new UserQuery(this);
    }

    @PostConstruct
    public void init() {
        // filterMap.put(FilterType.START_DATE, "created_date >= ?");
        // filterMap.put(FilterType.REPORT_ID, "cast(er.id as varchar) = ?");
        filterMap.put(FilterType.NAME, "name = ?");
        filterMap.put(FilterType.EMAIL, "email = ?");
        filterMap.put(FilterType.ROLE, "role = ?");
    }

    @Override
    public PaginationList<Users> findByFilters(List<Filter> filters, int offset, int limit) {
        try {
            String sql = "select * from users";
            String countSql = "SELECT COUNT(*) FROM users";
            sql = sql + filterUtil.generateWhereClause(filterMap, filters, offset, limit);
            if (filters.size() > 0) {
                countSql = countSql + filterUtil.generateWhereClause(filterMap, filters, -1, -1);
            }
            var list = template.query(sql,
                    ps -> {
                        filterUtil.injectPreparedStatementValues(filters, offset, limit, ps);
                    },
                    resultSet -> {
                        List<Users> userList = new ArrayList<>();
                        while (resultSet.next()) {
                            Users user = new Users();
                            user.setId(resultSet.getInt("id"));
                            user.setJob_id(resultSet.getInt("job_id"));
                            user.setName(resultSet.getString("name"));
                            user.setYearOfExp(resultSet.getString("year_of_exp"));
                            user.setResumeLink(resultSet.getString("resume_link"));
                            user.setEmail(resultSet.getString("email"));
                            user.setCreatedAt(resultSet.getTimestamp("created_at"));
                            user.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                            user.setRole(Role.fromString(resultSet.getString("role")));
                            user.setStatus(UserStatus.fromString(resultSet.getString("status")));
                            userList.add(user);
                        }
                        return userList;
                    });

            int total = template.query(countSql,
                    ps -> {
                        filterUtil.injectPreparedStatementValues(filters, -1, -1, ps);
                    },
                    resultSet -> {
                        int result = 0;
                        while (resultSet.next()) {
                            result = resultSet.getInt("count");
                        }
                        return result;
                    });
            return new PaginationList<>(list, total);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }

    }

    @Override
    public boolean deleteByFilters(List<Filter> filters) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteByFilters'");
    }

}
