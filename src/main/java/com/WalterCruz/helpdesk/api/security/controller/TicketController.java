package com.WalterCruz.helpdesk.api.security.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.WalterCruz.helpdesk.api.Service.TicketService;
import com.WalterCruz.helpdesk.api.Service.UserService;
import com.WalterCruz.helpdesk.api.entity.ChangeStatus;
import com.WalterCruz.helpdesk.api.entity.Ticket;
import com.WalterCruz.helpdesk.api.entity.User;
import com.WalterCruz.helpdesk.api.enums.StatusEnum;
import com.WalterCruz.helpdesk.api.response.Response;
import com.WalterCruz.helpdesk.api.security.JWTTokenUtil;



@RestController
@RequestMapping("api/ticket")
@CrossOrigin(origins = "*")
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@Autowired
	protected JWTTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	@PostMapping
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> createOrUpdate(HttpServletRequest request, @RequestBody Ticket ticket,
			BindingResult result) {
		Response<Ticket> response = new Response<Ticket>();
		try {
			validateCreateTicket(ticket, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			ticket.setStatus(StatusEnum.getStatus("New"));
			ticket.setUser(userFormRequest(request));
			ticket.setDate(new Date());
			ticket.setNumber(generateNumber());
			Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketPersisted);

		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

		return ResponseEntity.ok(response);

	}

	private void validateCreateTicket(Ticket ticket, BindingResult result) {
		if (ticket.getTitle() == null) {
			result.addError(new ObjectError("Ticket", "Title no information"));
			return;

		}

	}

	public User userFormRequest(HttpServletRequest request) {
		String token = request.getHeader("Autorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		return userService.findByEmail(email);
	}

	private Integer generateNumber() {
		Random random = new Random();
		return random.nextInt(9999);

	}

	@PutMapping
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> update(HttpServletRequest request, @RequestBody Ticket ticket,
			BindingResult result) {
		Response<Ticket> response = new Response<Ticket>();
		try {
			validateUpdateTicket(ticket, result);
			if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
			
		}
		
			Ticket ticketCurrent = ticketService.findById(ticket.getId());
			ticket.setStatus(ticketCurrent.getStatus());
			ticket.setUser(ticketCurrent.getUser());
			ticket.setDate(ticketCurrent.getDate());
			ticket.setNumber(ticketCurrent.getNumber());
			if (ticketCurrent.getAssignedUser() != null) {
				ticket.setAssignedUser(ticketCurrent.getAssignedUser());
			}
			Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketPersisted);
	     }catch (Exception e) {
	    	 response.getErrors().add(e.getMessage());
	    	 return ResponseEntity.badRequest().body(response);
	     }
		
		return ResponseEntity.ok(response);
	}
	
	

	private void validateUpdateTicket(Ticket ticket, BindingResult result) {
		if (ticket.getId() == null) {
			result.addError(new ObjectError("Ticket", "Id no information"));
			return;

		}

		if (ticket.getTitle() == null) {
			result.addError(new ObjectError("Ticket", "Title no information"));
			return;

		}

	}
	
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Ticket>> findById(@PathVariable ("id") String id) {
		Response<Ticket> response = new Response<Ticket>();
		Ticket ticket = ticketService.findById(id);
		if(ticket == null) {
			response.getErrors().add("Register not found:"+id);
			return ResponseEntity.badRequest().body(response);			
		}
		List<ChangeStatus> changes = new ArrayList<ChangeStatus>();
		Iterable<ChangeStatus> changesCurrent = ticketService.ListChangeStatus(ticket.getId());
		for(Iterator<ChangeStatus>iterator = changesCurrent.iterator();iterator.hasNext();) {
			ChangeStatus changestatus = (ChangeStatus) iterator.next();
			changestatus.setTicket(null);
			changes.add(changestatus);
		}
		ticket.setChanges(changes);
		response.setData(ticket);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<String>> delete(@PathVariable ("id") String id) {
		Response<String> response = new Response<String>();
		Ticket ticket = ticketService.findById(id);
		if(ticket == null) {
			response.getErrors().add("Register not found:"+id);
			return ResponseEntity.badRequest().body(response);			
		}
		ticketService.Delete(id);
		return ResponseEntity.ok(new Response<String>());
		}
	
}
