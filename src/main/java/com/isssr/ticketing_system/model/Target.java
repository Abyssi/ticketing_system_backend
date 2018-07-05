package com.isssr.ticketing_system.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.logger.aspect.LogClass;
import com.isssr.ticketing_system.model.SoftDelete.SoftDeletableEntity;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "target")
@DynamicInsert
@DynamicUpdate
@LogClass(idAttrs = {"id"})
public class Target extends SoftDeletableEntity {
    @JsonView(JsonViews.IdentifierOnly.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String name;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String version;

    public void updateMe(@NotNull Target updatedData) throws UpdateException {

        if (this.id.longValue() != updatedData.id.longValue())
            throw new UpdateException("Attempt to update a product record without ID matching");

        this.name = updatedData.name;

        this.version = updatedData.version;

    }
}
