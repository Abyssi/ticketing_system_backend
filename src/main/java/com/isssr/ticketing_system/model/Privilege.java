package com.isssr.ticketing_system.model;

import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@VariableResponseSelector

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "ts_privilege")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private String name;
}