package com.isssr.ticketing_system.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.response_entity.JsonViews;
import com.isssr.ticketing_system.model.User;
import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import com.isssr.ticketing_system.response_entity.PageResponseEntityBuilder;
import com.isssr.ticketing_system.response_entity.ResponseEntityBuilder;
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

@Validated
@RestController
@RequestMapping("/api/v1/users/")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

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
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
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
        Optional<User> foundUser = userService.findUser(term, type);

        if (!foundUser.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("USER_NOT_FOUND");

        try {
            userService.updateUser(foundUser.get().getId(), user);
        } catch (UpdateException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
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

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(value = "all", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<User> userPage = userService.findAll(page, pageSize);
            return new PageResponseEntityBuilder(userPage).setStatus(HttpStatus.OK).build();
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
}
