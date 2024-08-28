package com.est.jobshin.domain.levelfeedback.repository;

import com.est.jobshin.domain.levelfeedback.domain.LevelFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelFeedbackRepository extends JpaRepository<LevelFeedback, Long> {

    // 사용자 ID로 피드백을 조회하는 메서드 (예시)
    List<LevelFeedback> findByUserId(Long userId);
}