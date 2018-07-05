package com.isssr.ticketing_system.model.auto_generated.query;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.SoftDelete.SoftDeletable;
import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.model.auto_generated.enumeration.ComparisonOperatorsEnum;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.quartz.Job;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Observable;

@Data
@NoArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Query extends Observable implements Serializable, SoftDeletable {

    @JsonView(JsonViews.Basic.class)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "query_id", updatable = false, nullable = false)
    protected Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    protected String description;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    @ManyToOne
    protected TicketPriority queryPriority;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    protected boolean active = true;

    protected boolean deleted = false;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    protected boolean isEnable;

    public Query(String description,
                 TicketPriority queryPriority,
                 boolean isEnable) {
        this.description = description;
        this.queryPriority = queryPriority;
        this.isEnable = isEnable;
    }

    public Query(String description,
                 TicketPriority queryPriority,
                 boolean active,
                 boolean deleted,
                 boolean isEnable) {
        this.description = description;
        this.queryPriority = queryPriority;
        this.active = active;
        this.deleted = deleted;
        this.isEnable = isEnable;
    }

    public abstract boolean equalsByClass(Query otherQuery);

    public abstract String toMailPrettyString();


    public void updateMe(Query updatedData) throws UpdateException {

        if (this.id.longValue() != updatedData.id.longValue())
            throw new UpdateException("Attempt to update a data base time query record without ID matching");

        this.description = updatedData.description;

        this.queryPriority = updatedData.queryPriority;

        this.isEnable = updatedData.isEnable;

    }

    public void activeMe() { this.active = true; }

    public boolean isActive() { return active; }

    public void disableMe() { this.active = false; }

    public TicketPriority priority() { return this.queryPriority; }

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
}
