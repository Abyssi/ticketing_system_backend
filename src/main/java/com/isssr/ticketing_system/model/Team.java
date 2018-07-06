package com.isssr.ticketing_system.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.logger.aspect.LogClass;
import com.isssr.ticketing_system.model.SoftDelete.SoftDeletableEntity;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "team")
@LogClass(idAttrs = {"id"})
public class Team extends SoftDeletableEntity {
    @JsonView(JsonViews.IdentifierOnly.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String name;

    @JsonView(JsonViews.DetailedTeam.class)
    @NonNull
    @OneToOne
    private User leader;

    @JsonView(JsonViews.DetailedTeam.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "team_id")
    private Collection<User> members;

    // leader should be one of members
    public Team(String name, User leader) {
        this.name = name;
        this.setLeader(leader);
    }

    public void setLeader(User leader) {
        if (this.leader != null) this.getMembers().remove(this.leader);
        this.getMembers().add(leader);
        this.leader = leader;
    }

    public Collection<User> getMembers() {
        return this.members == null ? (this.members = new ArrayList<>()) : this.members;
    }
}
