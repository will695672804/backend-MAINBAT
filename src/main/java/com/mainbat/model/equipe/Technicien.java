package com.mainbat.model.equipe;

import com.mainbat.model.base.BaseEntity;
import com.mainbat.model.intervention.Intervention;
import com.mainbat.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "techniciens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Technicien extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String matricule;

    @Column(name = "telephone_urgence")
    private String telephoneUrgence;

    @ElementCollection
    @CollectionTable(name = "technicien_competences", joinColumns = @JoinColumn(name = "technicien_id"))
    @Column(name = "competence")
    @Builder.Default
    private Set<String> competences = new HashSet<>();  // Électricité, Climatisation, Plomberie, IT...

    @ElementCollection
    @CollectionTable(name = "technicien_certifications", joinColumns = @JoinColumn(name = "technicien_id"))
    @Column(name = "certification")
    @Builder.Default
    private Set<String> certifications = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "technicien_zones", joinColumns = @JoinColumn(name = "technicien_id"))
    @Column(name = "zone_intervention")
    @Builder.Default
    private Set<String> zonesIntervention = new HashSet<>();

    @Column(name = "disponible")
    @Builder.Default
    private Boolean disponible = true;

    @Column(name = "en_astreinte")
    @Builder.Default
    private Boolean enAstreinte = false;

    @Column(name = "charge_travail")
    @Builder.Default
    private Integer chargeTravail = 0;  // Nombre d'interventions en cours

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    @OneToMany(mappedBy = "technicien")
    @Builder.Default
    private List<Intervention> interventions = new ArrayList<>();
}
