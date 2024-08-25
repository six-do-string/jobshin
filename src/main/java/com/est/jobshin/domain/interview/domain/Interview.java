package com.est.jobshin.domain.interview.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Interview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

}
