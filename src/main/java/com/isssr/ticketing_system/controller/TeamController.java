package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.model.Team;
import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import com.isssr.ticketing_system.response_entity.ListObjectResponseEntityBuilder;
import com.isssr.ticketing_system.response_entity.ObjectResponseEntityBuilder;
import com.isssr.ticketing_system.service.TeamService;
import com.isssr.ticketing_system.validator.TeamValidator;
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
    public ResponseEntity get(@PathVariable Long id) {
        Optional<Team> team = teamService.findById(id);

        if (!team.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TEAM_NOT_FOUND");

        return new ObjectResponseEntityBuilder<>(team.get(), "full").setStatus(HttpStatus.OK).build();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@PathVariable Long id, @Valid @RequestBody Team team) {
        Optional<Team> foundTeam = teamService.findById(id);

        if (!foundTeam.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TEAM_NOT_FOUND");

        team.setId(foundTeam.get().getId());
        teamService.save(team);

        return CommonResponseEntity.OkResponseEntity("UPDATED");
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity delete(@PathVariable Long id) {
        Optional<Team> foundTeam = teamService.findById(id);

        if (!foundTeam.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TEAM_NOT_FOUND");

        teamService.deleteById(foundTeam.get().getId());

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) {
        Stream<Team> teams = (page != null && size != null)
                ? (teamService.findAll(PageRequest.of(page, size)).stream())
                : (StreamSupport.stream(teamService.findAll().spliterator(), false));

        return new ListObjectResponseEntityBuilder<>(teams.collect(Collectors.toList()))
                .setStatus(HttpStatus.OK)
                .build();
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
