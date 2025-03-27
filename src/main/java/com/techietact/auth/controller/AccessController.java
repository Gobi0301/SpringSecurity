package com.techietact.auth.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techietact.auth.models.AccessBo;
import com.techietact.auth.service.AccessService;

@RestController
@RequestMapping("/access")
public class AccessController {

	@Autowired
	AccessService accessService;

	@GetMapping("/check-duplicate")
	public ResponseEntity<?> isDuplicateAccess(@RequestParam("accessName") String accessName) {
		try {
			if ((accessName != null) && (!accessName.isEmpty())) {
				if (accessService.isDuplicateAccess(accessName)) {
					return ResponseEntity.status(HttpStatus.CONFLICT).body(true);
				} else {
					return ResponseEntity.ok().body(false);
				}
			} else {
				return ResponseEntity.badRequest().body("Access name cannot be null or empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

	@PostMapping("/create")
	public ResponseEntity<?> createAccess(@Validated @RequestBody AccessBo access, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
			} else {
				if (accessService.createAccess(access)) {
					return ResponseEntity.status(HttpStatus.CREATED).body(true);
				} else {
					return ResponseEntity.internalServerError().body("Access creation Failed");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

	@GetMapping("/view/{accessId}")
	public ResponseEntity<?> getAccessByAccessId(@PathVariable("accessId") int accessId) {
		try {
			if (accessId > 0) {
				AccessBo access = accessService.getAccessByAccessId(accessId);
				if (access != null) {
					return ResponseEntity.ok(access);
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Access not found");
				}
			} else {
				return ResponseEntity.badRequest().body("Access id should be greater than zero");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

	@GetMapping("/list/{pageIndex}/{pageSize}")
	public ResponseEntity<?> listAccesses(@PathVariable("pageIndex") int pageIndex,
			@PathVariable("pageSize") int pageSize,
			@RequestParam(name = "sortOrder", required = false) String sortOrder,
			@RequestParam(name = "searchText", required = false) String searchText) {
		try {
			if ((pageIndex >= 0) && (pageSize > 0)) {
				Page<AccessBo> accessPage = accessService.listAccesses(pageIndex, pageSize, sortOrder, searchText);
				if (accessPage != null) {
					return ResponseEntity.ok(accessPage);
				} else {
					return ResponseEntity.internalServerError().body("Error while fetching set of Accesses");
				}
			} else {
				return ResponseEntity.badRequest().body("Invalid page index or size");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateAccess(@Validated @RequestBody AccessBo access, BindingResult bindingResult) {
		try {
			if ((access != null) && (access.getAccessId() > 0)) {
				if (bindingResult.hasErrors()) {
					return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
				} else {
					if (accessService.updateAccess(access)) {
						return ResponseEntity.ok().body(true);
					} else {
						return ResponseEntity.internalServerError().body("Access Updation Failed");
					}
				}
			} else {
				return ResponseEntity.badRequest().body("Access id should be greater than zero");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
		}
	}

	@DeleteMapping("delete/{accessId}")
	public ResponseEntity<?> deleteAccess(@PathVariable("accessId") int accessId) {
		try {
			if (accessId > 0) {
				if (accessService.deleteAccess(accessId)) {
					return ResponseEntity.ok().body(true);
				} else {
					return ResponseEntity.internalServerError().body("Access deletion failed");
				}
			} else {
				return ResponseEntity.badRequest().body("Access id should be greater than zero");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
		}

	}
	
	@GetMapping("list-all")
	public ResponseEntity<?> getAllAccesses(){
		try {
				Set<AccessBo> accessPage = accessService.listAllAccesses();
				if (accessPage != null) {
					return ResponseEntity.ok(accessPage);
				} else {
					return ResponseEntity.internalServerError().body("Error while fetching set of Accesses");
				}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

}
