package com.isssr.ticketing_system.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "ts_role")
@DynamicInsert
@DynamicUpdate
public class Role {
    @JsonView(JsonViews.IdentifierOnly.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String name;

    @JsonView(JsonViews.DetailedRole.class)
    @NonNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ts_role_privilege")
    private Collection<Privilege> privileges;
}