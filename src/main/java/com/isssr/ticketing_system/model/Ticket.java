package com.isssr.ticketing_system.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.SoftDelete.SoftDeletableEntity;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "ts_ticket")
@DynamicInsert
@DynamicUpdate
public class Ticket extends SoftDeletableEntity {
    @JsonView(JsonViews.IdentifierOnly.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    @ManyToOne
    private TicketStatus status;

    @JsonView(JsonViews.DetailedTicket.class)
    @NonNull
    @ManyToOne
    private TicketSource source;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private Instant creationTimestamp;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    @ManyToOne
    private TicketCategory category;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String title;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonView(JsonViews.DetailedTicket.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id")
    private Collection<TicketAttachment> attachments;

    @JsonView(JsonViews.Basic.class)
    @ManyToOne
    private User assignee;

    @JsonView(JsonViews.Basic.class)
    @ManyToOne
    private User customer;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    @ManyToOne
    private Target target;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    @ManyToOne
    private TicketPriority customerPriority;

    @JsonView(JsonViews.Basic.class)
    @ManyToOne
    private TicketPriority teamPriority;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    @ManyToOne
    private Visibility visibility;

    @JsonView(JsonViews.DetailedTicket.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id")
    private Collection<TicketRelation> relations;

    @JsonView(JsonViews.DetailedTicket.class)
    @ManyToOne
    private TicketDifficulty difficulty;

    @JsonView(JsonViews.DetailedTicket.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id")
    private Collection<TicketEvent> events;

    @JsonView(JsonViews.DetailedTicket.class)
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

    public void updateMe(@NotNull Ticket updatedData) throws UpdateException {

        if (this.id.longValue() != updatedData.id.longValue())
            throw new UpdateException("Attempt to update a ticket record without ID matching");

        this.status = updatedData.status;

        this.source = updatedData.source;

        this.category = updatedData.category;

        this.title = updatedData.title;

        this.description = updatedData.description;

        this.attachments = updatedData.attachments;

        this.assignee = updatedData.assignee;

        this.target = updatedData.target;

        this.customerPriority = updatedData.customerPriority;

        this.teamPriority = updatedData.teamPriority;

        this.visibility = updatedData.visibility;

        this.relations = updatedData.relations;

        this.difficulty = updatedData.difficulty;

        this.events = updatedData.events;

        this.comments = updatedData.comments;
    }
}
