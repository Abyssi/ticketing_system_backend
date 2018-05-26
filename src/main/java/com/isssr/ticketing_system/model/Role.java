package com.isssr.ticketing_system.model;

import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Collection;

@VariableResponseSelector

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "ts_role")
@DynamicInsert
@DynamicUpdate
public class Role {
    @IncludeInResponse({"base", "full"})
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private String name;

    @IncludeInResponse({"full"})
    @NonNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ts_role_privilege")
    private Collection<Privilege> privileges;
}