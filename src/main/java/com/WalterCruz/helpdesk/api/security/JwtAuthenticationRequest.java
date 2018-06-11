package com.WalterCruz.helpdesk.api.security;

import java.io.Serializable;

public class JwtAuthenticationRequest implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private String Email;
	private String Password;
	
	
	
	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}


	
	public JwtAuthenticationRequest() {
		super();
    }
	
	public JwtAuthenticationRequest(String email, String passoword) {
		this.setEmail(email);
		this.setPassword(passoword);
	}
	
}
