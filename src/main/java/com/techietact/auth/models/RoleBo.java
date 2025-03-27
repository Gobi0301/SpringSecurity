package com.techietact.auth.models;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RoleBo {

	private int roleId ;
	
	@NotNull(message = "Role name must not be null")
    @NotEmpty(message = "Role name must not be empty")
	private String roleName ;
	
	private boolean isActive ;
	
	private Set<PrivilegeBo> privileges;
	
}
