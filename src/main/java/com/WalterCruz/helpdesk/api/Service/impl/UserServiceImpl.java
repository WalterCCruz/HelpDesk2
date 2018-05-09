package com.WalterCruz.helpdesk.api.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.WalterCruz.helpdesk.api.Service.UserService;
import com.WalterCruz.helpdesk.api.entity.User;
import com.WalterCruz.helpdesk.api.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	
	@Autowired//realizada injecao de dependencia.
	private UserRepository userRepository;

	@Override
	public User findByEmail(String email) {

		return this.userRepository.findByEmail(email);
	}

	@Override
	public User createOrUpdate(User user) {
		
		return this.userRepository.save(user);
	}

	@Override
	public User findById(String id) {
		return this.userRepository.findOne(id);
	}

	@Override
	public void delete(String id) {
		this.userRepository.delete(id);
		
	}

	@Override
	public Page<User> FindAll(int page, int count) {
			
		Pageable pages = new PageRequest(page, count);
		return this.userRepository.findAll(pages);
	}

	
	
}
