package com.questions.backend.question;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.questions.backend.dto.QuestionsDTO;
import com.questions.backend.filters.Filter;
import com.questions.backend.filters.FilterType;
import com.questions.backend.filters.PaginationList;
import com.questions.backend.query.QuestionQuery;
import com.questions.backend.utils.FilterUtill;

import jakarta.annotation.PostConstruct;

@Repository
public class QuestionRepository implements com.questions.backend.filters.Repository<QuestionsDTO> {

    private Map<FilterType, String> filterMap = new HashMap<>();
    private Map<FilterType, String> likeMap = new HashMap<>(); // LIKE search

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private FilterUtill filterUtil;

    @PostConstruct
    public void init() {
        filterMap.put(FilterType.TYPE, "type = ?");
        filterMap.put(FilterType.LEVEL, "level = ?");
        filterMap.put(FilterType.QUESTION_NAME, "question_name ILIKE ?");
        filterMap.put(FilterType.JOB_ID, "CAST(qs.job_id AS INTEGER) = ?");
        filterMap.put(FilterType.CREATED_BY, "created_by = ?");
        filterMap.put(FilterType.ID_LIST, "CAST(qs.id AS INTEGER) IN (?)");

        likeMap.put(FilterType.QUESTION_NAME, "");
    }

    public QuestionQuery query() {
        return new QuestionQuery(this);
    }

    @Override
    public PaginationList<QuestionsDTO> findByFilters(List<Filter> filters, int offset, int limit) {

        try {
            String sql = """
                    	SELECT qs.id, qs.question_name, us.id as user_id,qs.question_answer,qs.type, qs.level,qs.created_at, qs.updated_at, qs.job_id, qs.mcq_options , us.name,
                    qs.answer_option, jb.job_name from questions qs LEFT JOIN job_category jb ON qs.job_id=jb.id
                    LEFT JOIN users us ON qs.created_by=us.id
                    """;
            String countSql = "SELECT COUNT(*) FROM questions";
            FilterUtill.FilterUtilDTO dto = new FilterUtill().new FilterUtilDTO();
            dto.setFilters(filters);
            dto.setLimit(limit);
            dto.setOffset(offset);
            dto.setFilterMap(filterMap);
            dto.setLikeMap(this.likeMap);
            dto.setRequirePrefix(true);

            sql = sql + filterUtil.generateWhereClause(dto);
            if (filters.size() > 0 && filters.get(0).getFilterType() != FilterType.ID_LIST) {
                countSql = countSql + filterUtil.generateWhereClause(filterMap, filters, -1, -1);
            }
            var list = template.query(sql,
                    ps -> {
                        filterUtil.injectPreparedStatementValues(dto, ps);
                    },
                    resultSet -> {
                        List<QuestionsDTO> questionList = new ArrayList<>();
                        while (resultSet.next()) {
                            QuestionsDTO question = new QuestionsDTO();
                            question.setId(resultSet.getInt("id"));
                            question.setJob_id(resultSet.getInt("job_id"));
                            question.setJobName(resultSet.getString("job_name"));
                            question.setCreated_user(resultSet.getString("name"));
                            question.setQuestion_name(resultSet.getString("question_name"));
                            question.setQuestion_answer(resultSet.getString("question_answer"));
                            question.setAnswerOption(resultSet.getString("answer_option"));
                            question.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
                            question.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
                            question.setCreated_by(resultSet.getInt("user_id"));
                            question.setJobName(resultSet.getString("job_name"));
                            question.setType(QuestionTypes.fromString(resultSet.getString("type")));
                            question.setLevel(QuestionLevel.fromString(resultSet.getString("level")));

                            try {
                                question.setMcq_options(question.getJsonData(resultSet.getString("mcq_options")));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                            questionList.add(question);
                        }
                        return questionList;
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
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean deleteByFilters(List<Filter> filters) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteByFilters'");
    }

    public int insert(QuestionsDTO questions) throws JsonProcessingException {
        String options = questions.setJsonData(questions.getMcq_options());
        try {
            var sql = """
                    INSERT INTO questions(answer_option,created_by,job_id,
                    question_answer,question_name,\"mcq_options\",type,created_at,updated_at,level)
                     VALUES (?,?,?,?,?,?,?,?,?,?)
                         """;

            int inserted = template.update(sql, questions.getAnswerOption(), questions.getCreated_by(),
                    questions.getJob_id(), questions.getQuestion_answer(), questions.getQuestion_name(),
                    options, questions.getType().name(), LocalDate.now(), LocalDate.now(), questions.getLevel().name());
            return inserted;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public int update(QuestionsDTO questions) throws JsonProcessingException {
        String options = questions.setJsonData(questions.getMcq_options());
        try {
            var sql = """
                    UPDATE questions SET answer_option=?,job_id=?,
                    question_answer=?,question_name=?,\"mcq_options\"=?,updated_at=?,level=? WHERE id = ?
                         """;

            int inserted = template.update(sql, questions.getAnswerOption(),
                    questions.getJob_id(), questions.getQuestion_answer(), questions.getQuestion_name(),
                    options, LocalDate.now(), questions.getLevel().name(), questions.getId());

            return inserted;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

}
