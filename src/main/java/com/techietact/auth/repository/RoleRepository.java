package com.techietact.auth.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techietact.auth.entity.RoleVo;

@Repository
public interface RoleRepository extends JpaRepository<RoleVo, Integer> {
	
	RoleVo findByRoleNameIgnoreCaseAndIsDeleted (String roleName,boolean isDeleted); 

	Page<RoleVo> findAllByIsDeleted(boolean isDeleted, Pageable pageable);
	
	Page<RoleVo> findAllByRoleNameContainingIgnoreCaseAndIsDeleted(String searchtext, boolean isDeleted, Pageable pageable);

	Optional<RoleVo> findByRoleName(String role);

	
	
}
