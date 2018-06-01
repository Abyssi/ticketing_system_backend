package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.Team;
import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import com.isssr.ticketing_system.response_entity.ObjectResponseEntityBuilder;
import com.isssr.ticketing_system.response_entity.PageResponseEntityBuilder;
import com.isssr.ticketing_system.service.TeamService;
import com.isssr.ticketing_system.validator.TeamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/v1/teams/")
public class TeamController {
    @Autowired
    private TeamService teamService;

    private TeamValidator teamValidator;

    @Autowired
    public TeamController(TeamValidator teamValidator) {
        this.teamValidator = teamValidator;
    }

    @InitBinder
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(teamValidator);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity create(@Valid @RequestBody Team team) {
        teamService.save(team);

        return CommonResponseEntity.OkResponseEntity("CREATED");
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getByID(@PathVariable Long id) {
        Optional<Team> team = teamService.findById(id);

        if (!team.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TEAM_NOT_FOUND");

        return new ObjectResponseEntityBuilder<>(team.get(), "full").setStatus(HttpStatus.OK).build();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody Team team) {
        /*Optional<Team> foundTeam = teamService.findById(id);

        if (!foundTeam.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TEAM_NOT_FOUND");

        team.setId(foundTeam.get().getId());
        teamService.save(team);*/

        try {

            teamService.updateOne(id, team);

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
        /*Optional<Team> foundTeam = teamService.findById(id);

        if (!foundTeam.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TEAM_NOT_FOUND");*/

        if (teamService.deleteById(id)) {

            return CommonResponseEntity.OkResponseEntity("DELETED");

        } else {

            return CommonResponseEntity.NotFoundResponseEntity("TEAM_NOT_FOUND");

        }
    }

    @RequestMapping(path = "restore/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity restore(@PathVariable Long id) {

        try {
            Team restoredTeam = this.teamService.restoreById(id);

            return new ObjectResponseEntityBuilder<Team>(restoredTeam, "full").setStatus(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());
        }

    }


    @RequestMapping(value = "all", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {

        Page<Team> teamPage;
        try {
            teamPage = teamService.findAll(page, pageSize);
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
        return new PageResponseEntityBuilder(teamPage)
                .setStatus(HttpStatus.OK)
                .build();
        /*Stream<Target> products;
        if (page != null && size != null) {
            try {
                products = (productService.findAll(page, size).stream());
            } catch (PageableQueryException e) {
                return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
            } catch (EntityNotFoundException e) {
                return CommonResponseEntity.NotFoundResponseEntity("PRODUCTS_NOT_FOUND");
            }
        } else
            products = (StreamSupport.stream(productService.findAll().spliterator(), false));

        return new ListObjectResponseEntityBuilder<>(products.collect(Collectors.toList()))
                .setStatus(HttpStatus.OK)
                .build();*/

        /*Page<Target> productPage;
        try {

            productPage = productService.findAll(page, pageSize);

        } catch (PageableQueryException e) {

            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());

        } /*catch (EntityNotFoundException e) {

            return CommonResponseEntity.NotFoundResponseEntity("PRODUCTS_NOT_FOUND");

        }*/

        //return new ResponseEntity<Page<Target>>(productPage, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllNotDeletedPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {

        Page<Team> teamPage;
        try {
            teamPage = teamService.findAllNotDeleted(page, pageSize);
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
        return new PageResponseEntityBuilder(teamPage)
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

        Page<Team> teamPage;
        try {
            teamPage = teamService.findAllDeleted(page, pageSize);
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
        return new PageResponseEntityBuilder(teamPage)
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
    public ResponseEntity delete() {
        Long count = teamService.count();

        if (count == 0)
            return CommonResponseEntity.NotFoundResponseEntity("TEAMS_NOT_FOUND");

        teamService.deleteAll();

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }
}
