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
public class UserServiceTest {

    private Long index = 30L; //index = last ID in DB + 2

    @Autowired
    private UserService userService;

    @Test
    public void aCreateUser() throws Exception {
        /*
        Basic<User> userList =  userService.findAll();
        int firstFindAll = userList.size();

        Long testId = index;
        Customer testCustomer = createCustomer(testId);
        Customer insertedCustomer = (Customer) userService.createUser(testCustomer);

        assertTrue(testCustomer.equals(insertedCustomer));

        testId = index + 1;
        TeamMember testTeamMember = createTeamMember(testId);
        TeamMember insertedTeamMember =  (TeamMember) userService.createUser(testTeamMember);

        assertTrue(testTeamMember.equals(insertedTeamMember));

        testId = index + 2;
        TeamMember testTeamManager = createTeamManager(testId);
        TeamMember insertedTeamManager = (TeamMember) userService.createUser(testTeamManager);

        assertTrue(testTeamManager.equals(insertedTeamManager));

        assertEquals(firstFindAll + 3, userService.findAll().size());
        */
    }

    @Test
    public void bFindOneByID() throws Exception {
        /*
        Long testId = index + 3;
        Customer checkCustomer = createCustomer(testId);
        Customer createdCustomer = (Customer) userService.createUser(checkCustomer);
        User foundCustomer =  userService.findOneByID(testId);

        assertTrue(createdCustomer.equals(foundCustomer));
        */
    }

    @Test
    public void cFindAll() throws Exception {
        /*
        Long testId = index + 4;
        int firstFindAll = userService.findAll().size();
        Customer testCustomer = createCustomer(testId);
        userService.createUser(testCustomer);

        assertEquals(firstFindAll + 1, userService.findAll().size());
        */
    }

    @Test
    public void findAllTeamMembers() {
    }

    @Test
    public void findAllByEmail() {
    }

    @Test
    public void dUpdateUser() throws Exception {
        /*
        Long testId = index;
        Customer toUpdateCustomer = new Customer(testId, "updatedCustomer", "test", "updated@mail", "password");

        Customer updatedCustomer = (Customer) userService.updateUser(testId, toUpdateCustomer);
        assertTrue(toUpdateCustomer.equals(updatedCustomer));
        */
    }

    @Test
    public void eDeleteOneByID() throws Exception {
        /*
        Long testId = index + 5;
        Basic<User> userList = userService.findAll();
        int firstFindAll = userList.size();
        Customer toDelete = createCustomer(testId);
        userService.createUser(toDelete);
        assertTrue(userService.deleteOneByID(testId));
        assertEquals(firstFindAll, userService.findAll().size());
*/
    }

    /*
    private Customer createCustomer(Long ID) throws Exception {
        return new Customer(ID, "testAssistant", "test", "test@mail",
                "password");
    }

    private TeamMember createTeamMember(Long ID) throws Exception {
        return new TeamMember(ID, "testTeamMember", "test", "test@mail",
                "password", false);
    }

    private TeamMember createTeamManager(Long ID) throws Exception {
        return new TeamMember(ID, "testTeamManager", "test", "test@mail",
                "password", true);
    }
    */
}