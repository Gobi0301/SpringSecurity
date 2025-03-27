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
@Table(name="role")
@Data
public class RoleVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="role_id")
	private int roleId ;
	
	@Column(name="role_name")
	private String roleName ;
	
	@Column(name="is_active")
	private boolean isActive ;
	
	@Column(name="is_deleted")
	private boolean isDeleted ;
	
	@ManyToMany
	@JoinTable( name = "role_privilege", joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "privilege_id"))
	private Set<PrivilegeVo> privileges ;
	
}
