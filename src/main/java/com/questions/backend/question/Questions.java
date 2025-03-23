package com.questions.backend.question;

import java.time.LocalDate;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
@ToString
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    private String question_name;
    private String question_answer;
    private String answerOption;

    @Column(columnDefinition = "TEXT")
    private String mcq_options;

    // fk
    private Integer job_id;
    private Integer created_by;

    @Enumerated(EnumType.STRING)
    private QuestionTypes type;

    @Enumerated(EnumType.STRING)
    private QuestionLevel level;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    public void setJsonData(Map<String, String> keyValueMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.mcq_options = objectMapper.writeValueAsString(keyValueMap);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getJsonData() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(mcq_options, Map.class);
    }
}
