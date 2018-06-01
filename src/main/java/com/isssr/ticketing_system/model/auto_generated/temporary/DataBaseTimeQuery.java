package com.isssr.ticketing_system.model.auto_generated.temporary;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.model.auto_generated.enumeration.ComparisonOperatorsEnum;
import com.isssr.ticketing_system.response_entity.JsonViews;
import com.isssr.ticketing_system.service.auto_generated.AutoGeneratedTicketService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Observable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "ts_data_base_time_query")
@DynamicInsert
@DynamicUpdate

@PersistJobDataAfterExecution //persist data after execution of a job
@DisallowConcurrentExecution //avoid race condition on persisted data
public class DataBaseTimeQuery extends Observable implements Job, Serializable {

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

    @NonNull
    @Column(name = "deleted")
    private boolean deleted = false;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    private ComparisonOperatorsEnum comparisonOperator;
    private Long referenceValue;
    private Long lastValue;

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
    @Transient
    private JobKey jobKey; //key in scheduler at runtime execution

    @Transient
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transient
    @Autowired
    private AutoGeneratedTicketService autoGeneratedTicketService;


    public DataBaseTimeQuery(String description, String queryText, TicketPriority queryPriority, String cron,
                             boolean active, boolean deleted, ComparisonOperatorsEnum comparisonOperator,
                             Long referenceValue, Long lastValue, QueryType queryType) {
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

        System.out.println("Observers on query " + this.countObservers());
        //notify observers
        /*setChanged();
        notifyObservers();*/

        if (this.executeQuery(this.jdbcTemplate)) {
            autoGeneratedTicketService.generateTicket(this);
        }

    }

    public String printQuery() {

        return this.queryText;

    }

    public Boolean executeQuery(JdbcTemplate jdbcTemplate) {

        switch (this.queryType) {

            case DATA_BASE_INSTANT_CHECK:

                return this.executeInstantCheck(jdbcTemplate);

            case DATA_BASE_TABLE_MONITOR:

                return this.executeMonitorCheck(jdbcTemplate);

        }

        return false;

    }

    private Boolean executeMonitorCheck(JdbcTemplate jdbcTemplate) {
        return null;
    }

    private Boolean executeInstantCheck(JdbcTemplate jdbcTemplate) {

        Long count = jdbcTemplate.queryForObject(this.queryText, Long.class);

        return this.compareResult(count, this.queryType);

    }

    private Boolean compareResult(Long value, QueryType queryType) {

        switch (queryType) {
            case DATA_BASE_TABLE_MONITOR:

                return compareMonitorResult(value);

            case DATA_BASE_INSTANT_CHECK:

                return compareInstantResult(value);
        }

        return false;


    }

    private boolean compareMonitorResult(Long value) {

        Long difference = value - this.lastValue;

        return compare(difference);

    }

    private boolean compareInstantResult(Long value) {

        return compare(value);

    }

    private boolean compare(Long value) {

        int comparison = value.compareTo(this.referenceValue);

        switch (this.comparisonOperator) {

            case LESS:

                return comparison < 0;

            case EQUALS:

                return comparison == 0;

            case GRATHER:

                return comparison > 0;
            case LESS_EQUALS:

                return comparison <= 0;

            case GRATHER_EQUALS:

                return comparison >= 0;

        }

        return false;

    }

    public TicketPriority priority() {

        return this.queryPriority;

    }

    public void markMeAsDeleted() {

        this.deleted = true;

    }

    public void restoreMe() {

        this.deleted = false;

    }

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
}
