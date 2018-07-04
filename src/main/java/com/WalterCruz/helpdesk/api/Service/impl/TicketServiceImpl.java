package com.WalterCruz.helpdesk.api.Service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.WalterCruz.helpdesk.api.Service.TicketService;
import com.WalterCruz.helpdesk.api.entity.ChangeStatus;
import com.WalterCruz.helpdesk.api.entity.Ticket;
import com.WalterCruz.helpdesk.api.repository.ChangeStatusRepository;
import com.WalterCruz.helpdesk.api.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService{
	
	
	@Autowired
	private TicketRepository ticketRepository;
	
	
	@Autowired
	private ChangeStatusRepository changestatusRepository;
	
	

	@Override
	public Ticket createOrUpdate(Ticket ticket) {
	
		return this.ticketRepository.save(ticket);
		
	}

	@Override
	public Ticket findById(String id) {
		return this.ticketRepository.findOne(id);
	}

	@Override
	public void Delete(String id) {
		this.ticketRepository.delete(id);
	}

	@Override
	public Page<Ticket> listTicket(int page, int count) {
		org.springframework.data.domain.Pageable pages = new PageRequest(page, count);
	    return this.ticketRepository.findAll(pages);
	}

	@Override
	public ChangeStatus createChangeStatus(ChangeStatus changestatus) {
		return this.changestatusRepository.save(changestatus);
	}

	@Override
	public Iterable<ChangeStatus> ListChangeStatus(String TicketId) {
		return this.changestatusRepository.findByTicketIdOrderByDateChangeStatusDesc(TicketId);
	}

	@Override
	public Page<Ticket> FindByCurrentUser(int Page, String UserId, int count ) {
		org.springframework.data.domain.Pageable pages = new PageRequest(Page,count);
		return this.ticketRepository.findByUserIdOrderByDateDesc(pages, UserId);
		
	}

	@Override
	public Page<Ticket> FindByParameters(int page, int count, String title, String status, String priority) {
		org.springframework.data.domain.Pageable pages = new PageRequest(page,count);
		return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusAndPriorityOrderByDateDesc(title, status, priority, pages);
				
	}

	@Override
	public Page<Ticket> FindByParametersAndCurrentUser(int page, int count, String title, String status,
			String priority, String UserId) {
		org.springframework.data.domain.Pageable pages = new PageRequest(page,count);
		return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusAndPriorityAndUserIdOrderByDateDesc(title, status, priority, pages);
		
	}

	@Override
	public Page<Ticket> FindByNumer(int page, int count, Integer number) {
		org.springframework.data.domain.Pageable pages = new PageRequest(page, count);
		return this.ticketRepository.findByNumber(number, pages);
	}

	@Override
	public Iterable<Ticket> findAll() {
		return this.ticketRepository.findAll();
	}

	@Override
	public Page<Ticket> findByParameterAndAssignedUser(int page, int count, String title, String status,String priority) {
			org.springframework.data.domain.Pageable pages = new PageRequest(page,count);
			return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusAndPriorityAndAssignedUserIdOrderByDateDesc(title, status, priority, pages);	
	}



	
	
	
	
}
