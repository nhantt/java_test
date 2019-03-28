package com.nals.api.dto.bs;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "WORK")
public class Work {
	@Id
	@Column(name = "ID", length=36)
	private String id;

	@Column(name = "WORK_NAME")
	private String workName;
	
	@Column(name = "STARTING_DATE")
	private LocalDate startingDate;

	@Column(name = "ENDING_DATE")
	private LocalDate endingDate;
	
	@Column(name = "STATUS")
	private Status status;
}
