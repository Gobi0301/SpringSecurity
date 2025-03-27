package com.techietact.auth.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AccessBo {

	private int accessId ;
	
	@NotNull(message = "Access name must not be null")
    @NotEmpty(message = "Access name must not be empty")
	private String accessName ;
	
}
