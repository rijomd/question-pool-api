package com.questions.backend.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobRepository extends JpaRepository<JobCategory, Integer>, JpaSpecificationExecutor<JobCategory> {

}