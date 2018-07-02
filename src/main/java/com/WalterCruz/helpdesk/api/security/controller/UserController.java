package com.WalterCruz.helpdesk.api.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.WalterCruz.helpdesk.api.Service.UserService;
import com.WalterCruz.helpdesk.api.entity.User;
import com.WalterCruz.helpdesk.api.response.Response;
import com.mongodb.DuplicateKeyException;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*") // permitir acesso a qualquer porta
public class UserController {

	@Autowired
	private UserService userservice;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")//Qual perfil tera permissao para create
	public ResponseEntity<Response<User>> create(HttpServletRequest request, @RequestBody User user, BindingResult result){
		
		Response<User> response = new Response <User>();
		try {
			validateCreateUser(user,result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User userPersisted = (User) userservice.createOrUpdate(user);
			response.setData(userPersisted);
			
		}catch (DuplicateKeyException dE) {
			response.getErrors().add("E-mail already registred !");
			return ResponseEntity.badRequest().body(response);
		}catch(	Exception e){
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.badRequest().body(response);
	}
	

	private void validateCreateUser(User user, BindingResult result) {
		if(user.getEmail()== null) {
			result.addError(new ObjectError("User", "Email no information"));
		}
		
	}
	
	@PutMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> update(HttpServletRequest request, @RequestBody User user, BindingResult result){
		Response<User> response = new Response <User>();
		return ResponseEntity.ok(response);
		
	}
	
	
	private void validateUpdateUser (User user, BindingResult result) {
		if(user.getId() == null) {
			result.addError(new ObjectError("User", "Id no Information"));
		}
		if(user.getEmail() == null) {
			result.addError(new ObjectError("User", "Email no Information"));
		}
	}
	
}

	
	
	

