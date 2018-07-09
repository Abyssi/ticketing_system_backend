package com.isssr.ticketing_system.model.auto_generated.query;

import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.model.auto_generated.enumeration.ComparisonOperatorsEnum;
import com.isssr.ticketing_system.model.db_connection.DBConnectionInfo;
import com.isssr.ticketing_system.service.UserSwitchService;
import com.isssr.ticketing_system.service.auto_generated.QueryService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.dao.DataAccessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;
import java.sql.SQLException;


@Data
@NoArgsConstructor

@Entity
@Table(name = "db_scheduled_count_query")
@DynamicInsert
@DynamicUpdate

@PersistJobDataAfterExecution //persist data after execution of a job
@DisallowConcurrentExecution //avoid race condition on persisted data
@FilterDef(name = "deleted_filter", parameters = {@ParamDef(name = "value", type = "boolean")})
@Filter(name = "deleted_filter", condition = "deleted = :value")
//@LogClass(idAttrs = {"id"})
public class DBScheduledCountQuery extends DBScheduledQuery<BigInteger, ComparisonOperatorsEnum> {

    public DBScheduledCountQuery(String description, TicketPriority queryPriority, boolean isEnable, String author,
                                 String cron, String queryText, DBConnectionInfo dbConnectionInfo, QueryType queryType,
                                 ComparisonOperatorsEnum comparisonOperator, BigInteger referenceValue) {
        super(description, queryPriority, isEnable, author, cron, queryText, dbConnectionInfo, queryType,
                comparisonOperator, referenceValue);
    }

    public DBScheduledCountQuery(String description, TicketPriority queryPriority, boolean active, boolean deleted,
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

            BigInteger lastNeg = this.lastValue.multiply(BigInteger.valueOf(-1));

            BigInteger difference = count.add(lastNeg);

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

            if (this.isEnable)
                return userSwitchService.doQueryReadOnlyMode(this.queryText, BigInteger.class, null, null, null, null);

            return userSwitchService.doNotLog(this.queryText, BigInteger.class, null, null, null, null);

        } else {

            if (this.isEnable)
                return userSwitchService.doQueryReadOnlyMode(this.queryText, BigInteger.class, this.dbConnectionInfo.getUrl(), this.dbConnectionInfo.getUsername(), this.dbConnectionInfo.getPassword(), this.dbConnectionInfo.getDriver());

            return userSwitchService.doNotLog(this.queryText, BigInteger.class, this.dbConnectionInfo.getUrl(), this.dbConnectionInfo.getUsername(), this.dbConnectionInfo.getPassword(), this.dbConnectionInfo.getDriver());

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

        // compute percent from value (this is a monitoring query
        // so it is the difference between instant value and last value)
        // and this.lastValue that has already been updated with last instant value
        float percent = percent(value, this.lastValue);

        switch (this.comparisonOperator) {
            case PERCENT_GROWTH:

                return percent >= this.referenceValue.floatValue();

            case PERCENT_DROP:

                return percent < this.referenceValue.multiply(BigInteger.valueOf(-1)).floatValue();

        }

        return false;

    }

    /**
     * Returns a proportion (n out of a total) as a percentage, in a float.
     */
    private float percent(BigInteger n, BigInteger total) {

        float ratio = (n.floatValue()) / (total.floatValue());

        return ratio * 100;

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        //get data map
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        //extract job from data map
        DBScheduledCountQuery dbSchedulableTimeQuery = (DBScheduledCountQuery) jobDataMap.get(this.MAP_ME);

        //activate query
        dbSchedulableTimeQuery.wakeUp();
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
     **/
    @Override
    public boolean equalsByClass(Query otherQuery) {

        if (otherQuery instanceof DBScheduledCountQuery)
            return true;

        return false;
    }

    @Override
    public void updateMe(Query updatedData) throws UpdateException {

        if (!(updatedData instanceof DBScheduledCountQuery))
            throw new UpdateException("Query class doesn't match");

        DBScheduledCountQuery upData = (DBScheduledCountQuery) updatedData;

        super.updateMe(upData);

        this.comparisonOperator = upData.comparisonOperator;

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
