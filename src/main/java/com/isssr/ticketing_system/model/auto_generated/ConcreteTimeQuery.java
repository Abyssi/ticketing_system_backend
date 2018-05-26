package com.isssr.ticketing_system.model.auto_generated;

import com.isssr.ticketing_system.model.TicketPriority;


/*@VariableResponseSelector
@Data
@Entity
@Table(name = "ts_concrete_time_query")
@PrimaryKeyJoinColumn(name = "concrete_query_id")
@Getter
@Setter
@NoArgsConstructor*/
public class ConcreteTimeQuery extends ConcreteQuery {

    /*@IncludeInResponse({"base", "full"})
    @NonNull*/
    private String cron;

    public ConcreteTimeQuery(Long id, String queryText, TicketPriority queryPriority, String cron) {
        super(id, queryText, queryPriority);
        this.cron = cron;
    }

    @Override
    public void wakeUp() {
        super.wakeUp();
    }

    @Override
    public void run() {
        this.wakeUp();
    }

    public String getCron() {
        return cron;
    }
}