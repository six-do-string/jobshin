package com.est.jobshin.domain.interviewDetail.repository;

import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InterviewDetailRepository extends JpaRepository<InterviewDetail, Long> {

    List<InterviewDetail> findByInterviewId(Long interviewId);
}
