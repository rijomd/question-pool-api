package com.questions.backend.job;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Optional<List<JobCategory>> findAllJobs() {
        return Optional.of(jobRepository.findAll());
    }

    public void add(JobCategory request) {
        var job = JobCategory.builder()
                .job_name(request.getJob_name())
                .description(request.getDescription())
                .status(JobStatus.ACTIVE)
                .build();
        jobRepository.save(job);
    }

    public void update(JobCategory request) throws Exception {
        Optional<JobCategory> jobOptional = jobRepository.findById(request.getId());
        if (jobOptional.isEmpty()) {
            throw new Exception("job not found");
        }
        JobCategory job = jobOptional.get();
        job.setJob_name(request.getJob_name() != null ? request.getJob_name() : job.getJob_name());
        job.setDescription(request.getDescription() != null ? request.getDescription() : job.getDescription());
        job.setStatus(request.getStatus() != null ? request.getStatus() : job.getStatus());
        jobRepository.save(job);
    }

    public void delete(int id) {
        jobRepository.deleteById(id);
    }

}
