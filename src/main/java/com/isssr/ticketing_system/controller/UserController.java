package com.isssr.ticketing_system.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.Company;
import com.isssr.ticketing_system.model.Role;
import com.isssr.ticketing_system.model.User;
import com.isssr.ticketing_system.response_entity.*;
import com.isssr.ticketing_system.service.CompanyService;
import com.isssr.ticketing_system.service.RoleService;
import com.isssr.ticketing_system.service.UserService;
import com.isssr.ticketing_system.validator.UserValidator;
import com.isssr.ticketing_system.validator.ValidString;
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
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Validated
@RestController
@RequestMapping("/api/v1/users/")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CompanyService companyService;

    private UserValidator userValidator;

    @Autowired
    public UserController(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @InitBinder
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @JsonView(JsonViews.DetailedUser.class)
    @RequestMapping(path = "self", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity self(@AuthenticationPrincipal Principal principal) {
        Optional<User> user = userService.findByEmail(principal.getName());

        if (!user.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("USER_NOT_FOUND");

        return new ResponseEntityBuilder<>(user.get()).setStatus(HttpStatus.OK).build();
    }

    @JsonView(JsonViews.DetailedUser.class)
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity create(@Valid @RequestBody User user) {
        if (userService.existsByEmail(user.getEmail()))
            return CommonResponseEntity.UnprocessableEntityResponseEntity("EMAIL_ALREADY_REGISTERED");

        user.setRoles(Collections.singletonList(roleService.findByName("ROLE_CUSTOMER").get()));
        userService.save(user);

        return CommonResponseEntity.OkResponseEntity("REGISTERED");
    }

    @JsonView(JsonViews.DetailedUser.class)
    @RequestMapping(path = "{term}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@PathVariable String term, @ValidString(list = {"id", "email"}, message = "Invalid type") @RequestParam("type") String type) {
        Optional<User> user = userService.findUser(term, type);

        if (!user.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("USER_NOT_FOUND");

        return new ResponseEntityBuilder<>(user.get()).setStatus(HttpStatus.OK).build();
    }

    @JsonView(JsonViews.DetailedUser.class)
    @RequestMapping(path = "{term}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity update(@PathVariable String term,
                                 @ValidString(list = {"id", "email"}, message = "Invalid type") @RequestParam("type") String type,
                                 @Valid @RequestBody User user) {
        try {
            userService.updateUser(term, type, user);
        } catch (UpdateException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        } catch (EntityNotFoundException e) {
            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());
        }

        return CommonResponseEntity.OkResponseEntity("UPDATED");
    }

    @JsonView(JsonViews.DetailedUser.class)
    @RequestMapping(path = "{term}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity delete(@PathVariable String term, @ValidString(list = {"id", "email"}, message = "Invalid type") @RequestParam("type") String type) {
        Optional<User> user = userService.findUser(term, type);

        if (!user.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("USER_NOT_FOUND");

        userService.deleteById(user.get().getId());

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }

    @JsonView(JsonViews.DetailedUser.class)
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<User> userPage = userService.findAll(page, pageSize);
            return new PageResponseEntityBuilder(userPage).setStatus(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(path = "search/{email}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity searchByEmailPaginated(@PathVariable String email, @RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<User> userPage = userService.findByEmailContaining(email, page, pageSize);
            return new PageResponseEntityBuilder(userPage).setStatus(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.DetailedUser.class)
    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity deleteAll() {
        Long count = userService.count();

        if (count == 0)
            return CommonResponseEntity.NotFoundResponseEntity("USERS_NOT_FOUND");

        userService.deleteAll();

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(path = "metadata", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity metadata(@AuthenticationPrincipal Principal principal) {

        Iterable<Role> roles = roleService.findAll();
        Iterable<Company> companies = companyService.findAll();

        return new HashMapResponseEntityBuilder(HttpStatus.OK)
                .set("roles", StreamSupport.stream(roles.spliterator(), false).collect(Collectors.toList()))
                .set("companies", StreamSupport.stream(companies.spliterator(), false).collect(Collectors.toList()))
                .build();
    }
}
