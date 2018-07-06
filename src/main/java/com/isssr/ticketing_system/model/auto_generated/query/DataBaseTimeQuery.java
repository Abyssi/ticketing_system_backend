package com.isssr.ticketing_system.model.auto_generated.query;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.logEnabler.LogEnabler;
import com.isssr.ticketing_system.model.db_connection.DBConnectionInfo;
import com.isssr.ticketing_system.model.SoftDelete.SoftDeletable;
import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.model.auto_generated.enumeration.ComparisonOperatorsEnum;
import com.isssr.ticketing_system.response_entity.JsonViews;
import com.isssr.ticketing_system.service.UserSwitchService;
import com.isssr.ticketing_system.service.auto_generated.QueryService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.*;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Observable;

/*@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "data_base_time_query")
@DynamicInsert
@DynamicUpdate

@PersistJobDataAfterExecution //persist data after execution of a job
@DisallowConcurrentExecution //avoid race condition on persisted data
@FilterDef(name = "deleted_filter", parameters = {@ParamDef(name = "value", type = "boolean")})
@Filter(name = "deleted_filter", condition = "deleted = :value")
@Component
//@LogClass(idAttrs = {"id"})
public class DataBaseTimeQuery extends Observable implements Job, Serializable, SoftDeletable {

    @Transient
    public static final String MAP_ME = "ME";

    @Transient
    @Value("${spring.datasource.url}")
    private String defaultDBUrl;

    @JsonView(JsonViews.Basic.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "query_id", updatable = false, nullable = false)
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String description;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String queryText;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    @ManyToOne
    private TicketPriority queryPriority;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String cron;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private boolean active = true;

    private boolean deleted;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    private ComparisonOperatorsEnum comparisonOperator;

    @JsonView(JsonViews.Detailed.class)
    private BigInteger referenceValue;

    private BigInteger lastValue;

    @JsonView(JsonViews.Detailed.class)
    @ManyToOne
    private DBConnectionInfo dbConnectionInfo;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    private QueryType queryType;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private boolean isEnable;

    private JobKey jobKey; //key in scheduler at runtime execution

    @Transient
    @Autowired
    private LogEnabler logEnabler;

    public DataBaseTimeQuery(String description, String queryText, TicketPriority queryPriority, String cron,
                             boolean active, boolean deleted, ComparisonOperatorsEnum comparisonOperator,
                             BigInteger referenceValue, BigInteger lastValue, QueryType queryType, boolean isEnable) {
        this.description = description;
        this.queryText = queryText;
        this.queryPriority = queryPriority;
        this.cron = cron;
        this.active = active;
        this.deleted = deleted;
        this.comparisonOperator = comparisonOperator;
        this.referenceValue = referenceValue;
        this.lastValue = lastValue;
        this.queryType = queryType;
        this.isEnable = isEnable;
    }

    public void wakeUp() {

        System.out.println("Observers on query " + this.countObservers());
        //notify observers
        setChanged();
        notifyObservers();

    }

    public String printQuery() {

        return this.queryText;

    }

    public Boolean executeQuery(UserSwitchService userSwitchService, QueryService queryService) throws SQLException, DataAccessException {

        switch (this.queryType) {

            case DATA_BASE_INSTANT_CHECK:

                return this.executeInstantCheck(userSwitchService);

            case DATA_BASE_TABLE_MONITOR:

                return this.executeMonitorCheck(userSwitchService, queryService);

        }

        return false;

    }

    private Boolean executeMonitorCheck(UserSwitchService userSwitchService, QueryService queryService) throws SQLException, DataAccessException {

        BigInteger count = this.executeSQL(userSwitchService);

        if (this.lastValue == null) {

            //update last value
            this.lastValue = count;

            //update query
            try {

                queryService.simpleUpdateOne(this.id, this);

            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }

            // It's first time running this query.
            // It is equivalent to run an instant check.
            // So return simply false, because is not a monitor action
            return false;

        } else {

            BigInteger difference = count.andNot(this.lastValue);

            //update last value
            this.lastValue = count;

            try {

                queryService.simpleUpdateOne(this.id, this);

            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }

            return this.compare(difference);

        }

    }

    private Boolean executeInstantCheck(UserSwitchService userSwitchService) throws SQLException, DataAccessException {

        BigInteger count = this.executeSQL(userSwitchService);

        return this.compare(count);

    }

    private BigInteger executeSQL(UserSwitchService userSwitchService) throws SQLException, DataAccessException {

        if (this.dbConnectionInfo == null) {

           return userSwitchService.doNotLog(this.queryText , BigInteger.class, null, null, null, this.isEnable);

        } else {

            //if (!this.isEnable)

            return userSwitchService.doNotLog(this.queryText , BigInteger.class, this.dbConnectionInfo.getUrl(), this.dbConnectionInfo.getUsername(), this.dbConnectionInfo.getPassword(), this.isEnable);

            /*else count = (BigInteger) userSwitchService
                    .doQueryReadOnlyMode(
                            this.queryText,
                            BigInteger.class,
                            this.dbConnectionInfo.getUrl(),
                            this.dbConnectionInfo.getUsername(),
                            this.dbConnectionInfo.getPassword()
                    );*/

       /* }

    }

    private boolean compare(BigInteger value) {

        int comparison = value.compareTo(this.referenceValue);

        switch (this.comparisonOperator) {

            case LESS:

                return comparison < 0;

            case EQUALS:

                return comparison == 0;

            case GREATER:

                return comparison > 0;

            case LESS_EQUALS:

                return comparison <= 0;

            case GREATER_EQUALS:

                return comparison >= 0;

        }

        return false;

    }

    public TicketPriority priority() {

        return this.queryPriority;

    }

    @Override
    public void delete() {
        this.deleted = true;
    }

    @Override
    public void restore() {
        this.deleted = false;
    }

    @Override
    public boolean isDeleted() {
        return this.deleted;
    }

    public void markMeAsNotActive() {

        this.active = false;

    }

    public void activeMe() {

        this.active = true;

    }

    public boolean isActive() {

        return active;

    }

    public void disableMe() {

        this.active = false;

    }

    public void updateMe(DataBaseTimeQuery updatedData) throws UpdateException {

        if (this.id.longValue() != updatedData.id.longValue())
            throw new UpdateException("Attempt to update a data base time query record without ID matching");

        this.description = updatedData.description;

        this.queryText = updatedData.queryText;

        this.queryPriority = updatedData.queryPriority;

        this.cron = updatedData.cron;

        this.comparisonOperator = updatedData.comparisonOperator;

        this.referenceValue = updatedData.referenceValue;

        this.lastValue = updatedData.lastValue;

        this.queryType = updatedData.queryType;

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        //get data map
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        //extract job from data map
        DataBaseTimeQuery dataBaseTimeQuery = (DataBaseTimeQuery) jobDataMap.get(MAP_ME);

        //activate query
        dataBaseTimeQuery.wakeUp();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataBaseTimeQuery)) {
            return false;
        }

        DataBaseTimeQuery other = (DataBaseTimeQuery) o;

        return this.id.equals(other.id);
    }

    public String toMailPrettyString() {

        String dbUrl = dbConnectionInfo.getUrl() != null ? dbConnectionInfo.getUrl() : this.defaultDBUrl;

        return String.format(
                "Query information: \nID: %d%n\nDESCRIPTION: %s\nSQL: %s\nDB URL: %s\nTYPE: %s",
                this.id,
                this.description,
                this.queryText,
                dbUrl,
                this.queryType.toString());

    }
}*/

