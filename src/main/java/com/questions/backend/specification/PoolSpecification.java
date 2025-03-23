package com.questions.backend.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.questions.backend.question.QuestionPool;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PoolSpecification {

    public static Specification<QuestionPool> buildSpecification(Map<String, String> params) {
        return new Specification<QuestionPool>() {

            @Override
            public Predicate toPredicate(Root<QuestionPool> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate predicate = builder.conjunction();

                if (params.containsKey("pool_name")) {
                    String pool_name = params.get("pool_name");
                    predicate = builder.and(predicate,
                            builder.like(root.get("pool_name"), "%" + pool_name + "%"));
                }
                if (params.containsKey("pool_id")) {
                    Integer pool_id = Integer.parseInt(params.get("pool_id"));
                    predicate = builder.and(predicate,
                            builder.equal(root.get("id"), pool_id));
                }
                return predicate;
            }

        };

    }

}
