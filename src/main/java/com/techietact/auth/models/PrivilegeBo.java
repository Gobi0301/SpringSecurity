package com.techietact.auth.models;

import java.util.Set;

import lombok.Data;

@Data
public class PrivilegeBo {
	
	
	private int privilegeId;
	
	private String privilegeName;
	

	private Set<AccessBo> accesses;
	
	

}
