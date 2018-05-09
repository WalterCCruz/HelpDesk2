package com.WalterCruz.helpdesk.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.WalterCruz.helpdesk.api.entity.Ticket;



//Este Repository é uma interface que realiza o mapeamento/complemento das minhas entidades.

//														Minha entidade, Meu tipo de Id(no caso String)
public interface TicketRepository extends MongoRepository<Ticket, String> {

	Page<Ticket> findByUserIdOrderByDateDesc (Pageable pages, String userId);
	
	Page<Ticket>FindByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingOrderByDateDesc(
			String title, String status, String Priority, Pageable pages);
	

	Page<Ticket>FindByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingAndUserIdOrderByDateDesc(
			String title, String status, String Priority, Pageable pages);
	
	Page<Ticket>FindByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContaininAndgAssignedUserIdOrderByDateDesc(
			String title, String status, String Priority, Pageable pages);
	
	
	
	Page<Ticket> findByNumber(Integer number, Pageable pages);
	
}
