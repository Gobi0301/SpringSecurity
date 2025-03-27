package com.techietact.auth.service;

import java.util.Set;

import org.springframework.data.domain.Page;

import com.techietact.auth.models.AccessBo;


public interface AccessService {

	boolean isDuplicateAccess(String accessName);

	boolean createAccess(AccessBo access);
	
	AccessBo getAccessByAccessId(int accessId);
	
	Page<AccessBo> listAccesses(int pageIndex,int pageSize,String sortOrder,String searchText);
	
	boolean updateAccess(AccessBo access);
	
	boolean deleteAccess(int accessId);

	Set<AccessBo> listAllAccesses();
	
}
