package com.WalterCruz.helpdesk.api.security;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JWTTokenUtil implements Serializable{


	private static final long serialVersionUID = 1L;
	
	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_CREATED = "created";
	static final String CLAIM_KEY_EXPIRED = "exp";
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String getUsernameFromToken(String token) {// ele irá obter o email que está no token (no caso, o user é o email )
		String username = "";
		try {
		final Claims claims = null;
			username = claims.getSubject();
		}catch (Exception e) {
			username = null;
		}
		return username;
	}
	
	public Date getExpirationDateFromToken (String token) {//irá retornar a data de expiração do Token
		Date expiration;
		try {
			final Claims claims = null; //getClaimsFromToken(	
			expiration = claims.getExpiration();			
		}catch (Exception e) {
			expiration = null;
		}
		return expiration;
		
	}
	
	private Claims getClaimsFromToken(String token) { //verifica se o token está expirado
		Claims claims;
		try {
			claims = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
		}catch (Exception e) {
			claims = null;
			}
		return claims;		
			
		}
	
	
	private Boolean isTokenExpired (String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());		
		
	}
	
	public String generateToken(UserDetails userDetails) {//responsável gerar token
		Map<String, Object> claims = new HashMap<>();
		
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		
		final Date createDate = new Date();
		claims.put(CLAIM_KEY_CREATED, createDate);
		
		return doGenerateToken(claims);
		
	}
	
	
	private String doGenerateToken(Map<String, Object>claims) {
		final Date createdDate = (Date) claims.get(CLAIM_KEY_CREATED);
		final Date expirationDate = new Date(createdDate.getTime() + expiration *1000);
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
		
	}
	
	public Boolean canTokenBeRefreshed(String token) {// verifica se o token pode ser atualizado
		return(!isTokenExpired(token));
	}
	
	public String refreshToken(String token) {//Atualiza o Token
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = doGenerateToken(claims);
		}catch(Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;		
		
		
	}
	
	
	public boolean validateToken(String token, UserDetails userDetails) {//Verifica se o Token é valido
		JwtUser user = (JwtUser) userDetails;
		final String username = getUsernameFromToken(token);
		return (
				username.equals(user.getUsername())
				&& !isTokenExpired(token));
	}
		
		
		
}
	
	