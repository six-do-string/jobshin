package com.est.jobshin.domain.user.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private User.Language language;
	@Enumerated(EnumType.STRING)
	private User.Level level;

	public enum Language {
		JAVA, PYTHON
	}

	public enum Level {
		LV1, LV2, LV3
	}
}


