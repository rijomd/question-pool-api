package com.questions.backend.sequenceTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NameRepository {

    @Autowired
    private JdbcTemplate template;

    public int insert() {
        try {
            var sql = """
                    insert into names( fname, lname )
                     values (?,?)
                         """;
            return template.update(sql, "sample", "new");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    // sequence for auto increment of PK
    // DROP SEQUENCE public.names_seq CASCADE;
    // CREATE SEQUENCE IF NOT EXISTS public.names_seq
    // INCREMENT 1
    // START 1
    // MINVALUE 1
    // MAXVALUE 2147483647
    // CACHE 1;
    // ALTER TABLE public.names
    // ALTER COLUMN id SET DEFAULT nextval('public.names_seq')
}
