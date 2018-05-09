package com.WalterCruz.helpdesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.WalterCruz.helpdesk.api.entity.User;

//Este Repository é uma interface que realiza o mapeamento/complemento das minhas entidades.

//														Minha entidade, Meu tipo de Id(no caso String)
public interface UserRepository extends MongoRepository<User, String>{

	User findByEmail(String email);


	
	
}
