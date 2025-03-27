package com.techietact.auth.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techietact.auth.entity.RoleVo;
import com.techietact.auth.entity.User;
import com.techietact.auth.models.JwtResponse;
import com.techietact.auth.models.Login;
import com.techietact.auth.models.MessageResponse;
import com.techietact.auth.models.Signup;
import com.techietact.auth.repository.RoleRepository;
import com.techietact.auth.repository.UserRepository;
import com.techietact.auth.security.JwtUtils;
import com.techietact.auth.service.UserDetailsImpl;

import jakarta.validation.Valid;
@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody Login login) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		if(userDetails.getIsDelete()) {
			JwtResponse response =new JwtResponse();
			response.setActive(false);
			return ResponseEntity.ok(response);
		}
		String jwt = jwtService.generateJwtToken(authentication);
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody Signup signUpRequest) {
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));
		Set<String> strRoles = signUpRequest.getRole();
		Set<RoleVo> roles = new HashSet<>();
		if (strRoles != null) {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					RoleVo adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				case "employee":
					RoleVo employeeRole = roleRepository.findByRoleName("ROLE_EMPLOYEE")
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			          roles.add(employeeRole);
			         break;			        
				default:
					RoleVo companyRole = roleRepository.findByRoleName("ROLE_COMPANY")
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				          roles.add(companyRole);
				}
				});
		
		user.setRoles(roles);
		user.setDelete(false);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
		
		}else {
			return ResponseEntity.ok(new MessageResponse("Role cannot be empty!"));
		}
	}

	@GetMapping("/validate/{email}")
	public ResponseEntity<?> checkEmailId(@PathVariable("email") String email, User user) throws Exception {
		boolean status = false;
		try {
			status = userRepository.existsByEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(status);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getloggedInUserById(@PathVariable("userId") long userId){
		User user = new User();
		try {	
			user = userRepository.findById(userId).get();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(user);
	
	}
}
