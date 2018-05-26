package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.model.auto_generated.ConcreteTimeQuery;
import com.isssr.ticketing_system.model.auto_generated.Query;
import com.isssr.ticketing_system.model.auto_generated.decorator.DataBaseQuery;
import com.isssr.ticketing_system.model.auto_generated.enumeration.ComparisonOperatorsEnum;
import com.isssr.ticketing_system.repository.ProductRepository;
import com.isssr.ticketing_system.repository.TeamRepository;
import com.isssr.ticketing_system.repository.TicketRepository;
import com.isssr.ticketing_system.service.TicketPriorityService;
import com.isssr.ticketing_system.service.auto_generated.AutoGeneratedTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Validated
@RestController
@RequestMapping(path = "/api/v1/queries")
public class QueryController {

    @Autowired
    private AutoGeneratedTicketService autoGeneratedTicketService;

    @Autowired
    private TicketPriorityService ticketPriorityService;

    /*@Autowired
    private TaskScheduler taskScheduler;*/

    //@PostConstruct
    public void testInit() throws NoSuchMethodException {

        Query dbIntegerQuery = new DataBaseQuery(
                new ConcreteTimeQuery(
                        null,
                        "Product number check every 5 seconds",
                        ticketPriorityService.findByName("LOW").get(),
                        "*/5 * * * * ?"

                ),
                ProductRepository.class,
                ProductRepository.class.getMethod("count"),
                5L,
                ComparisonOperatorsEnum.LESS_EQUALS,
                new ArrayList<Long>()
        );

        dbIntegerQuery.addObserver(autoGeneratedTicketService);
        autoGeneratedTicketService.activateQuery(dbIntegerQuery);

        dbIntegerQuery = new DataBaseQuery(
                new ConcreteTimeQuery(
                        null,
                        "Team number check every 8 seconds",
                        ticketPriorityService.findByName("MEDIUM").get(),
                        "*/8 * * * * ?"

                ),
                TeamRepository.class,
                TeamRepository.class.getMethod("count"),
                5L,
                ComparisonOperatorsEnum.LESS_EQUALS,
                new ArrayList<Long>()
        );

        dbIntegerQuery.addObserver(autoGeneratedTicketService);
        autoGeneratedTicketService.activateQuery(dbIntegerQuery);

        dbIntegerQuery = new DataBaseQuery(
                new ConcreteTimeQuery(
                        null,
                        "Ticket number check every 10 seconds",
                        ticketPriorityService.findByName("HIGH").get(),
                        "*/10 * * * * ?"

                ),
                TicketRepository.class,
                TicketRepository.class.getMethod("count"),
                5L,
                ComparisonOperatorsEnum.LESS_EQUALS,
                new ArrayList<Long>()
        );

        dbIntegerQuery.addObserver(autoGeneratedTicketService);
        autoGeneratedTicketService.activateQuery(dbIntegerQuery);

    }
}
