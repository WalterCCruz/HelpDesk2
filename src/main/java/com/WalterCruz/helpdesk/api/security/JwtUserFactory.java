package com.WalterCruz.helpdesk.api.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.WalterCruz.helpdesk.api.entity.User;
import com.WalterCruz.helpdesk.api.enums.ProfileEnum;

public class JwtUserFactory {
	
	private JwtUserFactory() {
		
	}
	
	
	public static JwtUser Create (User user) {//Gera um JWT user com os dados do usuario
		return new JwtUser(
				user.getId(),
				user.getEmail(),
				user.getPassword(),
				mapToGrantedAuthorities(user.getProfile())
	
	);
	}


	private static Collection<? extends GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profileEnum) {//converte os dados do usuario para o formato utilizado para o Spring Security
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));	
		return authorities;
	}

}
