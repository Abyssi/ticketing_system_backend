package com.isssr.ticketing_system.model.auto_generated.temporary;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.SoftDelete.SoftDeletable;
import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.model.auto_generated.enumeration.ComparisonOperatorsEnum;
import com.isssr.ticketing_system.response_entity.JsonViews;
import com.isssr.ticketing_system.service.UserSwitchService;
import com.isssr.ticketing_system.service.auto_generated.AutoGeneratedTicketService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.*;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Observable;

@Data
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
public class DataBaseTimeQuery extends Observable implements Job, Serializable, SoftDeletable {

    @Transient
    public static final String MAP_ME = "ME";

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

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    private boolean active = true;

    private boolean deleted;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    private ComparisonOperatorsEnum comparisonOperator;

    private BigInteger referenceValue;

    private BigInteger lastValue;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    private QueryType queryType;

    /*@Transient
    public static final String MAP_ID = "id";

    @Transient
    public static final String MAP_DESCRIPTION = "description";

    @Transient
    public static final String MAP_QUERY_TEXT = "query_text";

    @Transient
    public static final String MAP_QUERY_PRIORITY = "query_priority";

    @Transient
    public static final String MAP_CRON = "cron";*/

    //private String jobKeyName;

    private JobKey jobKey; //key in scheduler at runtime execution

    @Transient
    @Autowired
    private UserSwitchService userSwitchService;

    @Transient
    @Autowired
    private AutoGeneratedTicketService autoGeneratedTicketService;


    public DataBaseTimeQuery(String description, String queryText, TicketPriority queryPriority, String cron,
                             boolean active, boolean deleted, ComparisonOperatorsEnum comparisonOperator,
                             BigInteger referenceValue, BigInteger lastValue, QueryType queryType) {
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
    }

    public void wakeUp() {

        //System.out.println("Observers on query " + this.countObservers());
        //notify observers
        /*setChanged();
        notifyObservers();*/

        if (this.executeQuery(this.userSwitchService)) {
            autoGeneratedTicketService.generateTicket(this);
        }

    }

    public String printQuery() {

        return this.queryText;

    }

    public Boolean executeQuery(UserSwitchService userSwitchService) {

        switch (this.queryType) {

            case DATA_BASE_INSTANT_CHECK:

                return this.executeInstantCheck(userSwitchService);

            case DATA_BASE_TABLE_MONITOR:

                return this.executeMonitorCheck(userSwitchService);

        }

        return false;

    }

    private Boolean executeMonitorCheck(UserSwitchService userSwitchService) {

        if (this.lastValue == null) {

            BigInteger count = (BigInteger) userSwitchService.doQueryReadOnlyMode(this.queryText).get(0);

            //update last value
            this.lastValue = count;

            //TODO update query

            // It's first time running this query.
            // It is equivalent to run an instant check.
            // So return simply false, because is not a monitor action
            return false;

        } else {

            BigInteger count = (BigInteger) userSwitchService.doQueryReadOnlyMode(this.queryText).get(0);

            BigInteger difference = count.andNot(this.lastValue);

            //update last value
            this.lastValue = count;

            //TODO update query

            return this.compare(difference);

        }

    }

    private Boolean executeInstantCheck(UserSwitchService userSwitchService) {

        BigInteger count = (BigInteger) userSwitchService.doQueryReadOnlyMode(this.queryText).get(0);

        return this.compare(count);

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

    private void restoreQuery(DataBaseTimeQuery dataBaseTimeQuery) {

        this.id = dataBaseTimeQuery.id;

        this.description = dataBaseTimeQuery.description;

        this.queryText = dataBaseTimeQuery.queryText;

        this.queryPriority = dataBaseTimeQuery.queryPriority;

        this.cron = dataBaseTimeQuery.cron;

        this.active = dataBaseTimeQuery.active;

        this.deleted = dataBaseTimeQuery.deleted;

        this.comparisonOperator = dataBaseTimeQuery.comparisonOperator;

        this.referenceValue = dataBaseTimeQuery.referenceValue;

        this.lastValue = dataBaseTimeQuery.lastValue;

        this.queryType = dataBaseTimeQuery.queryType;

        //this.jobKeyName = dataBaseTimeQuery.jobKeyName;

        //keep track of job key
        this.jobKey = dataBaseTimeQuery.jobKey;

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //get data map
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        //extract job from data map
        DataBaseTimeQuery dataBaseTimeQuery = (DataBaseTimeQuery) jobDataMap.get(MAP_ME);

        //restore instance
        restoreQuery(dataBaseTimeQuery);

        //activate query
        this.wakeUp();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataBaseTimeQuery)) {
            return false;
        }

        DataBaseTimeQuery other = (DataBaseTimeQuery) o;

        return this.id.equals(other.id);
    }

    /*@Override
    public int hashCode() {
        return this.id.hashCode();
    }*/
}
