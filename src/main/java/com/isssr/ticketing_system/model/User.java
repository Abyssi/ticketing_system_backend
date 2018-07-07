package com.isssr.ticketing_system.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.logger.aspect.LogClass;
import com.isssr.ticketing_system.model.SoftDelete.SoftDeletableEntity;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "user")
@LogClass(idAttrs = {"id"})
public class User extends SoftDeletableEntity {
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
}

