package com.est.jobshin.domain.interview.repository;

import com.est.jobshin.domain.interview.domain.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    @Query("SELECT i FROM Interview i WHERE (i.mode = com.est.jobshin.domain.interviewDetail.util.Mode.PRACTICE) AND i.user.id = :userId")
    Page<Interview> findInterviewsWithPracticeModeByUser(@Param("userId") Long userId,
            Pageable pageable);
}
