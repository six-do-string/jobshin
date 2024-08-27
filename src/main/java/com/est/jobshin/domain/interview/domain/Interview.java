package com.est.jobshin.domain.interview.domain;

import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;

import com.est.jobshin.domain.user.domain.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "interview")
@Entity
@Builder
public class Interview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column
	private String title;

	@Column
	private LocalDateTime createAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InterviewDetail> interviewDetails = new ArrayList<>();


	public void addInterviewDetails(InterviewDetail interview){
		interviewDetails.add(interview);
		interview.setInterview(this);
	}

	public void removeInterviewDetails(InterviewDetail interview){
		interviewDetails.clear();
		interview.setInterview(null);
	}
}
