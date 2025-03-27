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
import com.techietact.auth.entity.PrivilegeVo;
import com.techietact.auth.models.AccessBo;
import com.techietact.auth.models.PrivilegeBo;
import com.techietact.auth.repository.PrivilegeRepository;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

	@Autowired
	public PrivilegeRepository repository;

	@Override
	public boolean checkDuplicateName(String privilegeName) {
		boolean status = false;
		try {
			PrivilegeVo privilegeVo = repository.findByPrivilegeNameAndIsDelete(privilegeName, false);
			if (privilegeVo != null) {
				status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public PrivilegeBo createPrivilege(PrivilegeBo privilegeBo) {
		try {
			PrivilegeVo privilegeVo = new PrivilegeVo();
			BeanUtils.copyProperties(privilegeBo, privilegeVo);
			Set<AccessVo> setVo = new TreeSet<>(Comparator.comparing(AccessVo::getAccessName));
			Set<AccessBo> setBo = privilegeBo.getAccesses();
			if ((setBo != null) && (!setBo.isEmpty())) {
				for (AccessBo accessBo : setBo) {
					AccessVo accessVo = new AccessVo();
					BeanUtils.copyProperties(accessBo, accessVo);
					setVo.add(accessVo);
				}
				privilegeVo.setAccesses(setVo);
				privilegeVo = repository.save(privilegeVo);
				if (privilegeVo != null) {
					BeanUtils.copyProperties(privilegeVo, privilegeBo);
					return privilegeBo;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Page<PrivilegeBo> listAllPrivileges(int pageIndex, int pageSize, String sortOrder, String searchText) {
		Page<PrivilegeVo> privilegeVo = null;
		Page<PrivilegeBo> privilegeBo = null;
		try {
			Sort sort;
			if (sortOrder != null) {
				if (sortOrder.equals("asc")) {
					sort = Sort.by("privilegeName").ascending();
				} else {
					sort = Sort.by("privilegeName").descending();
				}
			} else {
				sort = Sort.unsorted();
			}

			Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
			if (sortOrder != null && !searchText.isEmpty()) {
				privilegeVo = repository.findAllByPrivilegeNameContainingIgnoreCaseAndIsDelete(searchText, false,
						pageable);
			} else {
				privilegeVo = repository.findAllByIsDelete(false, pageable);
			}
			if (privilegeVo != null) {
				List<PrivilegeVo> privilegeVo1 = privilegeVo.getContent();
				List<PrivilegeBo> privilegeBo1 = new ArrayList<>();
				for (PrivilegeVo privilege : privilegeVo1) {
					PrivilegeBo privilegeBo2 = new PrivilegeBo();
					BeanUtils.copyProperties(privilege, privilegeBo2);
					privilegeBo1.add(privilegeBo2);
				}
				privilegeBo = new PageImpl<PrivilegeBo>(privilegeBo1, pageable, privilegeVo.getTotalElements());
				return privilegeBo;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PrivilegeBo findIdByPrivilege(int privilegeId) {

		try {
			PrivilegeVo privilegeVo = repository.findById(privilegeId).orElse(null);
			if (privilegeVo != null) {
				PrivilegeBo privilegeBo = new PrivilegeBo();
				BeanUtils.copyProperties(privilegeVo, privilegeBo);
				Set<AccessVo> setVo = privilegeVo.getAccesses();
				Set<AccessBo> setBo = new TreeSet<>(Comparator.comparing(AccessBo::getAccessName));
				if ((setVo != null) && (!setVo.isEmpty())) {
					for (AccessVo accessVo : setVo) {
						AccessBo accessBo = new AccessBo();
						BeanUtils.copyProperties(accessVo, accessBo);
						setBo.add(accessBo);
					}
				}
				privilegeBo.setAccesses(setBo);
				return privilegeBo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean updatePrivilege(PrivilegeBo privilegeBo) {
		PrivilegeVo privilegeVo = new PrivilegeVo();
		try {
			privilegeVo = repository.findById(privilegeBo.getPrivilegeId()).orElse(null);
			Set<AccessVo> setVo = new TreeSet<>(Comparator.comparing(AccessVo::getAccessName));
			Set<AccessBo> setBo = privilegeBo.getAccesses();
			if ((setBo != null) && (!setBo.isEmpty())) {
				for (AccessBo accessBo : setBo) {
					AccessVo accessVo = new AccessVo();
					BeanUtils.copyProperties(accessBo, accessVo);
					setVo.add(accessVo);
				}
				privilegeVo.setAccesses(setVo);
				privilegeVo = repository.save(privilegeVo);
				if (privilegeVo != null) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deletePrivilege(int privilegeId) {
		PrivilegeVo privilegeVo = null;
		try {
			privilegeVo = repository.findById(privilegeId).orElse(null);
			privilegeVo.setDelete(true);
			privilegeVo = repository.save(privilegeVo);
			if (privilegeVo != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Set<PrivilegeBo> listAllPrivileges() {

		Set<PrivilegeBo> setBo = new TreeSet<>(Comparator.comparing(PrivilegeBo::getPrivilegeName));

		try {
			Set<PrivilegeVo> setVo = repository.findAllByIsDelete(false);
			if ((setVo != null) && (!setVo.isEmpty())) {
				
				for (PrivilegeVo privilegeVo : setVo) {
					PrivilegeBo privilegeBo = new PrivilegeBo();
					BeanUtils.copyProperties(privilegeVo, privilegeBo);
					setBo.add(privilegeBo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return setBo;

	}

}
