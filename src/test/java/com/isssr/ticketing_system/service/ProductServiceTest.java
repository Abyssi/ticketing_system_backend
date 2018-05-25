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
public class ProductServiceTest {

    private Long index = 134L; //index = last ID in DB + 2

    @Autowired
    private ProductService productService;

    @Test
    public void Acreate() throws EntityNotFoundException, PageableQueryException {

        /*List<Product> prodLis =  productService.findAll(0, null);
        int firstFindAll = prodLis.size();

        Long testId = index;
        Product testProduct = createRandomProduct(testId);
        Product insertedProduct = productService.create(testProduct);

        assertTrue(testProduct.equals(insertedProduct));

        assertEquals(firstFindAll + 1, productService.findAll(0, null).size());*/


    }

    @Test
    public void BfindOneById() {
        /*Long testId = index + 1;
        Product checkProduct = createRandomProduct(testId);
        Product createdProduct = productService.create(checkProduct);
        Product foundProduct = productService.findOneById(testId);
        System.out.println(createdProduct.toString());
        System.out.println(foundProduct.toString());
        System.out.println(createdProduct.equals(foundProduct));

        assertTrue(createdProduct.equals(foundProduct));*/

    }

    @Test
    public void CfindAll() throws EntityNotFoundException, PageableQueryException {
        /*Long testId = index + 2;
        int firstFindAll = productService.findAll(0, null).size();
        Product testProduct = createRandomProduct(testId);
        productService.create(testProduct);

        assertEquals(firstFindAll + 1, productService.findAll(0, null).size());*/
    }

    @Test
    public void DupdateOne() throws Exception {
        /*Long testId = index;
        Product toUpdateProduct = new Product(testId, "updated", "ver2", new HashSet<Ticket>());

        Product updatedProduct = productService.updateOne(testId, toUpdateProduct);
        assertTrue(toUpdateProduct.equals(updatedProduct));*/
    }

    @Test
    public void EdeleteOneById() throws EntityNotFoundException, PageableQueryException {
        /*Long testId = index + 3;
        List<Product> prodList = productService.findAll(0 , null);
        int firstFindAll = prodList.size();
        Product toDelete = createRandomProduct(testId);
        productService.create(toDelete);
        assertTrue(productService.deleteOneById(testId));
        assertEquals(firstFindAll, productService.findAll(0, null).size());*/

    }

    /*private Product createRandomProduct(long Id){
        Product proudct = null;
        HashSet<Ticket> hashSet = new HashSet<Ticket>();
        try {
            proudct = new Product(Id, "testProd", "testVer", hashSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proudct;
    }*/
}