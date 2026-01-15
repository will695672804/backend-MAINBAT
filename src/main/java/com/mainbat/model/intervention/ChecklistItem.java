package com.mainbat.model.intervention;

import com.mainbat.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "checklist_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChecklistItem extends BaseEntity {

    @Column(nullable = false)
    private String libelle;

    @Column
    private String description;

    @Column(name = "est_complete")
    @Builder.Default
    private Boolean estComplete = false;

    @Column(name = "ordre")
    private Integer ordre;

    @Column
    private String commentaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intervention_id", nullable = false)
    private Intervention intervention;
}
