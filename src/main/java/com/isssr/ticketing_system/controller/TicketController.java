package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.model.Ticket;
import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import com.isssr.ticketing_system.response_entity.ListObjectResponseEntityBuilder;
import com.isssr.ticketing_system.response_entity.ObjectResponseEntityBuilder;
import com.isssr.ticketing_system.service.TicketService;
import com.isssr.ticketing_system.validator.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Validated
@RestController
@RequestMapping("/api/v1/tickets/")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    private TicketValidator ticketValidator;

    @Autowired
    public TicketController(TicketValidator ticketValidator) {
        this.ticketValidator = ticketValidator;
    }

    @InitBinder
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(ticketValidator);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity create(@Valid @RequestBody Ticket ticket) {
        ticketService.save(ticket);

        return CommonResponseEntity.OkResponseEntity("CREATED");
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.findById(id);

        if (!ticket.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TICKET_NOT_FOUND");

        return new ObjectResponseEntityBuilder<>(ticket.get(), "full").setStatus(HttpStatus.OK).build();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@PathVariable Long id, @Valid @RequestBody Ticket ticket) {
        Optional<Ticket> foundTicket = ticketService.findById(id);

        if (!foundTicket.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TICKET_NOT_FOUND");

        ticket.setId(foundTicket.get().getId());
        ticketService.save(ticket);

        return CommonResponseEntity.OkResponseEntity("UPDATED");
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity delete(@PathVariable Long id) {
        Optional<Ticket> foundTicket = ticketService.findById(id);

        if (!foundTicket.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TICKET_NOT_FOUND");

        ticketService.deleteById(foundTicket.get().getId());

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) {
        Stream<Ticket> tickets = (page != null && size != null)
                ? (ticketService.findAll(PageRequest.of(page, size)).stream())
                : (StreamSupport.stream(ticketService.findAll().spliterator(), false));

        return new ListObjectResponseEntityBuilder<>(tickets.collect(Collectors.toList()))
                .setStatus(HttpStatus.OK)
                .build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity delete() {
        Long count = ticketService.count();

        if (count == 0)
            return CommonResponseEntity.NotFoundResponseEntity("TICKETS_NOT_FOUND");

        ticketService.deleteAll();

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }
}
