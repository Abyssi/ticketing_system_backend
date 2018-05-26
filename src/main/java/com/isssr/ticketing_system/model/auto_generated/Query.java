package com.isssr.ticketing_system.model.auto_generated;

import com.isssr.ticketing_system.model.TicketPriority;

import java.util.Observable;

/*@VariableResponseSelector
@Entity
@Table(name = "ts_query")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor*/
public abstract class Query extends Observable implements Runnable {

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

    /*@IncludeInResponse({"full"})
    @NonNull*/
    protected boolean deleted = false;

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
}