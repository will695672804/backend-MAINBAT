package com.mainbat.model.notification;

import com.mainbat.model.base.BaseEntity;
import com.mainbat.model.enums.NiveauCriticite;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alertes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerte extends BaseEntity {

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "type_alerte")
    private String typeAlerte;  // EQUIPEMENT_HS, PANNE_REPETEE, MAINTENANCE_RETARD, BUDGET_DEPASSE

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauCriticite niveau;

    @Column(name = "target_type")
    private String targetType;  // EQUIPEMENT, INTERVENTION, BATIMENT

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "is_resolved")
    @Builder.Default
    private Boolean isResolved = false;

    @Column(name = "resolved_by")
    private Long resolvedBy;

    @Column(name = "resolution_note", columnDefinition = "TEXT")
    private String resolutionNote;
}
