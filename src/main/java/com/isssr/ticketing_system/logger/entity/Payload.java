package com.isssr.ticketing_system.logger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payload {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(columnDefinition="TEXT")
    private String json;
    @ManyToOne
    private Record record;

    private String objectId;
    private String type;
    private String classType;


    public Payload(String json, String objectId, String type, String classType, Record record) {
        this.json = json;
        this.objectId = objectId;
        this.type = type;
        this.classType = classType;
        this.record = record;
    }

}
