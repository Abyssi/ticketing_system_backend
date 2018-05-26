package com.isssr.ticketing_system.model.auto_generated.decorator;

import com.isssr.ticketing_system.model.auto_generated.Query;
import com.isssr.ticketing_system.model.auto_generated.enumeration.ComparisonOperatorsEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/*@VariableResponseSelector
@Data
@Entity
@Table(name = "ts_data_base_query")
@PrimaryKeyJoinColumn(name = "query_decorator_id")
@Getter
@Setter
@NoArgsConstructor*/
public class DataBaseQuery extends QueryDecorator {

    /*@JsonIgnore
     @NonNull*/
    protected Class referenceClass;

    /*@JsonIgnore
    @NonNull*/
    protected Method methodToCall;

    /*@IncludeInResponse({"full"})
    @NonNull*/
    protected Long referenceValue;


    /*@IncludeInResponse({"full"})
    @NonNull*/
    protected ComparisonOperatorsEnum comparisonOperator;

    /*@JsonIgnore*/
    protected List<Long> lastValues;

    public DataBaseQuery(Query query, Class referenceClass, Method methodToCall, Long referenceValue,
                         ComparisonOperatorsEnum comparisonOperator, List<Long> lastValues) {
        super(query);
        this.referenceClass = referenceClass;
        this.methodToCall = methodToCall;
        this.referenceValue = referenceValue;
        this.comparisonOperator = comparisonOperator;
        this.lastValues = lastValues;
    }

    @Override
    public void wakeUp() {
        super.wakeUp();
    }

    @Override
    public String getCron() {
        return super.getCron();
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public Boolean executeQuery(Object o) {

        if (o instanceof JpaRepository) {

            try {

                Object result = this.methodToCall.invoke(o);

                if (result instanceof java.lang.Long) {

                    return compareResult((java.lang.Long) result);
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return super.executeQuery(o);
            }

        }

        return super.executeQuery(o);


    }

    private boolean compareResult(java.lang.Long value) {

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

    public Class getReferenceClass() {
        return referenceClass;
    }
}