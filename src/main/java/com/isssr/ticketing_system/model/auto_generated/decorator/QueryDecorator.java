package com.isssr.ticketing_system.model.auto_generated.decorator;

import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.model.auto_generated.Query;
import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/*@VariableResponseSelector
@Data
@Entity
@Table(name = "ts_query_decorator")
@PrimaryKeyJoinColumn(name = "query_id")
@Getter
@Setter
@NoArgsConstructor*/
public class QueryDecorator extends Query {

    /*@IncludeInResponse({"base", "full"})
    @NonNull
    @ManyToOne*/
    protected Query query;

    public QueryDecorator(Query query) {
        this.query = query;
    }


    @Override
    public void wakeUp() {
        System.out.println("Observers on query " + this.countObservers());
        //notify observers
        setChanged();
        notifyObservers();
        this.query.wakeUp();
    }

    @Override
    public String getCron() {
        return query.getCron();
    }

    @Override
    public String printQuery() {
        return this.query.printQuery();
    }

    @Override
    public Boolean executeQuery(Object o) {
        return this.query.executeQuery(o);
    }

    @Override
    public TicketPriority priority() {
        return this.query.priority();
    }

    @Override
    public void run() {
        this.wakeUp();
    }
}
