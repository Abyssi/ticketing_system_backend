package com.isssr.ticketing_system.model;

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
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "company")
public class Company extends SoftDeletableEntity {

    @JsonView(JsonViews.IdentifierOnly.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String name;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private boolean enable;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String domain;

    @JsonView(JsonViews.DetailedCompany.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Collection<User> members;

    public Collection<User> getMembers() {
        return this.members == null ? (this.members = new ArrayList<>()) : this.members;
    }
}
