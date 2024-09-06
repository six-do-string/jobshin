package com.est.jobshin.domain.interview.domain;

import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;

import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "interviews")
@Entity
public class Interview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Enumerated(EnumType.STRING)
	private Mode mode;

	private LocalDateTime createdAt;

	private boolean complete = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InterviewDetail> interviewDetails = new ArrayList<>();

	private Interview(String title, LocalDateTime createdAt, User user, Mode mode) {
		this.title = title;
		this.createdAt = createdAt;
		this.user = user;
		this.mode = mode;
	}

	public static Interview createInterview(String title, LocalDateTime createdAt, User user, Mode mode) {
		return new Interview(title, createdAt, user, mode);
	}

	/**
	 * 면접 상태 완료로 변경
	 */
	public void completeInterview() {
		this.complete = true;
	}

	public void addInterviewDetails(InterviewDetail interview){
		interviewDetails.add(interview);
		interview.setInterview(this);
	}

	public void removeInterviewDetails(InterviewDetail interview){
		interviewDetails.clear();
		interview.setInterview(null);
	}
}
