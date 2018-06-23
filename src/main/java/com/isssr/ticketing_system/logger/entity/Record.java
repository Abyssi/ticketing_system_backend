package it.uniroma2.ticketingsystem.logger.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Record {

    @Id
    @GeneratedValue
    private Integer id;

    private String operationName;
    private String author;
    private String tag;
    private Timestamp timestamp;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
    private Set<Payload> payloads;



    public Record(@NotNull String operationName, String author,  String tag){

        this.operationName = operationName;
        this.author = author;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.tag = tag;

    }

    public Record(String operationName, String author, String tag, Set<Payload> payloads, Timestamp recordTimeStamp) {
        this.operationName = operationName;
        this.author = author;
        this.tag = tag;
        this.payloads = payloads;
        this.timestamp = recordTimeStamp;
    }
}
