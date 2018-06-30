package com.WalterCruz.helpdesk.api.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.WalterCruz.helpdesk.api.entity.ChangeStatus;
import com.WalterCruz.helpdesk.api.entity.Ticket;

@Component
public interface TicketService {
	
	
	Ticket createOrUpdate (Ticket ticket);
	
	Ticket findById(String id);
	
	void  Delete (String id);
	
	Page<Ticket> listTicket (int page, int count);
	
	ChangeStatus createChangeStatus	(ChangeStatus changestatus);
	
	Iterable <ChangeStatus> ListChangeStatus (String TicketId);
	
	Page<Ticket> FindByCurrentUser (int Page, String UserId, int count);
	
	Page<Ticket> FindByParameters (int page, int count, String Title, String Status, String Priority);
	
	Page<Ticket> FindByParametersAndCurrentUser (int page, int count, String title, String status, String priority,String UserId);
	
	Page <Ticket> FindByNumer (int page, int count, Integer number);
	
	Iterable <Ticket> findAll();
	
	Page<Ticket> findByPsrameterAndAssignedUser(int page, String title, String tatus, String priority);
	
	
	
	
}
