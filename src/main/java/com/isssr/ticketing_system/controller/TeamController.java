package com.isssr.ticketing_system.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.model.Team;
import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import com.isssr.ticketing_system.response_entity.JsonViews;
import com.isssr.ticketing_system.response_entity.PageResponseEntityBuilder;
import com.isssr.ticketing_system.response_entity.ResponseEntityBuilder;
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

    @JsonView(JsonViews.DetailedTeam.class)
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity create(@Valid @RequestBody Team team) {
        teamService.save(team);

        return CommonResponseEntity.OkResponseEntity("CREATED");
    }

    @JsonView(JsonViews.DetailedTeam.class)
    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@PathVariable Long id) {
        Optional<Team> team = teamService.findById(id);

        if (!team.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TEAM_NOT_FOUND");

        return new ResponseEntityBuilder<>(team.get()).setStatus(HttpStatus.OK).build();
    }

    @JsonView(JsonViews.DetailedTeam.class)
    @RequestMapping(path = "{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody Team team) {
        try {
            teamService.updateById(id, team);
        } catch (EntityNotFoundException e) {
            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());
        }

        return CommonResponseEntity.OkResponseEntity("UPDATED");
    }

    @JsonView(JsonViews.DetailedTeam.class)
    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity delete(@PathVariable Long id) {
        if (teamService.deleteById(id))
            return CommonResponseEntity.OkResponseEntity("DELETED");
        else
            return CommonResponseEntity.NotFoundResponseEntity("TEAM_NOT_FOUND");
    }

    @JsonView(JsonViews.DetailedTeam.class)
    @RequestMapping(path = "restore/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity restore(@PathVariable Long id) {
        try {
            Team restoredTeam = this.teamService.restoreById(id);
            return new ResponseEntityBuilder<>(restoredTeam).setStatus(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.DetailedTeam.class)
    @RequestMapping(value = "all", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<Team> teamPage = teamService.findAll(page, pageSize);
            return new PageResponseEntityBuilder(teamPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.DetailedTeam.class)
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllNotDeletedPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<Team> teamPage = teamService.findAllNotDeleted(page, pageSize);
            return new PageResponseEntityBuilder(teamPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.DetailedTeam.class)
    @RequestMapping(value = "deleted", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllDeletedPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<Team> teamPage = teamService.findAllDeleted(page, pageSize);
            return new PageResponseEntityBuilder(teamPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.DetailedTeam.class)
    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity deleteAll() {
        Long count = teamService.count();

        if (count == 0)
            return CommonResponseEntity.NotFoundResponseEntity("TEAMS_NOT_FOUND");

        teamService.deleteAll();

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }
}
