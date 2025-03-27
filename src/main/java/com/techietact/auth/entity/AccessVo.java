package com.techietact.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name="access")
@Data
public class AccessVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="access_id")
	private int accessId ;
	
	@Column(name="access_name")
	private String accessName ;

	@Column(name="is_deleted")
	private boolean isDeleted ;
}
