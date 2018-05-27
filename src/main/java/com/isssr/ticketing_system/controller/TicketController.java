package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.*;
import com.isssr.ticketing_system.response_entity.*;
import com.isssr.ticketing_system.service.*;
import com.isssr.ticketing_system.validator.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Validated
@RestController
@RequestMapping("/api/v1/tickets/")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketSourceService ticketSourceService;

    @Autowired
    private VisibilityService visibilityService;

    @Autowired
    private TicketCategoryService ticketCategoryService;

    @Autowired
    private TicketStatusService ticketStatusService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TicketPriorityService priorityService;

    private TicketValidator ticketValidator;

    @Autowired
    public TicketController(TicketValidator ticketValidator) {
        this.ticketValidator = ticketValidator;
    }

    @InitBinder
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(ticketValidator);
    }

    @RequestMapping(path = "metadata", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity metadata(@AuthenticationPrincipal Principal principal) {
        Optional<User> user = userService.findByEmail(principal.getName());

        Iterable<Visibility> visibilities = visibilityService.findAll();
        Collection<User> assignees = user.get().getTeam().getMembers();
        Iterable<TicketCategory> categories = ticketCategoryService.findAll();
        Iterable<Product> targets = productService.findAll();
        Iterable<TicketPriority> priorities = priorityService.findAll();

        return new HashMapResponseEntityBuilder()
                .setBuilder("visibilities", new ListObjectResponseEntityBuilder<>((StreamSupport.stream(visibilities.spliterator(), false)).collect(Collectors.toList())))
                .setBuilder("assignees", new ListObjectResponseEntityBuilder<>(assignees))
                .setBuilder("categories", new ListObjectResponseEntityBuilder<>((StreamSupport.stream(categories.spliterator(), false)).collect(Collectors.toList())))
                .setBuilder("targets", new ListObjectResponseEntityBuilder<>((StreamSupport.stream(targets.spliterator(), false)).collect(Collectors.toList())))
                .setBuilder("priorities", new ListObjectResponseEntityBuilder<>((StreamSupport.stream(priorities.spliterator(), false)).collect(Collectors.toList())))
                .setStatus(HttpStatus.OK)
                .build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity create(@Valid @RequestBody Ticket ticket) {
        ticket.setCreationTimestamp(Instant.now());
        ticket.setSource(ticketSourceService.findByName("CLIENT").get());
        ticket.setStatus(ticketStatusService.findByName("INITIALIZED").get());
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
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody Ticket ticket) {
        /*Optional<Ticket> foundTicket = ticketService.findById(id);

        if (!foundTicket.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TICKET_NOT_FOUND");*/

        //ticket.setId(foundTicket.get().getId());
        //ticketService.save(ticket);

        try {

            ticket.setId(id);

            ticketService.updateOne(id, ticket);

        } catch (UpdateException e) {

            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());

        } catch (EntityNotFoundException e) {

            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());

        }

        return CommonResponseEntity.OkResponseEntity("UPDATED");
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity delete(@PathVariable Long id) {
        /*Optional<Ticket> foundTicket = ticketService.findById(id);

        if (!foundTicket.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TICKET_NOT_FOUND");*/

        if (ticketService.deleteById(id)) {

            return CommonResponseEntity.OkResponseEntity("DELETED");

        } else {

            return CommonResponseEntity.NotFoundResponseEntity("TICKET_NOT_FOUND");

        }
    }

    @RequestMapping(path = "restore/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity restore(@PathVariable Long id) {

        try {
            Ticket restoredTicket = this.ticketService.restoreById(id);

            return new ObjectResponseEntityBuilder<Ticket>(restoredTicket, "full").setStatus(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());
        }

    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        Page<Ticket> ticketPage;
        try {
            ticketPage = ticketService.findAll(page, pageSize);
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
        return new PageResponseEntityBuilder(ticketPage)
                .setStatus(HttpStatus.OK)
                .build();

        /*Stream<Ticket> tickets;
        if (page != null && size != null) {
            try {
                tickets = (ticketService.findAll(page, size).stream());
            } catch (PageableQueryException e) {
                return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
            } catch (EntityNotFoundException e) {
                return CommonResponseEntity.NotFoundResponseEntity("TICKETS_NOT_FOUND");
            }
        } else
            tickets = (StreamSupport.stream(ticketService.findAll().spliterator(), false));

        return new ListObjectResponseEntityBuilder<>(tickets.collect(Collectors.toList()))
                .setStatus(HttpStatus.OK)
                .build();*/

        /*
        Page<Ticket> ticketPage;

        try {

            ticketPage = ticketService.findAll(page, pageSize);

        } catch (PageableQueryException e) {

            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());

        } /*catch (EntityNotFoundException e) {

            return CommonResponseEntity.NotFoundResponseEntity("TICKETS_NOT_FOUND");

        }*/
        //return new ResponseEntity<Page<Ticket>>(ticketPage, HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllNotDeletedPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {

        Page<Ticket> ticketPage;
        try {
            ticketPage = ticketService.findAllNotDeleted(page, pageSize);
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
        return new PageResponseEntityBuilder(ticketPage)
                .setStatus(HttpStatus.OK)
                .build();

        /*Stream<Ticket> tickets;
        if (page != null && size != null) {
            try {
                tickets = (ticketService.findAll(page, size).stream());
            } catch (PageableQueryException e) {
                return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
            } catch (EntityNotFoundException e) {
                return CommonResponseEntity.NotFoundResponseEntity("TICKETS_NOT_FOUND");
            }
        } else
            tickets = (StreamSupport.stream(ticketService.findAll().spliterator(), false));

        return new ListObjectResponseEntityBuilder<>(tickets.collect(Collectors.toList()))
                .setStatus(HttpStatus.OK)
                .build();*/

        /*
        Page<Ticket> ticketPage;

        try {

            ticketPage = ticketService.findAllNotDeleted(page, pageSize);

        } catch (PageableQueryException e) {

            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());

        } /*catch (EntityNotFoundException e) {

            return CommonResponseEntity.NotFoundResponseEntity("TICKETS_NOT_FOUND");

        }*/
        //return new ResponseEntity<Page<Ticket>>(ticketPage, HttpStatus.OK);

    }

    @RequestMapping(value = "deleted", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllDeletedPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {

        Page<Ticket> ticketPage;
        try {
            ticketPage = ticketService.findAllDeleted(page, pageSize);
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
        return new PageResponseEntityBuilder(ticketPage)
                .setStatus(HttpStatus.OK)
                .build();

        /*Stream<Ticket> tickets;
        if (page != null && size != null) {
            try {
                tickets = (ticketService.findAll(page, size).stream());
            } catch (PageableQueryException e) {
                return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
            } catch (EntityNotFoundException e) {
                return CommonResponseEntity.NotFoundResponseEntity("TICKETS_NOT_FOUND");
            }
        } else
            tickets = (StreamSupport.stream(ticketService.findAll().spliterator(), false));

        return new ListObjectResponseEntityBuilder<>(tickets.collect(Collectors.toList()))
                .setStatus(HttpStatus.OK)
                .build();*/

        /*
        Page<Ticket> ticketPage;

        try {

            ticketPage = ticketService.findAllDeleted(page, pageSize);

        } catch (PageableQueryException e) {

            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());

        } /*catch (EntityNotFoundException e) {

            return CommonResponseEntity.NotFoundResponseEntity("TICKETS_NOT_FOUND");

        }*/
        //return new ResponseEntity<Page<Ticket>>(ticketPage, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity deleteAll() {
        Long count = ticketService.count();

        if (count == 0)
            return CommonResponseEntity.NotFoundResponseEntity("TICKETS_NOT_FOUND");

        ticketService.deleteAll();

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }
}
