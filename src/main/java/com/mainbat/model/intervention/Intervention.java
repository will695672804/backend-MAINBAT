package com.mainbat.model.intervention;

import com.mainbat.model.base.BaseEntity;
import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.enums.StatutIntervention;
import com.mainbat.model.enums.TypeIntervention;
import com.mainbat.model.equipement.Equipement;
import com.mainbat.model.equipe.Technicien;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interventions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Intervention extends BaseEntity {

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_intervention", nullable = false)
    private TypeIntervention typeIntervention;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutIntervention statut = StatutIntervention.CREE;

    @Enumerated(EnumType.STRING)
    @Column
    private NiveauCriticite urgence;

    @Column(name = "cause_presumee", columnDefinition = "TEXT")
    private String causePresumee;

    @Column(name = "date_planifiee")
    private LocalDateTime datePlanifiee;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @Column(name = "date_cloture")
    private LocalDateTime dateCloture;

    @Column(name = "duree_estimee_minutes")
    private Integer dureeEstimeeMinutes;

    @Column(name = "duree_reelle_minutes")
    private Integer dureeReelleMinutes;

    @Column(name = "rapport_intervention", columnDefinition = "TEXT")
    private String rapportIntervention;

    @Column(name = "actions_effectuees", columnDefinition = "TEXT")
    private String actionsEffectuees;

    @Column(name = "pieces_utilisees", columnDefinition = "TEXT")
    private String piecesUtilisees;

    // Planification préventive
    @Column(name = "frequence_jours")
    private Integer frequenceJours;  // Pour interventions préventives récurrentes

    @Column(name = "prochaine_date")
    private LocalDateTime prochaineDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipement_id", nullable = false)
    private Equipement equipement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technicien_id")
    private Technicien technicien;

    @Column(name = "validateur_id")
    private Long validateurId;

    @OneToMany(mappedBy = "intervention", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChecklistItem> checklistItems = new ArrayList<>();

    @OneToMany(mappedBy = "intervention", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CoutIntervention> couts = new ArrayList<>();

    @OneToMany(mappedBy = "intervention", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Commentaire> commentaires = new ArrayList<>();

    @OneToMany(mappedBy = "intervention", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PieceJointe> piecesJointes = new ArrayList<>();

    public Double getCoutTotal() {
        return couts.stream()
                .mapToDouble(CoutIntervention::getMontant)
                .sum();
    }
}
