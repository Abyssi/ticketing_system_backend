package com.isssr.ticketing_system;

import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.mail.MailService;
import com.isssr.ticketing_system.mail.model.Mail;
import com.isssr.ticketing_system.model.*;
import com.isssr.ticketing_system.model.auto_generated.enumeration.ComparisonOperatorsEnum;
import com.isssr.ticketing_system.model.auto_generated.query.DBScheduledCountQuery;
import com.isssr.ticketing_system.model.auto_generated.query.Query;
import com.isssr.ticketing_system.model.auto_generated.query.QueryType;
import com.isssr.ticketing_system.model.db_connection.DBConnectionInfo;
import com.isssr.ticketing_system.service.*;
import com.isssr.ticketing_system.service.auto_generated.QueryService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${wrong.query.mail.type}")
    private String wrongQueryMailType;

    @Value("${root.mode.username}")
    private String ROOT_USERNAME;

    @Value("${root.mode.password}")
    private String ROOT_PASSWORD;

    @Value("${admin.email}")
    private String ADMIN_EMAIL;

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
    private CompanyService companyService;

    @Autowired
    private SetupService setupService;

    @Autowired
    private QueryService queryService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private boolean firstSchedulingAlreadyDone = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //Check if db is already setup, Otherwise set up it
        if (!this.checkConfig()) {
            //Create DB and users
            this.createDB();
            this.createReadOnlyUser();

            //Create start table
            this.configureCompany();
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
            this.configureTargets();
            this.configureTeams();
            this.configureEmail();

            //Only for demo
            this.demoInit();

            //Make db setup
            this.setAlreadySetup(true);
        }

        if (this.firstSchedulingAlreadyDone) return;

        this.startScheduling();

        this.firstSchedulingAlreadyDone = true;

    }

    private void createDB() {
        try {
            Connection con = DriverManager.getConnection("jdbc:postgres://localhost:3306/", ROOT_USERNAME, ROOT_PASSWORD);
            Statement statement = con.createStatement();
            statement.execute("CREATE DATABASE ticketing_system_db\n" +
                    "    WITH \n" +
                    "    OWNER = " + ROOT_USERNAME + "\n" +
                    "    ENCODING = 'UTF8'\n" +
                    "    CONNECTION LIMIT = -1;");
            statement.close();
        } catch (SQLException e) {
            System.out.println("DB has been created");
        }
    }

    private void setAlreadySetup(boolean b) {
        this.setupService.save(new Setup(true));
    }

    private boolean checkConfig() {
        return this.setupService.existsBySetup(true);
    }

    private void configureCompany() {
        if (!this.companyService.existsByName("System"))
            this.companyService.save(new Company("System", false, "test.it"));
    }

    private void configurePrivileges() {
        this.privilegeService.save(new Privilege("READ_PRIVILEGE"));
        this.privilegeService.save(new Privilege("WRITE_PRIVILEGE"));

        this.privilegeService.save(new Privilege("READ_ALL_PRIVILEGE"));
        this.privilegeService.save(new Privilege("WRITE_ALL_PRIVILEGE"));
    }

    private void configureEmail() {
        if (!this.mailService.existsByType("FORMAT"))
            this.mailService.save(new Mail("Format error", "Format not respected. In attachment you can find rules for opening tickets by e-mail.\n" +
                    "You can also contact our help desk for more assistance.", "FORMAT"));
        if (!this.mailService.existsByType("TICKET_OPENED"))
            this.mailService.save(new Mail("Ticket opened", "Your ticket has been successfully created", "TICKET_OPENED"));
        if (!this.mailService.existsByType(this.wrongQueryMailType))
            this.mailService.save(new Mail("Wrong query", "A query with wrong behaviour has been executed. It has been disabled. \n\nCause:", this.wrongQueryMailType));
    }

    private void configureRoles() {
        // External
        this.roleService.save(new Role("ROLE_CUSTOMER", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_PRIVILEGE").get(), privilegeService.findByName("WRITE_PRIVILEGE").get()))));
        this.roleService.save(new Role("ROLE_HELP_DESK_OPERATOR", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_ALL_PRIVILEGE").get(), privilegeService.findByName("WRITE_ALL_PRIVILEGE").get()))));

        // Internal
        this.roleService.save(new Role("ROLE_TEAM_COORDINATOR", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_ALL_PRIVILEGE").get(), privilegeService.findByName("WRITE_PRIVILEGE").get()))));
        this.roleService.save(new Role("ROLE_TEAM_LEADER", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_ALL_PRIVILEGE").get(), privilegeService.findByName("WRITE_PRIVILEGE").get()))));
        this.roleService.save(new Role("ROLE_TEAM_MEMBER", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_ALL_PRIVILEGE").get(), privilegeService.findByName("WRITE_PRIVILEGE").get()))));
        this.roleService.save(new Role("ROLE_ADMIN", new ArrayList<>(Arrays.asList(privilegeService.findByName("READ_ALL_PRIVILEGE").get(), privilegeService.findByName("WRITE_ALL_PRIVILEGE").get()))));
    }

    private void configureVisibilities() {
        this.visibilityService.save(new Visibility("PRIVATE"));
        this.visibilityService.save(new Visibility("PUBLIC"));
    }

    private void configureUsers() {
        this.userService.save(new User("Admin", "Admin", ADMIN_EMAIL, "password", this.companyService.findByName("System").get(), new ArrayList<>(Arrays.asList(roleService.findByName("ROLE_ADMIN").get()))));
    }

    private void configurePriorities() {
        this.ticketPriorityService.save(new TicketPriority("LOW"));
        this.ticketPriorityService.save(new TicketPriority("MEDIUM"));
        this.ticketPriorityService.save(new TicketPriority("HIGH"));
    }

    private void configureCategories() {
        this.ticketCategoryService.save(new TicketCategory("BUG"));
        this.ticketCategoryService.save(new TicketCategory("ERROR"));
        this.ticketCategoryService.save(new TicketCategory("FAILURE"));
        this.ticketCategoryService.save(new TicketCategory("SUPPORT"));
        this.ticketCategoryService.save(new TicketCategory("HUMAN ERROR"));
        this.ticketCategoryService.save(new TicketCategory("GENERAL"));
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

    private void configureTargets() {
        this.targetService.save(new Target("System", "1.0"));
    }

    private void configureTeams() {
        Team systemTeam = this.teamService.save(new Team("System team", userService.findByEmail(ADMIN_EMAIL).get()));
        if (!this.teamService.existsByName(systemTeam.getName())) this.teamService.save(systemTeam);
    }

    private void createReadOnlyUser() {
        try {
            Connection connection = this.jdbcTemplate.getDataSource().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("CREATE ROLE Read_Only_User WITH LOGIN PASSWORD 'user' " +
                    "NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION VALID UNTIL 'infinity';");
            statement.execute("GRANT CONNECT ON DATABASE ticketing_system_db TO Read_Only_User;\n" +
                    "GRANT USAGE ON SCHEMA public TO Read_Only_User;\n" +
                    "GRANT SELECT ON ALL TABLES IN SCHEMA public TO Read_Only_User;\n" +
                    "GRANT SELECT ON ALL SEQUENCES IN SCHEMA public TO Read_Only_User;");
            statement.close();
        } catch (SQLException e) {
            System.out.println("User has been created");
        }
    }

    public void startScheduling() {
        List<Query> queryList = this.queryService.findAllActiveQueries();
        for (int i = 0; i < queryList.size(); i++) {
            Query query = queryList.get(i);
            try {
                this.queryService.activateQuery(query);
                this.queryService.updateOne(query.getId(), query);

            } catch (ParseException | SchedulerException | EntityNotFoundException | UpdateException e) {
                System.out.println("Error while trying to start scheduling");
                e.printStackTrace();
            }
        }
    }

    /*
        DEMO INITIALIZATION
     */

    private void demoInit() {
        this.demoGenerateCompanies();
        this.demoGenerateUsers();
        this.demoGenerateTeams();
        this.demoGenerateQueries();
        this.demoGenerateTargets();
        this.demoGenerateTickets();
    }

    private void demoGenerateCompanies() {
        this.companyService.save(new Company("ISSSR", true, "gmail.com"));
    }

    private void demoGenerateUsers() {
        this.userService.save(new User("Andrea", "Silvi", "andrea.silvi94@gmail.com", "password", this.companyService.findByName("ISSSR").get(), new ArrayList<>(Arrays.asList(roleService.findByName("ROLE_CUSTOMER").get()))));
        this.userService.save(new User("Alessio", "Vintari", "alessio.vintari@gmail.com", "password", this.companyService.findByName("ISSSR").get(), new ArrayList<>(Arrays.asList(roleService.findByName("ROLE_CUSTOMER").get()))));
        this.userService.save(new User("Luca", "Menzolini", "luca.menzolini@gmail.com", "password", this.companyService.findByName("ISSSR").get(), new ArrayList<>(Arrays.asList(roleService.findByName("ROLE_CUSTOMER").get()))));
        this.userService.save(new User("Tiziano", "Ditoma", "tiziano.ditoma@gmail.com", "password", this.companyService.findByName("ISSSR").get(), new ArrayList<>(Arrays.asList(roleService.findByName("ROLE_CUSTOMER").get()))));
        this.userService.save(new User("Simone", "Mancini", "2simonemancini5@gmail.com", "password", this.companyService.findByName("ISSSR").get(), new ArrayList<>(Arrays.asList(roleService.findByName("ROLE_CUSTOMER").get()))));
        this.userService.save(new User("Francesco", "Ottaviano", "francesco.ottaviano@gmail.com", "password", this.companyService.findByName("ISSSR").get(), new ArrayList<>(Arrays.asList(roleService.findByName("ROLE_CUSTOMER").get()))));
    }

    private void demoGenerateTeams() {
        Team systemTeam = this.teamService.save(new Team("Team 2", userService.findByEmail("andrea.silvi94@gmail.com").get()));

        systemTeam.getMembers().add(this.userService.findByEmail("alessio.vintari@gmail.com").get());
        systemTeam.getMembers().add(this.userService.findByEmail("luca.menzolini@gmail.com").get());
        systemTeam.getMembers().add(this.userService.findByEmail("tiziano.ditoma@gmail.com").get());
        systemTeam.getMembers().add(this.userService.findByEmail("2simonemancini5@gmail.com").get());
        systemTeam.getMembers().add(this.userService.findByEmail("francesco.ottaviano@gmail.com").get());

        this.teamService.save(systemTeam);
    }

    private void demoGenerateTargets() {
        this.targetService.save(new Target("ISSSR", "1.0"));
    }

    private void demoGenerateQueries() {
        this.queryService.create(new DBScheduledCountQuery(
                "DBScheduledCountQuery: This query check number of targets in target table, if it is grater than 1 generate alert ticket",
                ticketPriorityService.findByName("HIGH").get(),
                true,
                this.userService.findByEmail(ADMIN_EMAIL).get().getEmail(),
                "*/5 * * * * ?",
                "SELECT count(*) FROM ts_target",
                new DBConnectionInfo(null, null, null),
                QueryType.DATA_BASE_INSTANT_CHECK,
                ComparisonOperatorsEnum.GREATER,
                BigInteger.valueOf(1)));

        this.queryService.create(new DBScheduledCountQuery(
                "DBScheduledCountQuery: This query check number of teams in team table, if it is less than 1 generate alert ticket",
                ticketPriorityService.findByName("HIGH").get(),
                false,
                this.userService.findByEmail(ADMIN_EMAIL).get().getEmail(),
                "*/8 * * * * ?",
                "SELECT count(*) FROM ts_team",
                new DBConnectionInfo(null, null, null),
                QueryType.DATA_BASE_INSTANT_CHECK,
                ComparisonOperatorsEnum.LESS,
                BigInteger.valueOf(1)
        ));

        this.queryService.create(new DBScheduledCountQuery(
                "DBScheduledCountQuery: This query check for post 'Hello world!' inside wordpress in order to find comments containing error keyword ",
                ticketPriorityService.findByName("HIGH").get(),
                true,
                this.userService.findByEmail(ADMIN_EMAIL).get().getEmail(),
                "*/10 * * * * ?",
                "SELECT count(*) FROM wp_comments AS c JOIN wp_users AS u ON c.user_id = u.ID JOIN wp_posts AS p ON p.ID = c.comment_post_ID WHERE p.post_title = 'Hello world!' AND c.comment_content LIKE '%errore%'",
                new DBConnectionInfo("jdbc:mysql://localhost:3306/wordpress?useSSL=false", "root", "password"),
                QueryType.DATA_BASE_TABLE_MONITOR,
                ComparisonOperatorsEnum.GREATER_EQUALS,
                BigInteger.valueOf(1)
        ));

        this.queryService.create(new DBScheduledCountQuery(
                "Rapporto ultimo minuto: è stato rilevato più di 1 commento sotto il post denominato 'Supporto' riguardante il sistema 'ISSSR blog demo' che riporta le parole errore, bug o crash",
                ticketPriorityService.findByName("HIGH").get(),
                true,
                this.userService.findByEmail(ADMIN_EMAIL).get().getEmail(),
                "0 */1 * * * ?",
                "SELECT c.comment_content FROM wp_comments AS c JOIN wp_posts AS p ON p.ID = c.comment_post_ID WHERE p.post_title = 'Supporto'  AND c.comment_content LIKE 'ISSSR blog demo%' AND c.comment_content LIKE '%errore%'  OR '%bug%' OR '%crash%'",
                new DBConnectionInfo("jdbc:mysql://localhost:3306/wordpress?useSSL=false", "root", "password"),
                QueryType.DATA_BASE_TABLE_MONITOR,
                ComparisonOperatorsEnum.GREATER,
                BigInteger.valueOf(1)
        ));

        this.queryService.create(new DBScheduledCountQuery(
                "DBScheduledCountQuery: This query check for post 'Hello world!' inside wordpress in order to find comments containing error keyword ",
                ticketPriorityService.findByName("HIGH").get(),
                true,
                this.userService.findByEmail(ADMIN_EMAIL).get().getEmail(),
                "0 */2 * * * ?",
                "SELECT count(*) FROM ts_ticket AS t JOIN ts_ticketcategory AS tc ON t.category_id = tc.id WHERE tc.name = 'BUG'",
                new DBConnectionInfo(null, null, null),
                QueryType.DATA_BASE_TABLE_MONITOR,
                ComparisonOperatorsEnum.PERCENT_DROP,
                BigInteger.valueOf(20)
        ));
    }

    private void demoGenerateTickets() {
        this.ticketService.save(new Ticket(
                ticketStatusService.findByName("INITIALIZED").get(),
                ticketSourceService.findByName("CLIENT").get(),
                Instant.now(),
                ticketCategoryService.findByName("BUG").get(),
                "Found a bug!",
                "Hi,\nI'm Andrea, I would like you to inform that the system that you created is beautiful but there are some problems, you should fix all problems.\nSee you soon,\nAndrea",
                userService.findByEmail("andrea.silvi94@gmail.com").get(),
                targetService.findByName("ISSSR").get(),
                ticketPriorityService.findByName("HIGH").get(),
                visibilityService.findByName("PRIVATE").get()
        ));

        this.ticketService.save(new Ticket(
                ticketStatusService.findByName("INITIALIZED").get(),
                ticketSourceService.findByName("CLIENT").get(),
                Instant.now(),
                ticketCategoryService.findByName("ERROR").get(),
                "Error found inside form",
                "Hi,\nI'm Alessio, thanks for your amazing system, I'm experiencing some issues with your for during ticket creation.\nSee you soon,\nAlessio",
                userService.findByEmail("alessio.vintari@gmail.com").get(),
                targetService.findByName("ISSSR").get(),
                ticketPriorityService.findByName("HIGH").get(),
                visibilityService.findByName("PRIVATE").get()
        ));

        this.ticketService.save(new Ticket(
                ticketStatusService.findByName("INITIALIZED").get(),
                ticketSourceService.findByName("CLIENT").get(),
                Instant.now(),
                ticketCategoryService.findByName("FAILURE").get(),
                "Failure of browser",
                "Hi,\nI'm Luca, I would like you to inform that my browser crash instantly. Maybe the problem is my computer, I'm using Windows Vista and Internet explorer!\nSee you soon,\nLuca",
                userService.findByEmail("luca.menzolini@gmail.com").get(),
                targetService.findByName("ISSSR").get(),
                ticketPriorityService.findByName("HIGH").get(),
                visibilityService.findByName("PRIVATE").get()
        ));
    }
}