package com.mainbat.model.equipement;

import com.mainbat.model.base.BaseEntity;
import com.mainbat.model.batiment.Zone;
import com.mainbat.model.enums.EtatEquipement;
import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.intervention.Intervention;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipement extends BaseEntity {

    @Column(nullable = false)
    private String nom;

    @Column(name = "type_equipement")
    private String typeEquipement;  // Climatisation, Ascenseur, Groupe électrogène, etc.

    @Column
    private String marque;

    @Column
    private String modele;

    @Column(name = "numero_serie", unique = true)
    private String numeroSerie;

    @Column(name = "date_mise_service")
    private LocalDate dateMiseService;

    @Column(name = "date_fin_garantie")
    private LocalDate dateFinGarantie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EtatEquipement etat = EtatEquipement.OK;

    @Enumerated(EnumType.STRING)
    @Column
    private NiveauCriticite criticite;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "qr_code")
    private String qrCode;

    @Column(name = "cout_cumule")
    @Builder.Default
    private Double coutCumule = 0.0;

    @Column(name = "heures_fonctionnement")
    private Integer heuresFonctionnement;

    @Column
    private String fournisseur;

    @Column(name = "fournisseur_contact")
    private String fournisseurContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @OneToMany(mappedBy = "equipement", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Intervention> interventions = new ArrayList<>();

    @OneToMany(mappedBy = "equipement", cascade = CascadeType.ALL)
    @Builder.Default
    private List<HistoriquePanne> historiquePannes = new ArrayList<>();
}
