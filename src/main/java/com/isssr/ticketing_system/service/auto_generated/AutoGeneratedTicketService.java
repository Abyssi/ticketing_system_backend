package com.isssr.ticketing_system.service.auto_generated;

import com.isssr.ticketing_system.model.Ticket;
import com.isssr.ticketing_system.model.auto_generated.scheduler.TaskScheduler;
import com.isssr.ticketing_system.model.auto_generated.temporary.DataBaseTimeQuery;
import com.isssr.ticketing_system.repository.QueryRepository;
import com.isssr.ticketing_system.repository.TargetRepository;
import com.isssr.ticketing_system.repository.TeamRepository;
import com.isssr.ticketing_system.repository.TicketRepository;
import com.isssr.ticketing_system.service.*;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Service
public class AutoGeneratedTicketService implements Observer {

    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    private List<DataBaseTimeQuery> activeQueries = new ArrayList<>();

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private UserSwitchService userSwitchService;

    @Autowired
    private QueryRepository queryRepository;

    /**
     * external services
     **/
    @Autowired
    private TicketStatusService ticketStatusService;

    @Autowired
    private TicketSourceService ticketSourceService;

    @Autowired
    private TicketCategoryService ticketCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private TargetService targetService;

    @Autowired
    private VisibilityService visibilityService;

    /**
     * get all queries in db and start schedule them
     */
    public void startScheduling() throws ParseException, SchedulerException {

        /*if (this.activeQueries != null)
            return;

        this.activeQueries = new ArrayList<>();

        this.activeQueries = this.queryRepository.findAllByActiveTrue();*/

        List<DataBaseTimeQuery> dataBaseTimeQueryList = this.queryRepository.findAllByActive(true);

        for (int i = 0; i < dataBaseTimeQueryList.size(); i++) {

            DataBaseTimeQuery dataBaseTimeQuery = dataBaseTimeQueryList.get(i);

            this.activateQuery(dataBaseTimeQuery);

        }

    }


    @Override
    public void update(Observable o, Object arg) {

        /*System.out.println("TimeQuery: " + ((Query) o).printQuery() + " --> RUNNING!");

        JpaRepository jpaRepository = null;

        if (o instanceof DataBaseQuery) {

            String repositoryName = ((DataBaseQuery) o).getReferenceClass().getName();

            switch (repositoryName) {
                case "com.isssr.ticketing_system.repository.ProductRepository":

                    jpaRepository = this.productRepository;
                    break;
                case "com.isssr.ticketing_system.repository.TeamRepository":

                    jpaRepository = this.teamRepository;
                    break;
                case "com.isssr.ticketing_system.repository.TicketRepository":

                    jpaRepository = this.ticketRepository;
                    break;
            }

        }

        Boolean result = ((Query) o).executeQuery(jpaRepository);

        if (result != null) {

            if (result) {

                try {

                    generateTicket((Query) o);

                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO generate system error ticket
                    return;
                }
            }

        }


        /*switch (((DataBaseTimeQuery) o).getComparisonOperator()) {
            case EQUALS:

                try {
                    System.out.println("Repository: " + jpaRepository.toString() + "  -->  Elementi presenti: " + ((DataBaseTimeQuery) o).getMethodToCall().invoke(jpaRepository ));
                    if (((DataBaseTimeQuery) o).getMethodToCall().invoke(jpaRepository ) == (Integer) ((DataBaseTimeQuery) o).getReferenceValue())
                        System.out.println("Ticket generated");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
        }*/

        /*if (((DataBaseTimeQuery) o).executeQuery(this.userSwitchService)) {
            generateTicket((DataBaseTimeQuery) o);
        }*/


    }

    public void generateTicket(DataBaseTimeQuery query) {

        Ticket ticket = new Ticket(
                ticketStatusService.findByName("PENDING").get(),
                ticketSourceService.findByName("SYSTEM").get(),
                Instant.now(),
                ticketCategoryService.findByName("SYSTEM").get(),
                "Auto generated ticket",
                query.getDescription(),
                targetService.findByName("System").get(),
                query.priority(),
                visibilityService.findByName("PRIVATE").get()
        );

        ticketService.save(ticket);

    }

    /*public void activateQuery(Query query) {
        taskScheduler.addJob(query);
        this.activeQuery.add(query);
        System.out.println("DataBaseTimeQuery: " + query.printQuery() + " --> SCHEDULED");
    }*/

    public boolean activateQuery(DataBaseTimeQuery query) throws ParseException, SchedulerException {

        taskScheduler.addJob(query);

        this.activeQueries.add(query);

        System.out.println("DataBaseTimeQuery: " + query.printQuery() + " --> SCHEDULED");

        return true;

    }


    public boolean disableQuery(DataBaseTimeQuery query) throws SchedulerException {

        taskScheduler.removeJob(query);

        boolean removed = this.activeQueries.remove(query);

        System.out.println("DataBaseTimeQuery: " + query.printQuery() + " --> REMOVED with value " + removed);

        return removed;

    }
}
