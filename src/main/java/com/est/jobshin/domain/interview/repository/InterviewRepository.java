package com.est.jobshin.domain.interview.repository;

import com.est.jobshin.domain.interview.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

}
