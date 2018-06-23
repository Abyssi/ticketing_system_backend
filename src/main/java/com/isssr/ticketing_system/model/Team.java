package com.isssr.ticketing_system.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.SoftDelete.SoftDeletableEntity;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor

@Entity
@Table(name = "team")
@DynamicInsert
@DynamicUpdate
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Team extends SoftDeletableEntity {
    @JsonView(JsonViews.IdentifierOnly.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String name;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    @OneToOne
    private User leader;

    @JsonView(JsonViews.Basic.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    public void updateMe(@NotNull Team updatedData) throws UpdateException {

        if (this.id.longValue() != updatedData.id.longValue())
            throw new UpdateException("Attempt to update a team record without ID matching");

        this.name = updatedData.name;

        this.leader = updatedData.leader;

        this.members = updatedData.members;

    }
}
