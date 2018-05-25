package com.isssr.ticketing_system.model;

import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@VariableResponseSelector

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "ts_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private String name;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private String version;

    public void updateMe(@NotNull Product updatedData) throws UpdateException {

        if (this.id.longValue() != updatedData.id.longValue())
            throw new UpdateException("Attempt to update a product record without ID matching");

        this.name = updatedData.name;

        this.version = updatedData.version;

    }
}
