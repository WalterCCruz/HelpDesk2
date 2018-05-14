package com.WalterCruz.helpdesk.api.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.WalterCruz.helpdesk.api.entity.Ticket;
import org.springframework.data.domain.Pageable;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    Page<Ticket> findByUserIdOrderByDateDesc(Pageable pages, String userId);

    Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityOrderByDateDesc(
            String title, String status, String priority, Pageable pages);

    Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityAndUserIdOrderByDateDesc(
            String title, String status, String priority, Pageable pages);
    

    Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityAndAssignedUserIdOrderByDateDesc(
    		String title, String status, String priority,Pageable pages); 
    

    Page<Ticket> findByNumber(Integer number, Pageable pages);


	
}
