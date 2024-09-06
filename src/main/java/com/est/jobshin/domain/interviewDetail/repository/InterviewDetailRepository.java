package com.est.jobshin.domain.interviewDetail.repository;

import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.user.util.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface InterviewDetailRepository extends JpaRepository<InterviewDetail, Long> {

    List<InterviewDetail> findByInterviewId(Long interviewId);
}
