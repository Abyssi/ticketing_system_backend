package com.isssr.ticketing_system.model;

import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@VariableResponseSelector

@Data
@NoArgsConstructor

@Entity
@Table(name = "ts_team")
public class Team {
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
