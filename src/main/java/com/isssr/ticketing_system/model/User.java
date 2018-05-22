package com.isssr.ticketing_system.model;

import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@VariableResponseSelector

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "ts_user") //user is a reserved word in postgres
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private String firstName;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private String lastName;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private String email;

    @NonNull
    private String password;

    @IncludeInResponse({"full"})
    @NonNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ts_user_role")
    private Collection<Role> roles;

    @IncludeInResponse({"full"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}

