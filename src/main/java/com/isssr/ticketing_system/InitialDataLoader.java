package com.isssr.ticketing_system;

import com.isssr.ticketing_system.model.*;
import com.isssr.ticketing_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketPriorityService ticketPriorityService;

    @Autowired
    private TicketCategoryService ticketCategoryService;

    @Autowired
    private TicketSourceService ticketSourceService;

    @Autowired
    private VisibilityService visibilityService;

    @Autowired
    private TicketDifficultyService ticketDifficultyService;

    @Autowired
    private TicketRelationTypeService ticketRelationTypeService;

    @Autowired
    private TicketStatusService ticketStatusService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;

        this.configurePrivileges();
        this.configureRoles();
        this.configureVisibilities();
        this.configureUsers();
        this.configurePriorities();
        this.configureCategories();
        this.configureSources();
        this.configureStatuses();
        this.configureDifficulties();
        this.configureRelationTypes();
        this.configureProducts();
        this.configureTeams();
        this.generateTicket();

        alreadySetup = true;
    }

    private void configurePrivileges() {
        this.privilegeService.save(new Privilege("READ_PRIVILEGE"));
        this.privilegeService.save(new Privilege("WRITE_PRIVILEGE"));
    }

    private void configureRoles() {
        // External
        this.roleService.save(new Role("ROLE_CUSTOMER", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_PRIVILEGE").get(), privilegeService.findByName("WRITE_PRIVILEGE").get()))));
        this.roleService.save(new Role("ROLE_HELP_DESK_OPERATOR", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_PRIVILEGE").get(), privilegeService.findByName("WRITE_PRIVILEGE").get()))));

        // Internal
        this.roleService.save(new Role("ROLE_TEAM_COORDINATOR", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_PRIVILEGE").get(), privilegeService.findByName("WRITE_PRIVILEGE").get()))));
        this.roleService.save(new Role("ROLE_TEAM_LEADER", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_PRIVILEGE").get(), privilegeService.findByName("WRITE_PRIVILEGE").get()))));
        this.roleService.save(new Role("ROLE_TEAM_MEMBER", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_PRIVILEGE").get(), privilegeService.findByName("WRITE_PRIVILEGE").get()))));
        this.roleService.save(new Role("ROLE_ADMIN", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_PRIVILEGE").get(), privilegeService.findByName("WRITE_PRIVILEGE").get()))));
    }

    private void configureVisibilities() {
        this.visibilityService.save(new Visibility("PRIVATE"));
        this.visibilityService.save(new Visibility("PUBLIC"));
    }

    private void configureUsers() {
        this.userService.save(new User("Admin", "Admin", "admin@admin.com", passwordEncoder.encode("password"), new ArrayList<>(Arrays.asList(roleService.findByName("ROLE_ADMIN").get()))));
        this.userService.save(new User("Andrea", "Silvi", "andrea.silvi@mail.com", passwordEncoder.encode("password"), new ArrayList<>(Arrays.asList(roleService.findByName("ROLE_CUSTOMER").get()))));
    }

    private void configurePriorities() {
        this.ticketPriorityService.save(new TicketPriority("LOW"));
        this.ticketPriorityService.save(new TicketPriority("MEDIUM"));
        this.ticketPriorityService.save(new TicketPriority("HIGH"));
    }

    private void configureCategories() {
        this.ticketCategoryService.save(new TicketCategory("SYSTEM"));
    }

    private void configureSources() {
        this.ticketSourceService.save(new TicketSource("SYSTEM"));
        this.ticketSourceService.save(new TicketSource("HELP_DESK"));
        this.ticketSourceService.save(new TicketSource("CLIENT"));
        this.ticketSourceService.save(new TicketSource("MAIL"));
    }

    private void configureStatuses() {
        this.ticketStatusService.save(new TicketStatus("INITIALIZED"));
        this.ticketStatusService.save(new TicketStatus("WORK_IN_PROGRESS"));
        this.ticketStatusService.save(new TicketStatus("FINISHED"));
    }

    private void configureDifficulties() {
        this.ticketDifficultyService.save(new TicketDifficulty("LOW"));
        this.ticketDifficultyService.save(new TicketDifficulty("MEDIUM"));
        this.ticketDifficultyService.save(new TicketDifficulty("HIGH"));
    }

    private void configureRelationTypes() {
        this.ticketRelationTypeService.save(new TicketRelationType("LINKED"));
        this.ticketRelationTypeService.save(new TicketRelationType("DEPENDENT"));
        this.ticketRelationTypeService.save(new TicketRelationType("SIMILAR"));
    }

    private void configureProducts() {
        this.productService.save(new Product("System", "1.0"));
    }

    private void configureTeams() {
        Team systemTeam = this.teamService.save(new Team("System team", userService.findByEmail("admin@admin.com").get()));
        systemTeam.getMembers().add(this.userService.findByEmail("andrea.silvi@mail.com").get());
        this.teamService.save(systemTeam);
    }

    private void generateTicket() {
        this.ticketService.save(new Ticket(
                ticketStatusService.findByName("INITIALIZED").get(),
                ticketSourceService.findByName("SYSTEM").get(),
                Instant.now(),
                ticketCategoryService.findByName("SYSTEM").get(),
                "Auto generated ticket",
                "This is an auto generated ticket description",
                userService.findByEmail("admin@admin.com").get(),
                productService.findByName("System").get(),
                ticketPriorityService.findByName("HIGH").get(),
                visibilityService.findByName("PRIVATE").get()
        ));
    }
}