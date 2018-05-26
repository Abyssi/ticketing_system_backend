package com.isssr.ticketing_system.model;

import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@VariableResponseSelector

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "ts_user") //user is a reserved word in postgres
@DynamicInsert()
@DynamicUpdate()
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

    public void updateMe(@NotNull User updatedData) throws UpdateException {

        if (this.id.longValue() != updatedData.id.longValue())
            throw new UpdateException("Attempt to update a User record without ID matching");

        this.firstName = updatedData.firstName;

        this.lastName = updatedData.lastName;

        this.email = updatedData.email;

        //no password update because in this method is always null
        //this.password = updatedData.password;

        this.roles = updatedData.roles;

        this.team = updatedData.team;
    }

    public void updatePassword(@NotNull Long id, @NotNull String password){
        //TODO update only password
    }
}

