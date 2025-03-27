package com.techietact.auth.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Data;
@Entity
@Data
@Table(name="privilege")
public class PrivilegeVo {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="privilege_Id")
	private int privilegeId;
	
	@Column(name="privilege_Name")
	private String privilegeName;
	
	private boolean isDelete;
	
	@ManyToMany
	@JoinTable( name = "privilege_access", joinColumns = @JoinColumn(name = "privilege_id"),inverseJoinColumns = @JoinColumn(name = "access_id"))
	private Set<AccessVo> accesses ;
	

}
