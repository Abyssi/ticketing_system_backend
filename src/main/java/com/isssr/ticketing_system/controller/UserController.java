package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.model.User;
import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import com.isssr.ticketing_system.response_entity.ListObjectResponseEntityBuilder;
import com.isssr.ticketing_system.response_entity.ObjectResponseEntityBuilder;
import com.isssr.ticketing_system.service.RoleService;
import com.isssr.ticketing_system.service.UserService;
import com.isssr.ticketing_system.validator.UserValidator;
import com.isssr.ticketing_system.validator.ValidString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    @RequestMapping(path = "register", method = RequestMethod.PUT)
    public ResponseEntity register(@Valid @RequestBody User user) {
        if (userService.existsByEmail(user.getEmail()))
            return CommonResponseEntity.UnprocessableEntityResponseEntity("EMAIL_ALREADY_REGISTERED");

        user.setRoles(Collections.singletonList(roleService.findByName("ROLE_CUSTOMER").get()));
        userService.save(user);

        return CommonResponseEntity.OkResponseEntity("REGISTERED");
    }

    @RequestMapping(path = "self", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity self(@AuthenticationPrincipal Principal principal) {
        Optional<User> user = userService.findByEmail(principal.getName());

        if (!user.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("USER_NOT_FOUND");

        return new ObjectResponseEntityBuilder<>(user.get(), "full").setStatus(HttpStatus.OK).build();
    }

    @RequestMapping(path = "{term}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@PathVariable String term, @ValidString(list = {"id", "email"}, message = "Invalid type") @RequestParam("type") String type) {
        Optional<User> user = findUser(term, type);

        if (!user.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("USER_NOT_FOUND");

        return new ObjectResponseEntityBuilder<>(user.get(), "full").setStatus(HttpStatus.OK).build();
    }

    @RequestMapping(path = "{term}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity delete(@PathVariable String term,
                                 @ValidString(list = {"id", "email"}, message = "Invalid type") @RequestParam("type") String type,
                                 @Valid @RequestBody User user) {
        Optional<User> foundUser = findUser(term, type);

        if (!foundUser.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("USER_NOT_FOUND");

        user.setId(foundUser.get().getId());
        userService.save(user);

        return CommonResponseEntity.OkResponseEntity("UPDATED");
    }

    @RequestMapping(path = "{term}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity delete(@PathVariable String term, @ValidString(list = {"id", "email"}, message = "Invalid type") @RequestParam("type") String type) {
        Optional<User> user = findUser(term, type);

        if (!user.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("USER_NOT_FOUND");

        userService.deleteById(user.get().getId());

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) {
        Stream<User> users;
        if (page != null && size != null) {
            try {
                users = (userService.findAll(page, size).stream());
            } catch (PageableQueryException e) {
                return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
            } catch (EntityNotFoundException e) {
                return CommonResponseEntity.NotFoundResponseEntity("USERS_NOT_FOUND");
            }
        } else
            users = (StreamSupport.stream(userService.findAll().spliterator(), false));

        return new ListObjectResponseEntityBuilder<>(users.collect(Collectors.toList()))
                .setStatus(HttpStatus.OK)
                .build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity delete() {
        Long count = userService.count();

        if (count == 0)
            return CommonResponseEntity.NotFoundResponseEntity("USERS_NOT_FOUND");

        userService.deleteAll();

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }

    private Optional<User> findUser(String term, String type) {
        switch (type) {
            case "id":
                return userService.findById(Long.parseLong(term));
            case "email":
                return userService.findByEmail(term);
        }
        return Optional.empty();
    }
}
