package com.techietact.auth.repository;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techietact.auth.entity.AccessVo;

@Repository
public interface AccessRepository extends JpaRepository<AccessVo, Integer> {
	
	AccessVo findByAccessNameAndIsDeleted (String accessName,boolean isDeleted); 

	Page<AccessVo> findAllByIsDeleted(boolean isDeleted, Pageable pageable);
	
	Page<AccessVo> findAllByAccessNameContainingAndIsDeleted(String searchtext, boolean isDeleted, Pageable pageable);
	
	Set<AccessVo> findAllByIsDeleted(boolean isDeleted);
}
