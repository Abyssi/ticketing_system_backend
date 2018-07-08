package com.isssr.ticketing_system.mail.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.model.SoftDelete.SoftDeletableEntity;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "email")
public class Mail extends SoftDeletableEntity {

    @JsonView(JsonViews.IdentifierOnly.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String subject;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String description;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String type;

}
