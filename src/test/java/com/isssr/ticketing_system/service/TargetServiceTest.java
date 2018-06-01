package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.exception.PageableQueryException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TargetServiceTest {

    private Long index = 134L; //index = last ID in DB + 2

    @Autowired
    private TargetService targetService;

    @Test
    public void Acreate() throws EntityNotFoundException, PageableQueryException {

        /*List<Target> prodLis =  productService.findAll(0, null);
        int firstFindAll = prodLis.size();

        Long testId = index;
        Target testProduct = createRandomProduct(testId);
        Target insertedProduct = productService.create(testProduct);

        assertTrue(testProduct.equals(insertedProduct));

        assertEquals(firstFindAll + 1, productService.findAll(0, null).size());*/


    }

    @Test
    public void BfindOneById() {
        /*Long testId = index + 1;
        Target checkProduct = createRandomProduct(testId);
        Target createdProduct = productService.create(checkProduct);
        Target foundProduct = productService.findOneById(testId);
        System.out.println(createdProduct.toString());
        System.out.println(foundProduct.toString());
        System.out.println(createdProduct.equals(foundProduct));

        assertTrue(createdProduct.equals(foundProduct));*/

    }

    @Test
    public void CfindAll() throws EntityNotFoundException, PageableQueryException {
        /*Long testId = index + 2;
        int firstFindAll = productService.findAll(0, null).size();
        Target testProduct = createRandomProduct(testId);
        productService.create(testProduct);

        assertEquals(firstFindAll + 1, productService.findAll(0, null).size());*/
    }

    @Test
    public void DupdateOne() throws Exception {
        /*Long testId = index;
        Target toUpdateProduct = new Target(testId, "updated", "ver2", new HashSet<Ticket>());

        Target updatedProduct = productService.updateOne(testId, toUpdateProduct);
        assertTrue(toUpdateProduct.equals(updatedProduct));*/
    }

    @Test
    public void EdeleteOneById() throws EntityNotFoundException, PageableQueryException {
        /*Long testId = index + 3;
        List<Target> prodList = productService.findAll(0 , null);
        int firstFindAll = prodList.size();
        Target toDelete = createRandomProduct(testId);
        productService.create(toDelete);
        assertTrue(productService.deleteOneById(testId));
        assertEquals(firstFindAll, productService.findAll(0, null).size());*/

    }

    /*private Target createRandomProduct(long Id){
        Target proudct = null;
        HashSet<Ticket> hashSet = new HashSet<Ticket>();
        try {
            proudct = new Target(Id, "testProd", "testVer", hashSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proudct;
    }*/
}