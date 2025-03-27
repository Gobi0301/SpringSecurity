package com.techietact.auth.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.techietact.auth.entity.AccessVo;
import com.techietact.auth.models.AccessBo;
import com.techietact.auth.repository.AccessRepository;

@Service
public class AccessServiceImpl implements AccessService {


	@Autowired
	AccessRepository accessRepository;
	
	@Override
	public boolean isDuplicateAccess(String accessName) {
		boolean isDuplicate = false;
		try {
			AccessVo access = accessRepository.findByAccessNameAndIsDeleted(accessName, false);
			if (access != null) {
				isDuplicate = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDuplicate;
	}

	@Override
	public boolean createAccess(AccessBo accessBo) {
		boolean creationStatus = false;
		try {
			if (accessBo != null) {
				AccessVo accessVo = new AccessVo();
				BeanUtils.copyProperties(accessBo, accessVo);
				accessVo = accessRepository.save(accessVo);
				if ((accessVo != null) && accessVo.getAccessId() > 0) {
					creationStatus = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return creationStatus;
	}

	@Override
	public AccessBo getAccessByAccessId(int accessId) {
		AccessBo accessBo = null;
		try {
			AccessVo accessVo = accessRepository.findById(accessId).orElse(null);
			if (accessVo != null) {
				accessBo = new AccessBo();
				BeanUtils.copyProperties(accessVo, accessBo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessBo;
	}

	@Override
	public Page<AccessBo> listAccesses(int pageIndex, int pageSize, String sortOrder, String searchText) {
		Page<AccessBo> pageBo = null;
		try {
			Sort sort;
			if ((sortOrder != null)&&(!sortOrder.isEmpty())) {
				if (sortOrder.equals("asc")) {
					sort = Sort.by("accessName").ascending();
				} else {
					sort = Sort.by("accessName").descending();
				}
			} else {
				sort = Sort.by("accessId").ascending();
			}
			Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
			Page<AccessVo> pageVo = null;
			if ((searchText != null) && (!searchText.isEmpty())) {
				pageVo = accessRepository.findAllByAccessNameContainingAndIsDeleted(searchText, false, pageable);
			} else {
				pageVo = accessRepository.findAllByIsDeleted(false, pageable);
			}
			if (pageVo != null) {
				List<AccessVo> listVo = pageVo.getContent();
				List<AccessBo> listBo = new ArrayList<>();
				for (AccessVo accessVo : listVo) {
					if(accessVo!=null) {
						AccessBo accessBo = new AccessBo();
						BeanUtils.copyProperties(accessVo, accessBo);
						listBo.add(accessBo);
					}				
				}
				pageBo = new PageImpl<AccessBo>(listBo, pageable, pageVo.getTotalElements());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageBo;
	}

	@Override
	public boolean updateAccess(AccessBo accessBo) {
		boolean updationStatus = false;
		try {
			if (accessBo != null) {
				AccessVo accessVo = accessRepository.findById(accessBo.getAccessId()).orElse(null);
				BeanUtils.copyProperties(accessBo, accessVo);
				accessVo = accessRepository.save(accessVo);
				if ((accessVo != null) && accessVo.getAccessId() > 0) {
					updationStatus = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updationStatus;
	}

	@Override
	public boolean deleteAccess(int accessId) {
		boolean softDeletionStatus = false;
		try {
			if (accessId > 0) {
				AccessVo accessVo = accessRepository.findById(accessId).orElse(null);
				accessVo.setDeleted(true);
				accessVo = accessRepository.save(accessVo);
				if ((accessVo != null) && accessVo.getAccessId() > 0 && accessVo.isDeleted()) {
					softDeletionStatus = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return softDeletionStatus;
	}

	@Override
	public Set<AccessBo> listAllAccesses() {
	
		Set<AccessBo> setBo = new TreeSet<>(Comparator.comparing(AccessBo::getAccessName));
		
		try {
			Set<AccessVo> setVo = accessRepository.findAllByIsDeleted(false);
			if((setVo !=null) &&(!setVo.isEmpty())) {
				for(AccessVo accessVo : setVo) {
					AccessBo accessBo = new AccessBo();
					BeanUtils.copyProperties(accessVo, accessBo);
					setBo.add(accessBo);
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		return setBo;
	
	}

}
