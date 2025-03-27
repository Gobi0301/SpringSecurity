package com.techietact.auth.service;

import java.util.Set;

import org.springframework.data.domain.Page;

import com.techietact.auth.models.PrivilegeBo;

public interface PrivilegeService {

	boolean checkDuplicateName(String privilegeName);

	PrivilegeBo createPrivilege(PrivilegeBo privilegeBo);

	Page<PrivilegeBo> listAllPrivileges(int pageIndex, int pageSize, String sortOrder, String searchText);

	PrivilegeBo findIdByPrivilege(int privilegeId);

	boolean updatePrivilege(PrivilegeBo privilegeBo);

	boolean deletePrivilege(int privilegeId);
	
	Set<PrivilegeBo> listAllPrivileges();
	
	
	
	
	
	
	
	
	
	

}
