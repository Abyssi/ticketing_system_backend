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
public class TeamServiceTest {

    private Long index = 119L; //index = last ID in DB + 2

    @Autowired
    private TeamService teamService;

    @Test
    public void aCreate() throws Exception {

        /*List<Team> teamList =  teamService.findAll(0, null);
        int firstFindAll = teamList.size();

        Long testId = index;
        Team testTeam = createTestTeam(testId);
        Team insertedTeam = teamService.create(testTeam);

        assertTrue(testTeam.equals(insertedTeam));

        assertEquals(firstFindAll + 1, teamService.findAll(0, null).size());*/


    }

    @Test
    public void bFindOneByID() throws Exception {

        /*Long testId = index + 1;
        Team checkTeam = createTestTeam(testId);
        Team createdTeam = teamService.create(checkTeam);
        Team foundTeam = teamService.findOneByID(testId);

        assertTrue(createdTeam.equals(foundTeam));*/

    }

    @Test
    public void cFindAll() throws Exception {
        /*Long testId = index + 2;
        int firstFindAll = teamService.findAll(0, null).size();
        Team testTeam = createTestTeam(testId);
        teamService.create(testTeam);

        assertEquals(firstFindAll + 1, teamService.findAll(0, null).size());*/

    }


    /*@Test
    public void dUpdateOne() throws Exception {
        Long testId = index;
        Team toUpdateTeam = new Team(testId, new TeamMember(1L, "testUser", "test", "test@mail",
                "password", true), new ArrayList<TeamMember>());

        Team updatedTeam = teamService.updateOne(testId, toUpdateTeam);
        assertTrue(toUpdateTeam.equals(updatedTeam));
    }*/

    @Test
    public void eAddAssistantToTeam() throws Exception {
        /*Long testId = index + 1;
        TeamMember toAddMember = new TeamMember(10L, "testTeamMember", "test", "test@mail",
                "password", false);
        Team checkTeam = teamService.findOneByID(testId);
        checkTeam.addAssistant(testId, toAddMember);
        Team updatedTeam = teamService.addAssistantToTeam(testId, toAddMember);

        assertTrue(checkTeam.equals(updatedTeam));*/

    }

    @Test
    public void addAssistantListToTeam() {
    }

    @Test
    public void removeAssistantFromTeam() {
    }

    @Test
    public void removeAssistantListFromTeam() {
    }

    @Test
    public void addProductToTeam() {
    }

    @Test
    public void addProductListToTeam() {
    }

    @Test
    public void removeProductFromTeam() {
    }

    @Test
    public void removeProductListFromTeam() {
    }

    @Test
    public void fDeleteOneByID() throws Exception {
        /*Long testId = index + 3;
        List<Team> teamList = teamService.findAll(0, null);
        int firstFindAll = teamList.size();
        Team toDelete = createTestTeam(testId);
        teamService.create(toDelete);
        assertTrue(teamService.deleteOneByID(testId));
        assertEquals(firstFindAll, teamService.findAll(0, null).size());*/
    }

    /*private Team createTestTeam(Long testId) throws Exception {
        List<TeamMember> assistantList = new ArrayList<TeamMember>();
        TeamMember assistant =  new TeamMember(23L, "testTeamMember", "test", "test@mail",
                "password", false);
        assistantList.add(assistant);
        TeamMember teamManager = new TeamMember(24L, "testTeamManager", "test", "test@mail",
                "password", true);
        //Team team = new Team(testId, teamManager, assistantList);
        HashSet<Ticket> hashSet = new HashSet<>();
        Target product = new Target(1L, "testProd", "testVer", hashSet);
        //team.addProduct(testId, product);
        return  null;
    }*/
}