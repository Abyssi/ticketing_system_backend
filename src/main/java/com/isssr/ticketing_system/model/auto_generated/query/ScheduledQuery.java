package com.isssr.ticketing_system.model.auto_generated.query;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobKey;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ScheduledQuery extends Query implements Job {

    @Transient
    public static final String MAP_ME = "ME";

    @JsonView(JsonViews.Basic.class)
    @NonNull
    protected String cron;

    protected JobKey jobKey; //key in scheduler at runtime execution

    public ScheduledQuery(String description, TicketPriority queryPriority, boolean isEnable, String cron, JobKey jobKey) {
        super(description, queryPriority, isEnable);
        this.cron = cron;
        this.jobKey = jobKey;
    }

    public ScheduledQuery(String cron, JobKey jobKey) {
        this.cron = cron;
        this.jobKey = jobKey;
    }

    public ScheduledQuery(String description, TicketPriority queryPriority, boolean active, boolean deleted, boolean isEnable, String cron, JobKey jobKey) {
        super(description, queryPriority, active, deleted, isEnable);
        this.cron = cron;
        this.jobKey = jobKey;
    }

    @Override
    public void updateMe(Query updatedData) throws UpdateException {

        if (! (updatedData instanceof ScheduledQuery))
            throw new UpdateException("Query class doesn't match");

        ScheduledQuery upData = (ScheduledQuery) updatedData;

        super.updateMe(upData);

        this.cron = upData.cron;

    }
}
