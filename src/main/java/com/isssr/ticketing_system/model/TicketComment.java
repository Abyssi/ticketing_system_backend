package com.isssr.ticketing_system.model;

import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@VariableResponseSelector

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "ts_ticket_comment")
public class TicketComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private String content;

    @IncludeInResponse({"base", "full"})
    @NonNull
    private Instant timestamp;

    @IncludeInResponse({"base", "full"})
    @NonNull
    @ManyToOne
    private Visibility visibility;

    @IncludeInResponse({"base", "full"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}