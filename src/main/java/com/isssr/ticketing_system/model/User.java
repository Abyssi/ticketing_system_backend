package com.isssr.ticketing_system.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "user") //user is a reserved word in postgres
@DynamicInsert
@DynamicUpdate
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @JsonView(JsonViews.IdentifierOnly.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String firstName;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String lastName;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String email;

    @NonNull
    private String password;

    @JsonView(JsonViews.DetailedUser.class)
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @JsonView(JsonViews.DetailedUser.class)
    @NonNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role")
    private Collection<Role> roles;

    @JsonView(JsonViews.DetailedUser.class)
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

    public void updatePassword(@NotNull Long id, @NotNull String password) {
        //TODO update only password
    }
}

