package com.est.jobshin.domain.interview.domain;

import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;

import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.domain.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "interviews")
@Entity
@Builder
public class Interview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@NotNull
	private String title;

//	@NotNull
	private Mode mode;

//	@NotNull
	private LocalDateTime createAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@JsonManagedReference
	@OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InterviewDetail> interviewDetails = new ArrayList<>();

	private Interview(String title, LocalDateTime createAt) {
		this.title = title;
		this.createAt = createAt;
//		this.user = user;
	}

	public static Interview createInterview(String title, LocalDateTime createAt) {
		return new Interview(title, createAt);
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
