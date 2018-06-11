package com.WalterCruz.helpdesk.api.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.WalterCruz.helpdesk.api.Service.UserService;
import com.WalterCruz.helpdesk.api.entity.User;
import com.WalterCruz.helpdesk.api.model.CurrentUser;
import com.WalterCruz.helpdesk.api.security.JWTTokenUtil;
import com.WalterCruz.helpdesk.api.security.JwtAuthenticationEntryPoint;
import com.WalterCruz.helpdesk.api.security.JwtAuthenticationRequest;

@RestController
@CrossOrigin(origins = "*")//permite acesso qualquer porta
public class AuthenticationRestController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "api/auth")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception{
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						authenticationRequest.getEmail(),
						authenticationRequest.getPassword()	
				)
				
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final User user = userService.findById(authenticationRequest.getEmail());
		user.setPassword(null);
		return ResponseEntity.ok(new CurrentUser(token, user));
			
				
	}
	
	public ResponseEntity<?> refreshAndGetAutehticationToken(HttpServletRequest request){
		String token = request.getHeader("Authorization");
		String username = jwtTokenUtil.getUsernameFromToken(token);
		final User user = userService.findByEmail(username);
		
		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new CurrentUser(refreshedToken, user));
		}else {
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	

}
