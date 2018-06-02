package com.isssr.ticketing_system.service;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TicketServiceTest {

    private Long index = 154L; //index = last ID in DB + 2

    @Autowired
    private TicketService ticketService;

    @Test
    public void aCreate() throws Exception {
        /*Basic<Ticket> tickLis =  ticketService.findAll(0, null);
        int firstFindAll = tickLis.size();

        Long testId = index;
        Ticket testTicket = createTestTicket(testId);
        Ticket insertedTicket = ticketService.create(testTicket);

        assertTrue(testTicket.equals(insertedTicket));

        assertEquals(firstFindAll + 1, ticketService.findAll(0, null).size());*/


    }

    //Problem with attachements list
    @Test
    public void bFindOneByID() throws Exception {
        /*Long testId = index + 1;
        Ticket checkTicket = createTestTicket(testId);
        Ticket createdTicket = ticketService.create(checkTicket);
        Ticket foundTicket = ticketService.findOneByID(testId);

        //System.out.println(createdTicket.toString());
        //System.out.println(foundTicket.toString());


        assertTrue(createdTicket.equals(foundTicket));*/

    }

    @Test
    public void cFindAll() throws Exception {
        /*Long testId = index + 2;
        int firstFindAll = ticketService.findAll(0, null).size();
        Ticket testTicket = createTestTicket(testId);
        ticketService.create(testTicket);

        assertEquals(firstFindAll + 1, ticketService.findAll(0, null).size());*/
    }

    @Test
    public void dUpdateOne() throws Exception {
        /*Long testId = index;
        Ticket toUpdateTicket = new Ticket(testId, "updatedTicket", "this is a test ticket", TicketStatus.CLOSED,
                TicketSource.CUSTOMER, Instant.now(), 2L,
                TicketVisibility.PUBLIC, TicketCategory.CATEGORY_A);

        Ticket updatedTicket = ticketService.updateOne(testId, toUpdateTicket);
        assertTrue(toUpdateTicket.equals(updatedTicket));*/
    }

    @Test
    public void eDeleteOneByID() throws Exception {
        /*Long testId = index + 3;
        Basic<Ticket> ticketList = ticketService.findAll(0, null);
        int firstFindAll = ticketList.size();
        Ticket toDelete = createTestTicket(testId);
        ticketService.create(toDelete);
        assertTrue(ticketService.deleteOneByID(testId));
        assertEquals(firstFindAll, ticketService.findAll(0, null).size());*/
    }

    /*private Ticket createTestTicket(Long Id) throws Exception {
        return new Ticket(Id, "testTitle", "this is a test ticket", TicketStatus.PENDING,
                TicketSource.CUSTOMER, Instant.now(), 2L,
                TicketVisibility.PUBLIC, TicketCategory.CATEGORY_A);
    }*/
}