package com.isssr.ticketing_system.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.controller.mailController.MailSenderController;
import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.model.*;
import com.isssr.ticketing_system.model.UserFilter.UserFiltered;
import com.isssr.ticketing_system.response_entity.*;
import com.isssr.ticketing_system.service.*;
import com.isssr.ticketing_system.validator.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
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
    private TargetService targetService;

    @Autowired
    private TicketPriorityService priorityService;

    @Autowired
    private MailSenderController mailSenderController;

    private TicketValidator ticketValidator;

    @Autowired
    public TicketController(TicketValidator ticketValidator) {
        this.ticketValidator = ticketValidator;
    }

    @InitBinder
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(ticketValidator);
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(path = "metadata", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity metadata(@AuthenticationPrincipal Principal principal) {
        Optional<User> user = userService.findByEmail(principal.getName());

        Iterable<Visibility> visibilities = visibilityService.findAll();
        Collection<User> assignees = user.get().getTeam().getMembers();
        Iterable<TicketCategory> categories = ticketCategoryService.findAll();
        Iterable<Target> targets = targetService.findAll();
        Iterable<TicketPriority> priorities = priorityService.findAll();

        return new HashMapResponseEntityBuilder(HttpStatus.OK)
                .set("visibilities", StreamSupport.stream(visibilities.spliterator(), false).collect(Collectors.toList()))
                .set("assignees", assignees)
                .set("categories", StreamSupport.stream(categories.spliterator(), false).collect(Collectors.toList()))
                .set("targets", StreamSupport.stream(targets.spliterator(), false).collect(Collectors.toList()))
                .set("priorities", StreamSupport.stream(priorities.spliterator(), false).collect(Collectors.toList()))
                .build();
    }

    @JsonView(JsonViews.DetailedTicket.class)
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity create(@Valid @RequestBody Ticket ticket, @AuthenticationPrincipal Principal principal) {
        ticket.setCreationTimestamp(Instant.now());
        ticket.setSource(ticketSourceService.findByName("CLIENT").get());
        ticket.setStatus(ticketStatusService.findByName("INITIALIZED").get());
        ticket.setCustomer(userService.findByEmail(principal.getName()).get());
        ticketService.save(ticket);

        new Thread(() -> mailSenderController.sendMail(principal.getName(), "TICKET_OPENED")).start();

        return CommonResponseEntity.OkResponseEntity("CREATED");
    }

    @JsonView(JsonViews.DetailedTicket.class)
    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @UserFiltered
    public ResponseEntity get(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.findById(id);

        if (!ticket.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TICKET_NOT_FOUND");

        return new ResponseEntityBuilder<>(ticket.get()).setStatus(HttpStatus.OK).build();
    }

    @JsonView(JsonViews.DetailedTicket.class)
    @RequestMapping(path = "{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody Ticket ticket) {
        try {
            ticketService.updateById(id, ticket);
        } catch (EntityNotFoundException e) {
            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());
        }

        return CommonResponseEntity.OkResponseEntity("UPDATED");
    }

    @JsonView(JsonViews.DetailedTicket.class)
    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity delete(@PathVariable Long id) {
        if (ticketService.deleteById(id))
            return CommonResponseEntity.OkResponseEntity("DELETED");
        else
            return CommonResponseEntity.NotFoundResponseEntity("TICKET_NOT_FOUND");
    }

    @JsonView(JsonViews.DetailedTicket.class)
    @RequestMapping(path = "restore/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity restore(@PathVariable Long id) {
        try {
            Ticket restoredTicket = this.ticketService.restoreById(id);
            return new ResponseEntityBuilder<>(restoredTicket).setStatus(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(value = "all", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @UserFiltered
    public ResponseEntity getAllPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<Ticket> ticketPage = ticketService.findAll(page, pageSize);
            return new PageResponseEntityBuilder(ticketPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @UserFiltered
    public ResponseEntity getAllNotDeletedPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<Ticket> ticketPage = ticketService.findAllNotDeleted(page, pageSize);
            return new PageResponseEntityBuilder(ticketPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(value = "deleted", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @UserFiltered
    public ResponseEntity getAllDeletedPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<Ticket> ticketPage = ticketService.findAllDeleted(page, pageSize);
            return new PageResponseEntityBuilder(ticketPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(path = "search/{title}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @UserFiltered
    public ResponseEntity searchByTitlePaginated(@PathVariable String title, @RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<Ticket> ticketPage = ticketService.findByTitleContaining(title, page, pageSize);
            return new PageResponseEntityBuilder(ticketPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.DetailedTicket.class)
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
