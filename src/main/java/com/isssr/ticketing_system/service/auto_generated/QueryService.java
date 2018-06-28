package com.isssr.ticketing_system.service.auto_generated;

import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.logger.aspect.LogOperation;
import com.isssr.ticketing_system.model.auto_generated.temporary.DataBaseTimeQuery;
import com.isssr.ticketing_system.model.db_connection.DBConnectionInfo;
import com.isssr.ticketing_system.repository.QueryRepository;
import com.isssr.ticketing_system.service.DBConnectionInfoService;
import com.isssr.ticketing_system.utils.PageableUtils;
import org.aspectj.lang.annotation.After;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.List;

@Service
public class QueryService {


    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private PageableUtils pageableUtils;

    @Autowired
    private AutoGeneratedTicketService autoGeneratedTicketService;

    @Autowired
    private DBConnectionInfoService dbConnectionInfoService;

    @Transactional
    @LogOperation(tag = "QUERY_CREATED", inputArgs = {"dataBaseTimeQuery"})
    public DataBaseTimeQuery create(DataBaseTimeQuery dataBaseTimeQuery) {

        //save or retrieve db connection info
        DBConnectionInfo dbConnectionInfo = this.dbConnectionInfoService.findByUrlAndUsernameAndPassword(
                dataBaseTimeQuery.getDbConnectionInfo().getUrl(),
                dataBaseTimeQuery.getDbConnectionInfo().getUsername(),
                dataBaseTimeQuery.getDbConnectionInfo().getPassword()
        );

        //update query connection info with id value
        dataBaseTimeQuery.setDbConnectionInfo(dbConnectionInfo);

        return this.queryRepository.save(dataBaseTimeQuery);
    }

    @Transactional
    public @NotNull DataBaseTimeQuery updateOne(@NotNull Long id, @NotNull DataBaseTimeQuery updatedData) throws UpdateException, EntityNotFoundException, SchedulerException, ParseException {

        if (!this.existsById(id))
            throw new EntityNotFoundException("Query to update not found in DB, maybe you have to create a new one");

        DataBaseTimeQuery updatingDataBaseTimeQuery = queryRepository.getOne(id);

        //check if query is active
        boolean isActive = updatingDataBaseTimeQuery.isActive();

        //if it is active
        if (isActive) {

            //disable query
            this.disableQuery(updatingDataBaseTimeQuery);

        }

        //update query
        updatingDataBaseTimeQuery.updateMe(updatedData);

        //save or retrieve db connection info
        DBConnectionInfo dbConnectionInfo = this.dbConnectionInfoService.findByUrlAndUsernameAndPassword(
                updatingDataBaseTimeQuery.getDbConnectionInfo().getUrl(),
                updatingDataBaseTimeQuery.getDbConnectionInfo().getUsername(),
                updatingDataBaseTimeQuery.getDbConnectionInfo().getPassword()
        );

        //update query connection info with id value
        updatingDataBaseTimeQuery.setDbConnectionInfo(dbConnectionInfo);

        if (isActive) {

            //activate query again
            this.activateQuery(updatingDataBaseTimeQuery);

        }

        return queryRepository.save(updatingDataBaseTimeQuery);
    }

    @Transactional
    public void simpleUpdateOne(Long id, DataBaseTimeQuery dataBaseTimeQuery) throws EntityNotFoundException {

        if (!this.existsById(id))
            throw new EntityNotFoundException("Query to update not found in DB, maybe you have to create a new one");


        queryRepository.save(dataBaseTimeQuery);

    }

    @Transactional
    public DataBaseTimeQuery findById(Long id) throws EntityNotFoundException {

        if (!this.existsById(id))
            throw new EntityNotFoundException("No query with this ID in db");

        return this.queryRepository.getOne(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.queryRepository.existsById(id);
    }

    @Transactional
    public Iterable<DataBaseTimeQuery> findAll() {
        return this.queryRepository.findAll();
    }

    @Transactional
    public Iterable<DataBaseTimeQuery> findAllById(Iterable<Long> ids) {
        return this.queryRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.queryRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.existsById(id);

        if (exists) {

            DataBaseTimeQuery query = this.queryRepository.getOne(id);

            if (query.isDeleted()) {

                this.queryRepository.deleteById(id);

            } else {

                query.delete();

                this.queryRepository.save(query);
            }
        }
        return exists;
    }

    @Transactional
    public DataBaseTimeQuery restoreById(Long id) throws EntityNotFoundException {

        if (this.existsById(id)) {

            DataBaseTimeQuery query = this.queryRepository.getOne(id);

            query.restore();

            return this.queryRepository.save(query);

        } else {
            throw new EntityNotFoundException("Trying to restore Target not present in db");
        }

    }

    @Transactional
    public void deleteAll() {
        this.queryRepository.deleteAll();
    }

    @Transactional
    public Page<DataBaseTimeQuery> findAll(Pageable pageable) {
        return this.queryRepository.findAll(pageable);
    }

    @Transactional
    public Page<DataBaseTimeQuery> findAll(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<DataBaseTimeQuery> retrievedPage = this.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    public Page<DataBaseTimeQuery> findAllNotDeleted(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<DataBaseTimeQuery> retrievedPage = this.findAllNotDeleted(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    public Page<DataBaseTimeQuery> findAllNotDeleted(PageRequest pageRequest) {
        return this.queryRepository.findAllNotDeleted(pageRequest);
    }

    @Transactional
    public Page<DataBaseTimeQuery> findAllDeleted(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<DataBaseTimeQuery> retrievedPage = this.findAllDeleted(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    public Page<DataBaseTimeQuery> findAllDeleted(PageRequest pageRequest) {
        return this.queryRepository.findAllDeleted(pageRequest);
    }

    @Transactional
    public boolean activateQuery(DataBaseTimeQuery query) throws ParseException, SchedulerException {

        //Be sure query is initialized, sometimes when obj is retrieved from db its initialization is postponed
        query = this.initializeAndUnproxyQuery(query);

        boolean activated = autoGeneratedTicketService.activateQuery(query);

        if (activated) {

            query.activeMe();

            this.queryRepository.save(query);

        }

        return activated;

    }

    @Transactional
    public boolean disableQuery(DataBaseTimeQuery query) throws SchedulerException {

        //Be sure query is initialized, sometimes when obj is retrieved from db its initialization is postponed
        query = this.initializeAndUnproxyQuery(query);


        boolean disabled = autoGeneratedTicketService.disableQuery(query);

        if (disabled) {

            query.disableMe();

            this.queryRepository.save(query);

        }

        return disabled;
    }

    @Transactional
    public List<DataBaseTimeQuery> findAllActiveQueries() {

        return this.queryRepository.findAllByActive(true);

    }

    public DataBaseTimeQuery initializeAndUnproxyQuery(DataBaseTimeQuery entity) {
        if (entity == null) {
            throw new
                    NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (DataBaseTimeQuery) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }
}
