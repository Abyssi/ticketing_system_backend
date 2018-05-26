package com.isssr.ticketing_system.model;

import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
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

@VariableResponseSelector

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "ts_ticket")
@DynamicInsert()
@DynamicUpdate()
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

    @IncludeInResponse({"full"})
    @NonNull
    @Column(name = "deleted")
    private boolean deleted;

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

    public void markMeAsDeleted() {

        this.deleted = true;

    }

    public void restoreMe() {

        this.deleted = false;

    }

    public boolean isDeleted(){

        return this.deleted;

    }
}