@Data
@NoArgsConstructor

@Entity
@Table(name = "data_base_time_query")
@DynamicInsert
@DynamicUpdate

@PersistJobDataAfterExecution //persist data after execution of a job
@DisallowConcurrentExecution //avoid race condition on persisted data
@FilterDef(name = "deleted_filter", parameters = {@ParamDef(name = "value", type = "boolean")})
@Filter(name = "deleted_filter", condition = "deleted = :value")
//@LogClass(idAttrs = {"id"})
public class DataBaseTimeQuery extends DBScheduledQuery<BigInteger, ComparisonOperatorsEnum> {

    public DataBaseTimeQuery(String description, TicketPriority queryPriority, boolean isEnable,
                             String author, String cron, String queryText, DBConnectionInfo dbConnectionInfo,
                             QueryType queryType, ComparisonOperatorsEnum comparisonOperator, BigInteger referenceValue) {
        super(description, queryPriority, isEnable, author, cron, queryText, dbConnectionInfo,
                queryType, comparisonOperator, referenceValue);
    }

    public DataBaseTimeQuery(String description, TicketPriority queryPriority, boolean active, boolean deleted,
                             boolean isEnable, String author, String cron, String queryText,
                             DBConnectionInfo dbConnectionInfo, QueryType queryType,
                             ComparisonOperatorsEnum comparisonOperator, BigInteger referenceValue) {
        super(description, queryPriority, active, deleted, isEnable, author, cron, queryText,
                dbConnectionInfo, queryType, comparisonOperator, referenceValue);
    }

