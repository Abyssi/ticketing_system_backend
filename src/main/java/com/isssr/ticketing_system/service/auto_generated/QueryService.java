package com.isssr.ticketing_system.service.auto_generated;

import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.logger.aspect.LogOperation;
import com.isssr.ticketing_system.model.auto_generated.query.DBScheduledQuery;
import com.isssr.ticketing_system.model.auto_generated.query.Query;
import com.isssr.ticketing_system.model.auto_generated.query.ScheduledQuery;
import com.isssr.ticketing_system.model.db_connection.DBConnectionInfo;
import com.isssr.ticketing_system.repository.QueryRepository;
import com.isssr.ticketing_system.service.DBConnectionInfoService;
import com.isssr.ticketing_system.utils.PageableUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

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

    /**
     * general create query
     **/
    @Transactional
    public Query create(Query query) {

        if (query instanceof DBScheduledQuery) {

            return this.createDBScheduledQuery((DBScheduledQuery) query);

        }

        return null;

    }

    /**
     * create DB SCHEDULED QUERY
     **/
    @Transactional
    @LogOperation(tag = "QUERY_CREATED", inputArgs = {"query"})
    private DBScheduledQuery createDBScheduledQuery(DBScheduledQuery query) {

        //save or retrieve db connection info
        DBConnectionInfo dbConnectionInfo = this.dbConnectionInfoService.findByUrlAndUsernameAndPassword(
                query.getDbConnectionInfo().getUrl(),
                query.getDbConnectionInfo().getUsername(),
                query.getDbConnectionInfo().getPassword()
        );

        //update query connection info with id value
        ((DBScheduledQuery) query).setDbConnectionInfo(dbConnectionInfo);

        return this.queryRepository.save(query);

    }

    @Transactional
    public @NotNull Query updateOne(@NotNull Long id, @NotNull Query updatedData) throws UpdateException, EntityNotFoundException, SchedulerException, ParseException {

        if (!this.existsById(id))
            throw new EntityNotFoundException("Query to update not found in DB, maybe you have to create a new one");

        Query updatingQuery = this.queryRepository.getOne(id);


        if (!updatingQuery.equalsByClass(updatedData))
            throw new UpdateException("Query class doesn't match");

        //check if query is active
        boolean isActive = updatingQuery.isActive();

        //if it is active
        if (isActive) {

            //disable query
            this.disableQuery(updatingQuery);

        }

        //update query
        updatingQuery.updateMe(updatedData);

        updatingQuery = this.initializeAndUnproxyQuery(updatingQuery);

        //if it is a db query retrieve db connection info
        if (updatingQuery instanceof DBScheduledQuery) {



            //save or retrieve db connection info
            DBConnectionInfo dbConnectionInfo = this.dbConnectionInfoService.findByUrlAndUsernameAndPassword(
                    ((DBScheduledQuery) updatingQuery).getDbConnectionInfo().getUrl(),
                    ((DBScheduledQuery) updatingQuery).getDbConnectionInfo().getUsername(),
                    ((DBScheduledQuery) updatingQuery).getDbConnectionInfo().getPassword()
            );

            //update query connection info with id value
            ((DBScheduledQuery) updatingQuery).setDbConnectionInfo(dbConnectionInfo);

        }

        if (isActive) {

            //activate query again
            this.activateQuery(updatingQuery);

        }

        return queryRepository.save(updatingQuery);
    }

    @Transactional
    public void simpleUpdateOne(Long id, Query query) throws EntityNotFoundException {

        if (!this.existsById(id))
            throw new EntityNotFoundException("Query to update not found in DB, maybe you have to create a new one");


        queryRepository.save(query);

    }

    @Transactional
    public Query findById(Long id) throws EntityNotFoundException {

        if (!this.existsById(id))
            throw new EntityNotFoundException("No query with this ID in db");

        return this.queryRepository.getOne(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.queryRepository.existsById(id);
    }

    @Transactional
    public Iterable<Query> findAll() {
        return this.queryRepository.findAll();
    }

    @Transactional
    public Iterable<Query> findAllById(Iterable<Long> ids) {
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

            Query query = this.queryRepository.getOne(id);

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
    public Query restoreById(Long id) throws EntityNotFoundException {

        if (this.existsById(id)) {

            Query query = this.queryRepository.getOne(id);

            query.restore();

            return this.queryRepository.save(query);

        } else {
            throw new EntityNotFoundException("Trying to restore Query not present in db");
        }

    }

    @Transactional
    public void deleteAll() {
        this.queryRepository.deleteAll();
    }

    @Transactional
    public Page<Query> findAll(Pageable pageable) {
        return this.queryRepository.findAll(pageable);
    }

    @Transactional
    public Page<Query> findAll(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Query> retrievedPage = this.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    public Page<Query> findAllNotDeleted(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Query> retrievedPage = this.findAllNotDeleted(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    public Page<Query> findAllNotDeleted(PageRequest pageRequest) {
        return this.queryRepository.findAllByDeleted(false, pageRequest);
    }

    @Transactional
    public Page<Query> findAllDeleted(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Query> retrievedPage = this.findAllDeleted(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    public Page<Query> findAllDeleted(PageRequest pageRequest) {
        return this.queryRepository.findAllByDeleted(true, pageRequest);
    }

    @Transactional
    public boolean activateQuery(Query query) throws ParseException, SchedulerException {

        //Be sure query is initialized, sometimes when obj is retrieved from db its initialization is postponed
        query = this.initializeAndUnproxyQuery(query);

        if (query instanceof ScheduledQuery) {

            boolean activated = autoGeneratedTicketService.activateQuery((ScheduledQuery) query);

            if (activated) {

                query.activeMe();

                this.queryRepository.save(query);

            }

            return activated;

        } else {

            query.activeMe();

            this.queryRepository.save(query);

            return true;
        }

    }

    @Transactional
    public boolean disableQuery(Query query) throws SchedulerException {

        //Be sure query is initialized, sometimes when obj is retrieved from db its initialization is postponed
        query = this.initializeAndUnproxyQuery(query);

        if (query instanceof ScheduledQuery) {


            boolean disabled = autoGeneratedTicketService.disableQuery((ScheduledQuery) query);

            if (disabled) {

                query.disableMe();

                this.queryRepository.save(query);

            }

            return disabled;

        } else {

            query.disableMe();

            this.queryRepository.save(query);

            return true;

        }

    }

    @Transactional
    public List<Query> findAllActiveQueries() {

        return this.queryRepository.findAllByActive(true);

    }

    public Query initializeAndUnproxyQuery(Query entity) {

        if (entity == null) {
            throw new
                    NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);

        if (entity instanceof HibernateProxy) {

            entity = (Query) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();

        }

        return entity;
    }
}
