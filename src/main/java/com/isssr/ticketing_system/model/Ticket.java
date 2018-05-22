package com.isssr.ticketing_system.model;

import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

@VariableResponseSelector

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "ts_ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IncludeInResponse({"base", "full"})
    @NonNull
    @ManyToOne
    private TicketStatus status;

    @IncludeInResponse({"full"})
    @NonNull
    @ManyToOne
    private TicketSource source;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private Instant creationTimestamp;

    @IncludeInResponse({"base", "full"})
    @NonNull
    @ManyToOne
    private TicketCategory category;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private String title;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private String description;

    @IncludeInResponse({"full"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id")
    private Collection<TicketAttachment> attachments;

    @IncludeInResponse({"base", "full"})
    @NonNull
    @ManyToOne
    private User assignee;

    @IncludeInResponse({"base", "full"})
    @NonNull
    @ManyToOne
    private Product target;

    @IncludeInResponse({"base", "full"})
    @NonNull
    @ManyToOne
    private TicketPriority customerPriority;

    @IncludeInResponse({"base", "full"})
    @ManyToOne
    private TicketPriority teamPriority;

    @IncludeInResponse({"base", "full"})
    @NonNull
    @ManyToOne
    private Visibility visibility;

    @IncludeInResponse({"full"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id")
    private Collection<TicketRelation> relations;

    @IncludeInResponse
    @ManyToOne
    private TicketDifficulty difficulty;

    @IncludeInResponse({"full"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id")
    private Collection<TicketEvent> events;

    @IncludeInResponse({"full"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id")
    private Collection<TicketComment> comments;

    public Collection<TicketAttachment> getAttachments() {
        return this.attachments == null ? (this.attachments = new ArrayList<>()) : this.attachments;
    }

    public Collection<TicketRelation> getRelations() {
        return this.relations == null ? (this.relations = new ArrayList<>()) : this.relations;
    }

    public Collection<TicketEvent> getEvents() {
        return this.events == null ? (this.events = new ArrayList<>()) : this.events;
    }

    public Collection<TicketComment> getComments() {
        return this.comments == null ? (this.comments = new ArrayList<>()) : this.comments;
    }
}