    public void wakeUp() {

        System.out.println("Observers on query " + this.countObservers());
        //notify observers
        setChanged();
        notifyObservers();

    }

    public Boolean executeQuery(UserSwitchService userSwitchService, QueryService queryService) throws SQLException, DataAccessException {

        switch (this.queryType) {

            case DATA_BASE_INSTANT_CHECK:

                return this.executeInstantCheck(userSwitchService);

            case DATA_BASE_TABLE_MONITOR:

                return this.executeMonitorCheck(userSwitchService, queryService);

        }

        return false;

    }

    private Boolean executeMonitorCheck(UserSwitchService userSwitchService, QueryService queryService) throws SQLException, DataAccessException {

        BigInteger count = this.executeSQL(userSwitchService);

        if (this.lastValue == null) {

            //update last value
            this.lastValue = count;

            //update query
            try {

                queryService.simpleUpdateOne(this.id, this);

            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }

            // It's first time running this query.
            // It is equivalent to run an instant check.
            // So return simply false, because is not a monitor action
            return false;

        } else {

            BigInteger difference = count.andNot(this.lastValue);

            //update last value
            this.lastValue = count;

            try {

                queryService.simpleUpdateOne(this.id, this);

            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }

            return this.compare(difference);

        }

    }

    private Boolean executeInstantCheck(UserSwitchService userSwitchService) throws SQLException, DataAccessException {

        BigInteger count = this.executeSQL(userSwitchService);

        return this.compare(count);

    }

    private BigInteger executeSQL(UserSwitchService userSwitchService) throws SQLException, DataAccessException {

        if (this.dbConnectionInfo == null) {

            return userSwitchService.doNotLog(this.queryText , BigInteger.class, null, null, null, this.isEnable);

        } else {

            return userSwitchService.doNotLog(this.queryText , BigInteger.class, this.dbConnectionInfo.getUrl(), this.dbConnectionInfo.getUsername(), this.dbConnectionInfo.getPassword(), this.isEnable);

        }

    }

    private boolean compare(BigInteger value) {

        int comparison = value.compareTo(this.referenceValue);

        switch (this.comparisonOperator) {

            case LESS:

                return comparison < 0;

            case EQUALS:

                return comparison == 0;

            case GREATER:

                return comparison > 0;

            case LESS_EQUALS:

                return comparison <= 0;

            case GREATER_EQUALS:

                return comparison >= 0;

        }

        return false;

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        //get data map
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        //extract job from data map
        DataBaseTimeQuery dataBaseTimeQuery = (DataBaseTimeQuery) jobDataMap.get(this.MAP_ME);

        //activate query
        dataBaseTimeQuery.wakeUp();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DBScheduledCountQuery)) {
            return false;
        }

        DBScheduledCountQuery other = (DBScheduledCountQuery) o;

        return this.id.equals(other.id);
    }

    /**
     * check @otherQuery has DBScheduledCountQuery class
     * **/
    @Override
    public boolean equalsByClass(Query otherQuery) {

        if (otherQuery instanceof DataBaseTimeQuery)
            return true;

        return false;
    }

    @Override
    public void updateMe(Query updatedData) throws UpdateException {

        if (! (updatedData instanceof DataBaseTimeQuery))
            throw new UpdateException("Query class doesn't match");

        DataBaseTimeQuery upData = (DataBaseTimeQuery) updatedData;

        super.updateMe(upData);

    }

    @Override
    public String toMailPrettyString() {

        String dbUrl = dbConnectionInfo.getUrl() != null ? dbConnectionInfo.getUrl() : this.defaultDBUrl;

        return String.format(
                "Query information: \nID: %d%n\nDESCRIPTION: %s\nSQL: %s\nDB URL: %s\nTYPE: %s",
                this.id,
                this.description,
                this.queryText,
                dbUrl,
                this.queryType.toString());

    }
}
