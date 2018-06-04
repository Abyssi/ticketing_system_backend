package com.isssr.ticketing_system;

import com.isssr.ticketing_system.model.*;
import com.isssr.ticketing_system.model.auto_generated.enumeration.ComparisonOperatorsEnum;
import com.isssr.ticketing_system.model.auto_generated.temporary.DataBaseTimeQuery;
import com.isssr.ticketing_system.model.auto_generated.temporary.QueryType;
import com.isssr.ticketing_system.service.*;
import com.isssr.ticketing_system.service.auto_generated.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
    private TargetService targetService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private QueryService queryService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        this.configureEmail();
        //this.createReadOnlyUser();
        //this.generateTicket();
        //this.generateQueries();

        alreadySetup = true;
    }

    private void configurePrivileges() {
        this.privilegeService.save(new Privilege("READ_PRIVILEGE"));
        this.privilegeService.save(new Privilege("WRITE_PRIVILEGE"));
    }

    private void configureEmail() {
        this.mailService.save(new Mail("Format error", "format not respected.", "FORMAT"));
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
        this.ticketStatusService.save(new TicketStatus("PENDING"));
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
        this.targetService.save(new Target("System", "1.0", false));
    }

    private void configureTeams() {
        Team systemTeam = this.teamService.save(new Team("System team", userService.findByEmail("admin@admin.com").get()));
        systemTeam.getMembers().add(this.userService.findByEmail("andrea.silvi@mail.com").get());
        this.teamService.save(systemTeam);
    }

    private void createReadOnlyUser() {
        try {
            Connection connection = this.jdbcTemplate.getDataSource().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("CREATE ROLE Read_Only_User WITH LOGIN PASSWORD 'user' \n" +
                    "NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION VALID UNTIL 'infinity';");
            statement.execute("GRANT CONNECT ON DATABASE ticketing_system_db TO Read_Only_User;\n" +
                    "GRANT USAGE ON SCHEMA public TO Read_Only_User;\n" +
                    "GRANT SELECT ON ALL TABLES IN SCHEMA public TO Read_Only_User;\n" +
                    "GRANT SELECT ON ALL SEQUENCES IN SCHEMA public TO Read_Only_User;");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                targetService.findByName("System").get(),
                ticketPriorityService.findByName("HIGH").get(),
                visibilityService.findByName("PRIVATE").get(),
                false
        ));
    }

    private void generateQueries() {

        DataBaseTimeQuery query = new DataBaseTimeQuery(
                "This query check number of products in product table, if it is grater than 1 generate alert ticket",
                "SELECT count(*) FROM ts_product",
                ticketPriorityService.findByName("HIGH").get(),
                "*/5 * * * * ?",
                true,
                false,
                ComparisonOperatorsEnum.GRATHER,
                1L,
                null,
                QueryType.DATA_BASE_INSTANT_CHECK
        );

        this.queryService.create(query);

        query = new DataBaseTimeQuery(
                "This query check number of teams in product table, if it is less than 1000 generate alert ticket",
                "SELECT count(*) FROM ts_team",
                ticketPriorityService.findByName("HIGH").get(),
                "*/8 * * * * ?",
                true,
                false,
                ComparisonOperatorsEnum.LESS,
                1000L,
                null,
                QueryType.DATA_BASE_INSTANT_CHECK
        );

        this.queryService.create(query);

        query = new DataBaseTimeQuery(
                "This query check number of products in product table, if it is growth of 1 or more generate alert ticket",
                "SELECT count(*) FROM ts_product",
                ticketPriorityService.findByName("HIGH").get(),
                "0 */1 * * * ?",
                true,
                false,
                ComparisonOperatorsEnum.GRATHER_EQUALS,
                1L,
                null,
                QueryType.DATA_BASE_TABLE_MONITOR
        );

        this.queryService.create(query);

    }
}