package com.isssr.ticketing_system.model;

import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@VariableResponseSelector

@Data
@NoArgsConstructor

@Entity
@Table(name = "ts_team")
@DynamicInsert
@DynamicUpdate
public class Team {
    @IncludeInResponse({"base", "full"})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private String name;

    @IncludeInResponse({"base", "full"})
    @NonNull
    @OneToOne
    private User leader;

    @IncludeInResponse({"full"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private Collection<User> members;

    @IncludeInResponse({"full"})
    @NonNull
    @Column(name = "deleted")
    private boolean deleted;

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

    public void markMeAsDeleted() {

        this.deleted = true;

    }

    public void restoreMe() {

        this.deleted = false;

    }

    public boolean isDeleted() {

        return this.deleted;

    }
}
