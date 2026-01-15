package com.mainbat.model.equipement;

import com.mainbat.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historique_pannes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriquePanne extends BaseEntity {

    @Column(name = "date_panne", nullable = false)
    private LocalDateTime datePanne;

    @Column(name = "date_resolution")
    private LocalDateTime dateResolution;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cause_panne", columnDefinition = "TEXT")
    private String causePanne;

    @Column(name = "action_corrective", columnDefinition = "TEXT")
    private String actionCorrective;

    @Column
    private Double cout;

    @Column(name = "pieces_remplacees", columnDefinition = "TEXT")
    private String piecesRemplacees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipement_id", nullable = false)
    private Equipement equipement;

    @Column(name = "intervention_id")
    private Long interventionId;
}
