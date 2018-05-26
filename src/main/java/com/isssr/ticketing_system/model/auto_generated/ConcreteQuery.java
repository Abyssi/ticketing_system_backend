package com.isssr.ticketing_system.model.auto_generated;

import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/*@VariableResponseSelector
@Data
@Entity
@Table(name = "ts_concrete_query")
@PrimaryKeyJoinColumn(name = "query_id")
@Getter
@Setter
@NoArgsConstructor*/
public class ConcreteQuery extends Query {

    public ConcreteQuery(Long id, String queryText, TicketPriority queryPriority) {
        this.id = id;
        this.queryText = queryText;
        this.queryPriority = queryPriority;
    }

    @Override
    public void wakeUp() {
        System.out.println(queryText);
    }

    @Override
    public String getCron() {
        return null;
    }

    @Override
    public String printQuery() {
        return this.queryText;
    }

    @Override
    public Boolean executeQuery(Object o) {

        return false;

    }

    @Override
    public TicketPriority priority() {
        return this.queryPriority;
    }

    @Override
    public void run() {
        this.wakeUp();
    }
}