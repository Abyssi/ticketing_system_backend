package com.isssr.ticketing_system.model.auto_generated.query;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.model.db_connection.DBConnectionInfo;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class DBScheduledQuery<T, S> extends ScheduledQuery {

    @Transient
    @Value("${spring.datasource.url}")
    protected String defaultDBUrl;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    protected String queryText;

    @JsonView(JsonViews.Detailed.class)
    @ManyToOne
    protected DBConnectionInfo dbConnectionInfo;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    protected QueryType queryType;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    @Type(type = "java.lang.Enum")
    protected S comparisonOperator;

    @JsonView(JsonViews.Detailed.class)
    @Type(type = "java.lang.Number")
    protected T referenceValue;

    @Type(type = "java.lang.Number")
    protected T lastValue;

    public DBScheduledQuery(String description, TicketPriority queryPriority, boolean isEnable, String author, String cron, String queryText, DBConnectionInfo dbConnectionInfo, QueryType queryType, S comparisonOperator, T referenceValue) {
        super(description, queryPriority, isEnable, author, cron);
        this.queryText = queryText;
        this.dbConnectionInfo = dbConnectionInfo;
        this.queryType = queryType;
        this.comparisonOperator = comparisonOperator;
        this.referenceValue = referenceValue;
    }

    public DBScheduledQuery(String description, TicketPriority queryPriority, boolean active, boolean deleted, boolean isEnable, String author, String cron, String queryText, DBConnectionInfo dbConnectionInfo, QueryType queryType, S comparisonOperator, T referenceValue) {
        super(description, queryPriority, active, deleted, isEnable, author, cron);
        this.queryText = queryText;
        this.dbConnectionInfo = dbConnectionInfo;
        this.queryType = queryType;
        this.comparisonOperator = comparisonOperator;
        this.referenceValue = referenceValue;
    }

    public String printQuery() {
        return this.queryText;
    }

    @Override
    public void updateMe(Query updatedData) throws UpdateException {

        if (!(updatedData instanceof DBScheduledQuery))
            throw new UpdateException("Query class doesn't match");

        DBScheduledQuery upData = (DBScheduledQuery) updatedData;

        super.updateMe(upData);

        this.queryText = upData.queryText;

        this.dbConnectionInfo = upData.dbConnectionInfo;

        this.queryType = upData.queryType;
    }
}
