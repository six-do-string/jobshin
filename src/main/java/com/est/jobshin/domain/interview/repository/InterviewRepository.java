package com.est.jobshin.domain.interview.repository;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.PracticeInterviewHistorySummaryResponse;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    @Query("SELECT i FROM Interview i JOIN FETCH i.interviewDetails d WHERE i.mode = :mode  AND i.user.id = :userId")
    Page<Interview> findInterviewsWithPracticeModeByUser(@Param("userId") Long userId
            , @Param("mode") Mode mode, Pageable pageable);
}
