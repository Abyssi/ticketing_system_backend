package com.isssr.ticketing_system.model.auto_generated;

import com.isssr.ticketing_system.model.SoftDelete.SoftDeletable;
import com.isssr.ticketing_system.model.TicketPriority;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.Observable;

/*@VariableResponseSelector
@Entity
@Table(name = "ts_query")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor*/
@FilterDef(name = "deleted_filter", parameters = {@ParamDef(name = "value", type = "boolean")})
@Filter(name = "deleted_filter", condition = "deleted = :value")
public abstract class Query extends Observable implements Runnable, SoftDeletable {

    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "query_id", updatable = false, nullable = false)*/
    protected Long id;

    /*@IncludeInResponse({"base", "full"})
    @NonNull*/
    protected String queryText;

    /*@IncludeInResponse({"base", "full"})
    @NonNull
    @ManyToOne*/
    protected TicketPriority queryPriority;

    /*@IncludeInResponse({"base", "full"})
    @NonNull*/
    protected boolean active = true;

    private boolean deleted;

    /**
     * implement this method to activate query process
     **/
    public abstract void wakeUp();

    /**
     * return cron only if query is a composition of ConcreteTimeQuery
     **/
    public abstract String getCron();

    public abstract String printQuery();

    public abstract Boolean executeQuery(Object o);

    public abstract TicketPriority priority();


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
}